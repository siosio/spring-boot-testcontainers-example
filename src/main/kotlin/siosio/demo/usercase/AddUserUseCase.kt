package siosio.demo.usercase

import siosio.demo.domain.User
import siosio.demo.domain.UserRepository

class AddUserUseCase(private val userRepository: UserRepository) {

    fun addUser(username: String) {
        userRepository.addUser(User(-1, username))
    }
}