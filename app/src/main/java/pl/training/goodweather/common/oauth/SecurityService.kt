package pl.training.goodweather.common.oauth

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SecurityService {

    private var api = proxyBuilder(null)
    private var refreshToken = ""

    private fun proxyBuilder(token: AccessToken?): OAuthApi {
        val builder = Retrofit.Builder()
            .baseUrl("http://192.168.1.124:9100/")
            .addConverterFactory(GsonConverterFactory.create())
        if (token != null) {
            builder.client(httpClient(token))
        }
        return builder.build()
            .create(OAuthApi::class.java)
    }

    private fun httpClient(token: AccessToken): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient().newBuilder()
            .addInterceptor(Interceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.header("Authorization", "bearer ${token.accessToken}")
                return@Interceptor chain.proceed(requestBuilder.build())
            })
            .addInterceptor(loggingInterceptor)
            .build()
    }


    suspend fun authenticate(username: String, password: String): AccessToken {
        val token = api.authenticate("cloud-service", "password", username, password)
        refreshToken = token.refreshToken
        api = proxyBuilder(token)
        return token
    }

    suspend fun refreshToken(): AccessToken {
        val token = api.refreshToken("cloud-service", "refresh_token", refreshToken)
        api = proxyBuilder(token)
        refreshToken = token.refreshToken
        return token
    }

    suspend fun sendTestRequest() {
        api.testRequest()
    }
}
