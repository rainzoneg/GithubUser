package com.dicoding.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.ApiConfig
import com.dicoding.githubuser.GithubUserDetailItem
import com.dicoding.githubuser.GithubUserItem
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application : Application) : ViewModel() {

    private val mFavoriteRepository : FavoriteRepository = FavoriteRepository(application)
    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }
    fun deleteByUsername(login: String) {
        mFavoriteRepository.deleteByUsername(login)
    }
    fun getFavByUsername(login: String) : LiveData<Boolean> = mFavoriteRepository.getFavByUsername(login)

    private val _userDetail = MutableLiveData<GithubUserDetailItem>()
    val userDetail : LiveData<GithubUserDetailItem> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _listFollowers = MutableLiveData<List<GithubUserItem>>()
    val listFollowers : LiveData<List<GithubUserItem>> = _listFollowers

    private val _listFollowings = MutableLiveData<List<GithubUserItem>>()
    val listFollowings : LiveData<List<GithubUserItem>> = _listFollowings

    fun findUserDetail(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object: Callback<GithubUserDetailItem> {
            override fun onResponse(
                call: Call<GithubUserDetailItem>,
                response: Response<GithubUserDetailItem>
            ){
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _userDetail.value = responseBody!!
                    }
                } else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubUserDetailItem>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findFollowers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object: Callback<List<GithubUserItem>>{
            override fun onResponse(
                call: Call<List<GithubUserItem>>,
                response: Response<List<GithubUserItem>>
            ){
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _listFollowers.value = responseBody!!
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

    fun findFollowings(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowings(username)
        client.enqueue(object: Callback<List<GithubUserItem>>{
            override fun onResponse(
                call: Call<List<GithubUserItem>>,
                response: Response<List<GithubUserItem>>
            ){
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _listFollowings.value = responseBody!!
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

    companion object{
        private const val TAG = "DetailViewModel"
    }
}