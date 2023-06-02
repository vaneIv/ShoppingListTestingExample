package com.example.shoppinglisttestingexample.repositories

import androidx.lifecycle.LiveData
import com.example.shoppinglisttestingexample.data.local.ShoppingDao
import com.example.shoppinglisttestingexample.data.local.ShoppingItem
import com.example.shoppinglisttestingexample.data.remote.UnsplashApi
import com.example.shoppinglisttestingexample.data.remote.responses.UnsplashResponse
import com.example.shoppinglisttestingexample.other.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val unsplashApi: UnsplashApi
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<UnsplashResponse> {
        return try {
            val response = unsplashApi.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach for the server. Check your internet connection", null)
        }
    }
}