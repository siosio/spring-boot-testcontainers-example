package siosio.demo.infra

import org.seasar.doma.jdbc.criteria.Entityql
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel
import siosio.demo.domain.User
import siosio.demo.domain.UserRepository

class DomaUserRepository(private val entityql: Entityql) : UserRepository {
    override fun findAll(): List<User> {
        return entityql.from(entityType<UserEntity>())
                .fetch()
                .map { User(it.id, it.name) }
    }

    override fun addUser(user: User) {
        entityql.insert(entityType<UserEntity>(), UserEntity(-1, user.name))
                .execute()
    }
}
private inline fun <reified ENTITY> entityType(): EntityMetamodel<ENTITY> {
    return Class.forName("${ENTITY::class.qualifiedName}_")
            .getConstructor()
            .newInstance() as EntityMetamodel<ENTITY>
}
