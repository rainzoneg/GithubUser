package com.dicoding.githubuser.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityUserFavoriteBinding
import com.dicoding.githubuser.helper.ViewModelFactory
import com.dicoding.githubuser.ui.settings.SettingsActivity

class UserFavoriteActivity : AppCompatActivity() {

    private var _activityUserFavoriteBinding : ActivityUserFavoriteBinding? = null
    private val binding get() = _activityUserFavoriteBinding
    private lateinit var adapter : FavoriteAdapter
    private lateinit var userFavoriteViewModel : UserFavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityUserFavoriteBinding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.title = "Favorite Users"

        userFavoriteViewModel = obtainViewModel(this@UserFavoriteActivity)
        userFavoriteViewModel.getAllFav().observe(this){ favList ->
            if(favList != null){
                adapter.setListFavorites(favList)
            }
        }
        adapter = FavoriteAdapter()
        binding?.rvUserFavs?.layoutManager = LinearLayoutManager(this)
        binding?.rvUserFavs?.setHasFixedSize(true)
        binding?.rvUserFavs?.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        menu.findItem(R.id.search).isVisible = false
        menu.findItem(R.id.favorite).isVisible = false
        return true
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserFavoriteViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserFavoriteViewModel::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings -> {
                val settingIntent = Intent(this@UserFavoriteActivity, SettingsActivity::class.java)
                startActivity(settingIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityUserFavoriteBinding = null
    }
}