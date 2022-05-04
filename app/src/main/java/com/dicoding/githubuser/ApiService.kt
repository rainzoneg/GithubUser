package com.dicoding.githubuser

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    @Headers("Authorization: Bearer ${BuildConfig.TOKEN_API}")
    fun getUsersList(): Call<List<GithubUserItem>>

    @GET("search/users")
    @Headers("Authorization: Bearer ${BuildConfig.TOKEN_API}")
    fun getUsersSearch(
        @Query("q") username: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: Bearer ${BuildConfig.TOKEN_API}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<GithubUserDetailItem>

    @GET("users/{username}/followers")
    @Headers("Authorization: Bearer ${BuildConfig.TOKEN_API}")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<GithubUserItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: Bearer ${BuildConfig.TOKEN_API}")
    fun getUserFollowings(
        @Path("username") username: String
    ): Call<List<GithubUserItem>>
}