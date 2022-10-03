package com.things.tothemovies.data.remote

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val interceptedRequest = chain.createAuthenticatedRequest()

        val newRequest = request.newBuilder().url(interceptedRequest).build()

        return chain.proceed(newRequest)
    }

    private fun Interceptor.Chain.createAuthenticatedRequest(): HttpUrl {
        return request()
            .url
            .newBuilder()
            .addQueryParameter(ApiParameters.API_KEY,ApiParameters.KEY)
            .build()
    }
}