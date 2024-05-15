package com.khush.gitsearch.di

import android.app.Application
import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.khush.gitsearch.common.dispatcher.DefaultDispatcherProvider
import com.khush.gitsearch.common.dispatcher.DispatcherProvider
import com.khush.gitsearch.common.networkhelper.NetworkHelper
import com.khush.gitsearch.common.networkhelper.NetworkHelperImpl
import com.khush.gitsearch.data.database.AppDatabaseService
import com.khush.gitsearch.data.database.DatabaseService
import com.khush.gitsearch.data.database.MainDatabase
import com.khush.gitsearch.data.model.MainData
import com.khush.gitsearch.data.network.ApiInterface
import com.khush.gitsearch.ui.paging.MainPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = "https://api.github.com/search/"

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideNetworkService(
        @BaseUrl baseUrl: String,
        gsonFactory: GsonConverterFactory,
    ): ApiInterface {

        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(gsonFactory)
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideDispatcher(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideNetworkHelper(
        @ApplicationContext context: Context
    ): NetworkHelper {
        return NetworkHelperImpl(context)
    }

    @DbName
    @Provides
    fun provideDbName(): String = "main_db"

    @Singleton
    @Provides
    fun provideDatabase(
        application: Application,
        @DbName dbName: String
    ): MainDatabase {
        return Room.databaseBuilder(
            application,
            MainDatabase::class.java,
            dbName
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabaseService(mainDatabase: MainDatabase): DatabaseService {
        return AppDatabaseService(mainDatabase)
    }
}