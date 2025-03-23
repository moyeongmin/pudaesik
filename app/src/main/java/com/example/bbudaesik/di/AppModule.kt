package com.example.bbudaesik.di

import com.example.bbudaesik.BuildConfig
import com.example.bbudaesik.data.remote.NotionApiService
import com.example.bbudaesik.data.repository.NotionRepositoryImpl
import com.example.bbudaesik.domain.repository.NotionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotionApi(): NotionApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotionApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotionRepository(api: NotionApiService): NotionRepository {
        return NotionRepositoryImpl(
            api,
            BuildConfig.API_KEY,
            databaseIds = mapOf(
                "RESTAURANT" to BuildConfig.Restaurant_Db_Id,
                "DORMITORY" to BuildConfig.Dormitory_Db_Id,
            ),
        )
    }
}

object WidgetHelper {
    fun provideNotionRepository(): NotionRepository {
        val api = Retrofit.Builder()
            .baseUrl(BuildConfig.BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotionApiService::class.java)

        return NotionRepositoryImpl(
            api,
            BuildConfig.API_KEY,
            mapOf(
                "RESTAURANT" to BuildConfig.Restaurant_Db_Id,
                "DORMITORY" to BuildConfig.Dormitory_Db_Id,
            )
        )
    }
}