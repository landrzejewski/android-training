package pl.training.runkeeper.common

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class AppIdInterceptor : Interceptor {

    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        if (request.url.host != HOST) {
            return chain.proceed(request)
        }
        val modifiedUrl = request.url.newBuilder()
            .addQueryParameter(PARAM_NAME, APP_ID)
            .build()
        val modifiedRequest = request.newBuilder()
            .url(modifiedUrl)
            .build()
        return chain.proceed(modifiedRequest)
    }

    companion object {

        const val HOST = "api.openweathermap.org"
        const val PARAM_NAME = "appid"
        const val APP_ID = "b933866e6489f58987b2898c89f542b8"

    }

}