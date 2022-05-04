package com.dicoding.githubuser.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuser.ui.main.MainViewModel
import com.dicoding.githubuser.ui.settings.SettingsViewModel
import java.lang.IllegalArgumentException

class ViewModelPrefFactory private constructor(private val pref: SettingsPreferences) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            return SettingsViewModel(pref) as T
        }else if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var INSTANCE : ViewModelPrefFactory? = null
        @JvmStatic
        fun getInstance(pref: SettingsPreferences) : ViewModelPrefFactory{
            if(INSTANCE == null){
                synchronized(ViewModelPrefFactory::class.java){
                    INSTANCE = ViewModelPrefFactory(pref)
                }
            }
            return INSTANCE as ViewModelPrefFactory
        }
    }
}