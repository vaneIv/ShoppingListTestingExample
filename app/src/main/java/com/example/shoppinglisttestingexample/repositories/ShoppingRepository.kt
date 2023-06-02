package com.example.shoppinglisttestingexample.repositories

import androidx.lifecycle.LiveData
import com.example.shoppinglisttestingexample.data.local.ShoppingItem
import com.example.shoppinglisttestingexample.data.remote.responses.UnsplashResponse
import com.example.shoppinglisttestingexample.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<UnsplashResponse>
}