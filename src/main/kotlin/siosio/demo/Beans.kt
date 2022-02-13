package siosio.demo

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.web.servlet.function.router
import siosio.demo.handler.UserHandler
import siosio.demo.infra.DomaUserRepository
import siosio.demo.usercase.AddUserUseCase
import siosio.demo.usercase.FindUsersUseCase

val beans: (GenericApplicationContext) -> BeanDefinitionDsl = {
    if (it.environment.activeProfiles.contains("test")) {
        beans { }
    } else {
        beans {
            bean<DomaUserRepository>()
            bean<FindUsersUseCase>()
            bean<AddUserUseCase>()
            bean<UserHandler>()

            bean(::myRouter)
        }
    }
}

private fun myRouter(
    userHandler: UserHandler
) = router {
    "/api".nest {
        POST("/users", userHandler::addUser)
        GET("/users", userHandler::findAll)
    }
}

