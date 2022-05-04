package com.dicoding.githubuser.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.helper.SettingsPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val pref : SettingsPreferences) : ViewModel() {
    fun getThemeSettings() : LiveData<Boolean>{
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSettings(isDarkModeActive : Boolean){
        viewModelScope.launch{
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}