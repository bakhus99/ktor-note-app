package com.bakhus.note

import com.bakhus.note.data.checkPasswordForEmail
import com.bakhus.note.data.collections.User
import com.bakhus.note.data.registerUser
import com.bakhus.note.routes.loginRoute
import com.bakhus.note.routes.registerRoute
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        registerRoute()
        loginRoute()
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication) {
        configureAuth()
    }
}


private fun Authentication.Configuration.configureAuth() {
    basic {
        realm = "Ktor Note Server"
        validate { credentials ->
            val email = credentials.name
            val password = credentials.name
            if (checkPasswordForEmail(email, password)) {
                UserIdPrincipal(email)
            } else null
        }
    }
}

