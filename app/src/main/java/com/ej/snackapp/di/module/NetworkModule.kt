package com.ej.snackapp.di.module

import com.ej.snackapp.api.SnackApi
import com.ej.snackapp.info.ServerInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideSnackApi() : SnackApi{
        val retrofit = Retrofit.Builder()
            .baseUrl(ServerInfo.SERVER_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val snackApi = retrofit.create(SnackApi::class.java)
        return snackApi
    }
}