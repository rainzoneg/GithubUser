package com.dicoding.githubuser.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.ui.detail.UserDetailActivity
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.databinding.ItemRowFavoriteBinding
import com.dicoding.githubuser.helper.FavoriteDiffCallback

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val listFavorites = ArrayList<Favorite>()

    fun setListFavorites(listFavorites : List<Favorite>){
        val diffCallback = FavoriteDiffCallback(this.listFavorites, listFavorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorites.clear()
        this.listFavorites.addAll(listFavorites)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemRowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            holder.binding.cardViewFav.setCardBackgroundColor(0x292929)
        }
        holder.bind(listFavorites[position])
    }

    override fun getItemCount(): Int = listFavorites.size

    inner class FavoriteViewHolder(internal val binding : ItemRowFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            with(binding) {
                tvUserUsernameFav.text = favorite.login
                Glide.with(itemView.context).load(favorite.avatar_url).circleCrop().into(imgUserPhotoFav)
                cardViewFav.setOnClickListener {
                    val intent = Intent(it.context, UserDetailActivity::class.java)
                    intent.putExtra(UserDetailActivity.EXTRA_USER, favorite.login)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}