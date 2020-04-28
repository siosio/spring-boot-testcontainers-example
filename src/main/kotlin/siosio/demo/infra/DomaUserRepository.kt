package siosio.demo.infra

import siosio.demo.domain.User
import siosio.demo.domain.UserRepository

class DomaUserRepository(private val userDao: UserDao) : UserRepository {
    override fun findAll(): List<User> {
        return userDao.findAll()
                .map { User(it.id, it.name) }
    }

    override fun addUser(user: User) {
        userDao.insert(UserEntity(-1, user.name))
    }
}