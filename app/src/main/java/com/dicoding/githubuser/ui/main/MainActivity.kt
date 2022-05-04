package com.dicoding.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.GithubUserItem
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.ui.detail.UserDetailActivity
import com.dicoding.githubuser.ui.favorite.UserFavoriteActivity
import com.dicoding.githubuser.ui.settings.SettingsActivity
import com.dicoding.githubuser.helper.SettingsPreferences
import com.dicoding.githubuser.helper.ViewModelPrefFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding : ActivityMainBinding
    private val list = ArrayList<GithubUserItem>()
    private lateinit var mainViewModel : MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.rvUsers.setHasFixedSize(true)
        activityMainBinding.rvUsers.layoutManager = LinearLayoutManager(this)

        val pref = SettingsPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, ViewModelPrefFactory.getInstance(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.getThemeSettings().observe(this){ isDarkModeActive : Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        mainViewModel.listUsers.observe(this){ githubUsers ->
            list.clear()
            list.addAll(githubUsers)
            showRecyclerList()
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView= menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.findSearchUsers(query as String)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.equals("")){
                    mainViewModel.findUsers()
                }
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> {
                val favIntent = Intent(this@MainActivity, UserFavoriteActivity::class.java)
                startActivity(favIntent)
            }
            R.id.settings -> {
                val settingIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(settingIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerList(){
        val listUserAdapter = ListUserAdapter(list)
        activityMainBinding.rvUsers.adapter = listUserAdapter
        listUserAdapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUserItem) {
                showClickedUser(data)
            }
        })
    }

    private fun showClickedUser(user: GithubUserItem){
        val detailIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
        detailIntent.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        startActivity(detailIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        activityMainBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}