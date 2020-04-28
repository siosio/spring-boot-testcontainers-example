package siosio.demo.domain

interface UserRepository {

    fun findAll(): List<User>

    fun addUser(user: User)
}