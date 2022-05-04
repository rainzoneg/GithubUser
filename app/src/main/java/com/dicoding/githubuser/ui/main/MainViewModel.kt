package com.dicoding.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.githubuser.ApiConfig
import com.dicoding.githubuser.GithubUserItem
import com.dicoding.githubuser.SearchResponse
import com.dicoding.githubuser.helper.SettingsPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref : SettingsPreferences) : ViewModel() {

    fun getThemeSettings() : LiveData<Boolean>{
        return pref.getThemeSetting().asLiveData()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init{
        findUsers()
    }

    private val _listUsers = MutableLiveData<List<GithubUserItem>>()
    val listUsers : LiveData<List<GithubUserItem>> = _listUsers


    fun findUsers(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsersList()
        client.enqueue(object: Callback<List<GithubUserItem>>{
            override fun onResponse(
                call: Call<List<GithubUserItem>>,
                response: Response<List<GithubUserItem>>
            ){
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _listUsers.value = responseBody!!
                    }
                } else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<GithubUserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findSearchUsers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsersSearch(username)
        client.enqueue(object: Callback<SearchResponse>{
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ){
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _listUsers.value = responseBody.items
                    }
                } else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}