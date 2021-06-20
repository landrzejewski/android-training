package pl.training.goodweather.common.oauth

import com.google.gson.annotations.SerializedName

class AccessToken(@SerializedName("access_token") val accessToken: String,
                  @SerializedName("refresh_token") val refreshToken: String,
                  @SerializedName("expires_in") val tokenExpiresIn: Long,
                  @SerializedName("refresh_expires_in") val refreshTokenExpiresIn: Long)