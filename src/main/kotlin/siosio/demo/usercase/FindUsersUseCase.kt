package siosio.demo.usercase

import org.springframework.transaction.annotation.Transactional
import siosio.demo.domain.UserRepository

@Transactional(readOnly = true)
class FindUsersUseCase(private val userRepository: UserRepository) {
    fun findAllUser(): List<UserDto> {
        return userRepository.findAll()
                .map { UserDto(it.id, it.name) }
    }
}

data class UserDto(val id: Int, val name: String)
