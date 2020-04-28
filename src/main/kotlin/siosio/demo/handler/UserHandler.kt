package siosio.demo.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import siosio.demo.usercase.AddUserUseCase
import siosio.demo.usercase.FindUsersUseCase

class UserHandler(
        private val findUsersUseCase: FindUsersUseCase,
        private val addUserUseCase: AddUserUseCase
) {
    fun findAll(serverRequest: ServerRequest): ServerResponse {
        val users = findUsersUseCase.findAllUser()
                .map { (id, name) -> UserResponse(id, name) }
        return ServerResponse.ok()
                .body(FindAllUserResponse(users))
    }

    fun addUser(serverRequest: ServerRequest) : ServerResponse {
        val addUserRequest = serverRequest.body(AddUserRequest::class.java)
        addUserUseCase.addUser(addUserRequest.name)
        return ServerResponse.ok().build()
    }
}

data class AddUserRequest(val name: String)
data class FindAllUserResponse(val users: List<UserResponse>)
data class UserResponse(val id: Int, val name: String)
