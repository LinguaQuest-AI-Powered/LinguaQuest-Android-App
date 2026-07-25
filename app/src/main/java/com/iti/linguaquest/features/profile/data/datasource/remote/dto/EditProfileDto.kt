package com.iti.linguaquest.features.profile.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName




data class UpdateProfileRequestDto(
    @SerializedName("username")
    val username: String
)

data class UpdatePasswordRequestDto(
    @SerializedName("oldPassword")
    val oldPassword: String,

    @SerializedName("newPassword")
    val newPassword: String
)



data class ProfileDto(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("username")
    val username: String?
)

data class PhotoResponseDto(
    @SerializedName("photoUrl")
    val photoUrl: String
)

data class PasswordStatusDto(
    @SerializedName("status")
    val status: String
)



data class ErrorDto(
    @SerializedName("apiPath")
    val apiPath: String,

    @SerializedName("errorCode")
    val errorCode: Int,

    @SerializedName("errorKey")
    val errorKey: String,

    @SerializedName("errorMessage")
    val errorMessage: String,

    @SerializedName("errorTime")
    val errorTime: String
)