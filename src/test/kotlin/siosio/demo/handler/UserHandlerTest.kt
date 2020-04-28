package siosio.demo.handler

import org.assertj.db.api.Assertions
import org.assertj.db.type.Table
import org.assertj.db.type.Changes
import org.flywaydb.core.Flyway
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import javax.sql.DataSource

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
internal class UserHandlerTest(
        val mockMvc: MockMvc,
        val dataSource: DataSource,
        val flyway: Flyway
) {

    @BeforeEach
    internal fun setUp() {
        flyway.clean()
        flyway.migrate()
    }

    companion object {

        @JvmStatic
        @Container
        val container = MySQLContainer<Nothing>()

        @DynamicPropertySource
        @JvmStatic
        fun changeProperty(registry: DynamicPropertyRegistry): Unit {
            println("container.jdbcUrl = ${container.jdbcUrl}")
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }
    }

    @Test
    internal fun ユーザが登録出来るよ() {
        val table = Table(dataSource, "user")
        val changes = Changes(table)
        changes.setStartPointNow()

        mockMvc.post("/api/users") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"name": "siosio"}"""
        }.andExpect {
            status { isOk }
        }

        changes.setEndPointNow()
        Assertions.assertThat(changes)
                .hasNumberOfChanges(1)
                .changeOfCreation()
                .rowAtEndPoint().value("name").isEqualTo("siosio")
    }

    @Test
    internal fun ユーザ一覧が取得できるよ() {

        mockMvc.get("/api/users") {
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.users.length()") { value(3) }
            jsonPath("$.users[*].name") { value(contains("user_1", "user_2", "user_3")) }
            jsonPath("$.users[*].id") { value(contains(1, 2, 3)) }
        }
    }
}