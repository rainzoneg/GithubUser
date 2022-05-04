package com.dicoding.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)
    @Update
    fun update(favorite: Favorite)
    @Delete
    fun delete(favorite: Favorite)
    @Query("SELECT * FROM favorite ORDER BY id ASC")
    fun getAllFav() : LiveData<List<Favorite>>
    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE login = :login)")
    fun getFavByUsername(login : String) : LiveData<Boolean>
    @Query("DELETE FROM favorite WHERE login = :login")
    fun deleteByUsername(login: String)
}