package com.bakhus.note.routes

import com.bakhus.note.data.checkPasswordForEmail
import com.bakhus.note.data.requests.AccountRequest
import com.bakhus.note.data.responses.SimplerResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.loginRoute() {

    route("/login") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val isPasswordCorrect = checkPasswordForEmail(request.email,request.password)
            if (isPasswordCorrect){
                call.respond(HttpStatusCode.OK,SimplerResponse(true,"You logged in!! "))
            } else{
                call.respond(HttpStatusCode.OK,SimplerResponse(false,"The email or password is incorrect"))
            }
        }
    }

}
