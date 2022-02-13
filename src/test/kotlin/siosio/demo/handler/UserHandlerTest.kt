package siosio.demo.handler

import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringExtension
import org.assertj.db.api.Assertions
import org.assertj.db.type.Changes
import org.assertj.db.type.Table
import org.flywaydb.core.Flyway
import org.hamcrest.Matchers.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import javax.sql.DataSource

@SpringBootTest
@AutoConfigureMockMvc
internal class UserHandlerTest(
    val mockMvc: MockMvc,
    val dataSource: DataSource,
    val flyway: Flyway
) : FreeSpec({

    extension(SpringExtension)

    beforeTest {
        flyway.clean()
        flyway.migrate()
    }

    "ユーザが登録できるよ" {
        println("dataSource.connection.metaData.databaseProductVersion = ${dataSource.connection.metaData.databaseProductVersion}")
        val table = Table(dataSource, "user")
        val changes = Changes(table)
        changes.setStartPointNow()

        mockMvc.post("/api/users") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"name": "siosio"}"""
        }.andExpect {
            status { isOk() }
        }

        changes.setEndPointNow()
        Assertions.assertThat(changes)
            .hasNumberOfChanges(1)
            .changeOfCreation()
            .rowAtEndPoint().value("name").isEqualTo("siosio")
    }

    "ユーザ一覧が取得できるよ" {
        mockMvc.get("/api/users")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.users.length()") { value(3) }
                jsonPath("$.users[*].name") { value(contains("user_1", "user_2", "user_3")) }
                jsonPath("$.users[*].id") { value(contains(1, 2, 3)) }
            }
    }
}) {
    companion object {
        @Suppress("unused")
        val container = MySQLContainer<Nothing>("mysql:8.0.28").run {
            start()
            waitingFor(HostPortWaitStrategy())

            System.setProperty("spring.datasource.url", jdbcUrl)
            System.setProperty("spring.datasource.username", username)
            System.setProperty("spring.datasource.password", password)
        }
    }
}