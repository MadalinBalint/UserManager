package com.mendelin.usermanager.networking

import com.google.gson.GsonBuilder
import com.mendelin.usermanager.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

object UserManagerServiceProvider {

    private const val TRY_COUNT = 3
    private const val TRY_PAUSE_BETWEEN = 1000L

    private fun okHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("Accept", "application/json")
                    builder.header("Content-Type", "application/json")
                    builder.header("Authorization", "${BuildConfig.API_BEARER} ${BuildConfig.REST_API_ACCESS_TOKEN}")

                    var response = chain.proceed(builder.build())

                    /* Automatically retry the call for N times if you receive a server error (5xx) */
                    var tryCount = 0
                    while (response.code() in 500..599 && tryCount < TRY_COUNT) {
                        try {
                            response.close()
                            Thread.sleep(TRY_PAUSE_BETWEEN)
                            response = chain.proceed(builder.build())
                            Timber.e("Request is not successful - $tryCount")
                        } catch (e: Exception) {
                            Timber.e(e.localizedMessage)
                        } finally {
                            tryCount++
                        }
                    }

                    /* Intercept empty body response */
                    if (response.body()?.contentLength() == 0L) {
                        val contentType: MediaType? = response.body()!!.contentType()
                        val body: ResponseBody = ResponseBody.create(contentType, "{}")
                        return@Interceptor response.newBuilder().body(body).build()
                    }

                    return@Interceptor response
                }
            )
        }.build()


    private fun retrofitBuilderInstance(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    fun userApiService(): UserApiService =
        retrofitBuilderInstance().create(UserApiService::class.java)
}