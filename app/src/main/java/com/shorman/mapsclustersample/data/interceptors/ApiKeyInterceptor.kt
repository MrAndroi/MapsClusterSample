package com.shorman.mapsclustersample.data.interceptors

import com.shorman.mapsclustersample.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter(LANGUAGE_QUERY_PARAM, "en")
            .addQueryParameter(LIMIT_QUERY_PARAM, "-1")
            .build()

        val newRequest = originalRequest.newBuilder()
            .addHeader(CLIENT_ID_HEADER_NAME, BuildConfig.CLIENT_ID)
            .addHeader(CLIENT_SECRET_HEADER_NAME, BuildConfig.CLIENT_SECRET)
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val CLIENT_ID_HEADER_NAME = "client_id"
        private const val CLIENT_SECRET_HEADER_NAME = "client_secret"
        private const val LANGUAGE_QUERY_PARAM = "locale"
        private const val LIMIT_QUERY_PARAM = "limit"
    }
}