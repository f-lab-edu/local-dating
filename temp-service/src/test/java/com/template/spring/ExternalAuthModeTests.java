package com.template.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 일반 마이크로서비스에서 발급용 Bean과 로그인 기능이 활성화되지 않는지 확인한다.
@SpringBootTest(properties = {
        "app.auth.mode=external",
        "app.auth.issuer=https://issuer.example.invalid",
        "app.auth.jwk-set-uri=https://issuer.example.invalid/jwks"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExternalAuthModeTests {
    @Autowired ApplicationContext context;
    @Autowired MockMvc mvc;

    @Test
    void externalModeDoesNotCreateSigningBeans() throws Exception {
        assertThat(context.getBeansOfType(JwtEncoder.class)).isEmpty();
        assertThat(context.getBeansOfType(java.security.KeyPair.class)).isEmpty();
        assertThat(context.getBeansOfType(JwtDecoder.class)).hasSize(1);
        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test-user\",\"password\":\"test-only-password\"}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/v1/notes")).andExpect(status().isUnauthorized());
    }
}
