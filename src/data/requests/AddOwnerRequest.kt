package com.bakhus.note.data.requests

data class AddOwnerRequest(
    val noteID: String,
    val owner: String
)
