package com.dicoding.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubuser.database.Favorite

class FavoriteDiffCallback(private val mOldFavList: List<Favorite>, private val mNewFavList: List<Favorite>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavList[oldItemPosition].id == mNewFavList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavList[oldItemPosition]
        val newEmployee = mNewFavList[newItemPosition]
        return oldEmployee.avatar_url == newEmployee.avatar_url && oldEmployee.login == newEmployee.login
    }
}