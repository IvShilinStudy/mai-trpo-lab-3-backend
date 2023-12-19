package ru.ivn_sln.data.response

import kotlinx.serialization.Serializable

@Serializable
data class RegUser(
    val firstName: String,
    val lastName: String,
    val phone: String,
)