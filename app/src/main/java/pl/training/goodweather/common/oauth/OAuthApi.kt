package pl.training.goodweather.common.oauth

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface OAuthApi {

    @FormUrlEncoded
    @POST("auth/realms/cloud/protocol/openid-connect/token")
    suspend fun authenticate(@Field("client_id") clientId: String,
                             @Field("grant_type") grantType: String,
                             @Field("username") username: String,
                             @Field("password") password: String): AccessToken

    @FormUrlEncoded
    @POST("auth/realms/cloud/protocol/openid-connect/token")
    suspend fun refreshToken(@Field("client_id") clientId: String,
                             @Field("grant_type") grantType: String,
                             @Field("refresh_token") refreshToken: String): AccessToken

    @GET("https://sages.pl")
    suspend fun testRequest()

}