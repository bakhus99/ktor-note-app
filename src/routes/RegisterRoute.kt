package com.bakhus.note.routes

import com.bakhus.note.data.checkIfUsersExist
import com.bakhus.note.data.collections.User
import com.bakhus.note.data.registerUser
import com.bakhus.note.data.requests.AccountRequest
import com.bakhus.note.data.responses.SimplerResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.registerRoute() {

    route("/register") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }
            val userExists = checkIfUsersExist(request.email)
            if (!userExists) {
                if (registerUser(User(request.email, request.password))) {
                    call.respond(OK,SimplerResponse(true,"Successfully created account"))

                }else{
                    call.respond(OK,SimplerResponse(false,"An unknown error occured"))
                }
            }else{
                call.respond(OK,SimplerResponse(false,"A user with that email already exists"))
            }
        }
    }

}