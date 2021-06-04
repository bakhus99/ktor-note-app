package com.bakhus.note.routes

import com.bakhus.note.data.*
import com.bakhus.note.data.collections.Note
import com.bakhus.note.data.requests.AddOwnerRequest
import com.bakhus.note.data.requests.DeleteNoteRequest
import com.bakhus.note.data.responses.SimplerResponse
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.noteRoutes() {

    route("/getNotes") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val notes = getNotesForUser(email)
                call.respond(OK, notes)
            }
        }
    }

    route("/deleteNote") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<DeleteNoteRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }

                if (deleteNoteForUser(email, request.id)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/addNote") {
        authenticate {
            post {
                val note = try {
                    call.receive<Note>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (saveNote(note)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/addOwnerToNote") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddOwnerRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (!checkIfUsersExist(request.owner)) {
                    call.respond(
                        OK,
                        SimplerResponse(false, "No user with this E-Mail exists")
                    )
                    return@post
                }
                if (isOwnerOfNote(request.noteID, request.owner)) {
                    call.respond(
                        OK,
                        SimplerResponse(false, "This user is already an owner of this note")
                    )
                    return@post
                }
                if (addOwnerToNote(request.noteID, request.owner)) {
                    call.respond(
                        OK,
                        SimplerResponse(true, "${request.owner} can now see this note")
                    )
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }
}