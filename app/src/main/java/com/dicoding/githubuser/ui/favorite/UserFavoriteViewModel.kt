package com.dicoding.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.repository.FavoriteRepository

class UserFavoriteViewModel(application : Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun getAllFav(): LiveData<List<Favorite>> = mFavoriteRepository.getAllFav()
}