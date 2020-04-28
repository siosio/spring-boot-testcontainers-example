package siosio.demo.infra

import org.seasar.doma.Dao
import org.seasar.doma.Insert
import org.seasar.doma.Select
import org.seasar.doma.boot.ConfigAutowireable
import org.seasar.doma.experimental.Sql
import org.seasar.doma.jdbc.Result

@Dao
@ConfigAutowireable
interface UserDao {

    @Sql("select /*%expand*/* from user")
    @Select
    fun findAll(): List<UserEntity>

    @Insert
    fun insert(userEntity: UserEntity): Result<UserEntity>
}