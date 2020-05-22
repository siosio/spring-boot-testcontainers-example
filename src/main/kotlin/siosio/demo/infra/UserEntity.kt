package siosio.demo.infra

import org.seasar.doma.Entity
import org.seasar.doma.GeneratedValue
import org.seasar.doma.GenerationType
import org.seasar.doma.Id
import org.seasar.doma.Metamodel
import org.seasar.doma.Table

@Entity(immutable = true, metamodel = Metamodel())
@Table(name = "user")
data class UserEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,

        val name: String
)
