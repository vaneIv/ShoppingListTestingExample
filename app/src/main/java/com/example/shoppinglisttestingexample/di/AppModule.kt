package com.example.shoppinglisttestingexample.di

import android.content.Context
import androidx.room.Room
import com.example.shoppinglisttestingexample.data.local.ShoppingDao
import com.example.shoppinglisttestingexample.data.local.ShoppingItemDatabase
import com.example.shoppinglisttestingexample.data.remote.UnsplashApi
import com.example.shoppinglisttestingexample.other.Constants.BASE_URL
import com.example.shoppinglisttestingexample.other.Constants.DATABASE_NAME
import com.example.shoppinglisttestingexample.repositories.DefaultShoppingRepository
import com.example.shoppinglisttestingexample.repositories.ShoppingRepository
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
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDefaultRepository(
        dao: ShoppingDao,
        api: UnsplashApi
    ) = DefaultShoppingRepository(dao, api) as ShoppingRepository

    @Singleton
    @Provides
    fun provideShoppingDao(
        database: ShoppingItemDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun provideUnsplashApi(): UnsplashApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(UnsplashApi::class.java)
    }
}