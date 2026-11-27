package com.template.spring;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
abstract class AbstractNoteApiTests {

    private static final String NOTE = """
            {"title":"First note","content":"Example content"}
            """;

    @Autowired
    MockMvc mvc;

    @Autowired
    org.springframework.security.oauth2.jwt.JwtEncoder jwtEncoder;

    @Autowired
    @org.springframework.beans.factory.annotation.Value("${app.auth.audience}")
    String audience;

    @Test
    void publicEndpointsAreAccessibleWithoutAuthentication() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk());
        mvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
        mvc.perform(get("/actuator/health/liveness")).andExpect(status().isOk());
        mvc.perform(get("/actuator/health/readiness")).andExpect(status().isOk());
    }

    @Test
    void apiDocumentationContainsBearerScheme() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath("$.paths['/api/v1/notes']").exists());
    }

    @Test
    void apiRequiresAuthentication() throws Exception {
        mvc.perform(get("/api/v1/notes")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/v1/notes").contentType(MediaType.APPLICATION_JSON).content(NOTE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginChecksConfiguredPassword() throws Exception {
        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test-user\",\"password\":\"incorrect\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void realJwtAllowsAuthenticatedWriteWithoutSession() throws Exception {
        String response = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test-user\",\"password\":\"test-only-password\"}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String token = JsonPath.read(response, "$.accessToken");
        var result = mvc.perform(post("/api/v1/notes").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(NOTE))
                .andExpect(status().isCreated()).andReturn();
        org.assertj.core.api.Assertions.assertThat(result.getRequest().getSession(false)).isNull();
    }

    @Test
    @WithMockUser(authorities = "SCOPE_notes.read")
    void writesRequireWriteScopeAndRole() throws Exception {
        mvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON).content(NOTE))
                .andExpect(status().isForbidden());
    }

    @Test
    void jwtValidationRejectsWrongAudienceAndExpiredTokens() throws Exception {
        var now = java.time.Instant.now();
        for (var claims : java.util.List.of(
                org.springframework.security.oauth2.jwt.JwtClaimsSet.builder()
                        .issuer("urn:template:test").subject("test-user").audience(java.util.List.of("other-service"))
                        .issuedAt(now).expiresAt(now.plusSeconds(900)).claim("scope", "notes.read").build(),
                org.springframework.security.oauth2.jwt.JwtClaimsSet.builder()
                        .issuer("urn:template:test").subject("test-user").audience(java.util.List.of(audience))
                        .issuedAt(now.minusSeconds(1000)).expiresAt(now.minusSeconds(120)).claim("scope", "notes.read").build())) {
            String token = jwtEncoder.encode(org.springframework.security.oauth2.jwt.JwtEncoderParameters.from(claims)).getTokenValue();
            mvc.perform(get("/api/v1/notes").header("Authorization", "Bearer " + token))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "SCOPE_notes.read", "SCOPE_notes.write"})
    void noteCanBeCreatedReadUpdatedAndDeleted() throws Exception {
        String location = createNote();
        mvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("First note"))
                .andExpect(jsonPath("$.createdAt").value(endsWith("Z")));
        mvc.perform(put(location).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Updated","content":"Changed content"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
        mvc.perform(get(location)).andExpect(jsonPath("$.content").value("Changed content"));
        mvc.perform(delete(location)).andExpect(status().isNoContent());
        mvc.perform(get(location)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "SCOPE_notes.read", "SCOPE_notes.write"})
    void listHasStablePaginationMetadata() throws Exception {
        createNote();
        createNote();
        mvc.perform(get("/api/v1/notes").param("page", "0").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "SCOPE_notes.read", "SCOPE_notes.write"})
    void invalidBodyReturnsFieldErrors() throws Exception {
        mvc.perform(post("/api/v1/notes").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":" ","content":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "SCOPE_notes.read", "SCOPE_notes.write"})
    void oversizedBodyIsRejectedBeforePersistence() throws Exception {
        String body = "{\"title\":\"" + "x".repeat(101) + "\",\"content\":\"Valid content\"}";
        mvc.perform(post("/api/v1/notes").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "SCOPE_notes.read", "SCOPE_notes.write"})
    void malformedJsonReturnsBadRequest() throws Exception {
        mvc.perform(post("/api/v1/notes").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "SCOPE_notes.read", "SCOPE_notes.write"})
    void paginationAndIdentifiersAreValidated() throws Exception {
        mvc.perform(get("/api/v1/notes").param("page", "-1")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/notes").param("size", "0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/notes").param("size", "101")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/notes/0")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/v1/notes/not-a-number")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "SCOPE_notes.read", "SCOPE_notes.write"})
    void missingNoteReturnsProblemDetail() throws Exception {
        mvc.perform(get("/api/v1/notes/9223372036854775807"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "SCOPE_notes.read", "SCOPE_notes.write"})
    void updateAndDeleteDoNotCreateMissingNotes() throws Exception {
        mvc.perform(put("/api/v1/notes/9223372036854775807")
                        .contentType(MediaType.APPLICATION_JSON).content(NOTE))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/api/v1/notes/9223372036854775807"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "SCOPE_notes.read", "SCOPE_notes.write"})
    void unsupportedMethodReturnsMethodNotAllowed() throws Exception {
        mvc.perform(put("/api/v1/notes").contentType(MediaType.APPLICATION_JSON).content(NOTE))
                .andExpect(status().isMethodNotAllowed());
    }

    private String createNote() throws Exception {
        return mvc.perform(post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON).content(NOTE))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getHeader("Location");
    }
}
