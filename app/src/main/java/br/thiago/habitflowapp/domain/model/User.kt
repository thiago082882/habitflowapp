package br.thiago.habitflowapp.domain.model

import com.google.gson.Gson

data class User(
    var id: String = "",
    var email: String = "",
    var password: String = "",
) {
    fun toJson(): String = Gson().toJson(User(
        id,
        email,
        password,

    ))

    companion object {
        fun fromJson(data: String): User = Gson().fromJson(data, User::class.java)
    }

}