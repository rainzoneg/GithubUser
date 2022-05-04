package com.dicoding.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.database.FavoriteDao
import com.dicoding.githubuser.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application : Application) {
    private val mFavoritesDao : FavoriteDao
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()
    init{
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }
    fun insert(favorite: Favorite) {
        executorService.execute { mFavoritesDao.insert(favorite) }
    }
    fun update(favorite: Favorite) {
        executorService.execute { mFavoritesDao.update(favorite) }
    }
    fun delete(favorite: Favorite) {
        executorService.execute { mFavoritesDao.delete(favorite) }
    }
    fun deleteByUsername(login: String){
        executorService.execute { mFavoritesDao.deleteByUsername(login)}
    }
    fun getAllFav(): LiveData<List<Favorite>> = mFavoritesDao.getAllFav()
    fun getFavByUsername(login : String): LiveData<Boolean> = mFavoritesDao.getFavByUsername(login)
}