package com.param.kohinoor.application

import com.param.kohinoor.ApiInterface
//import com.param.kohinoor.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = Interceptor { chain: Interceptor.Chain ->
            val original: Request = chain.request()
            val request: Request = original.newBuilder()
                .header(
                    "Authorization",
                    Credentials.basic(
                        "ck_3d47dd9e06028440284ac2a8d35c81b365efc48c",
                        "cs_438a1c1c5d5dff06800790f43d77f083c26d85f3"
                    )
                )
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        /*if (BuildConfig.DEBUG) {*/
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
      return      OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
                .build()
        /*} else OkHttpClient
            .Builder().addInterceptor(interceptor)
            .build()*/
    }

    @Provides
    @Singleton

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://kohinoormunich.de/wp-json/wc/v3/")
            .client(okHttpClient)
            .build()
    }
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiInterface::class.java)
}