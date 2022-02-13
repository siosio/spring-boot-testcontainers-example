package siosio.demo.usercase

import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.clear
import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.MockK
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import siosio.demo.domain.User
import siosio.demo.domain.UserRepository

@SpringBootTest(classes = [FindUsersUseCase::class])
@ActiveProfiles("test")
class FindUsersUseCaseTest(
    private val sut: FindUsersUseCase,
    @MockkBean val mockUserRepository: UserRepository
) : FreeSpec({

    "ユーザ一覧が取得できるよ" {
        every { mockUserRepository.findAll() } returns listOf(User(1, "name_1"), User(2, "name_2"), User(3, "name_3"))

        sut.findAllUser() shouldBe listOf(UserDto(1, "name_1"), UserDto(2, "name_2"), UserDto(3, "name_3"))

        verify { mockUserRepository.findAll() }
    }
})
