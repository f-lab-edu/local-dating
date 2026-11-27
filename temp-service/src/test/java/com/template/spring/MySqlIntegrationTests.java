package com.template.spring;

import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Import;

@Tag("mysql")
@Import(TestcontainersConfiguration.class)
class MySqlIntegrationTests extends AbstractNoteApiTests {
}
