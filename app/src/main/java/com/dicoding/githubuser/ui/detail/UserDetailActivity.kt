package com.dicoding.githubuser.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.databinding.ActivityUserDetailBinding
import com.dicoding.githubuser.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var activityUserDetailBinding : ActivityUserDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUserDetailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(activityUserDetailBinding.root)
        supportActionBar?.hide()

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            activityUserDetailBinding.let{ binding ->
                binding.detailPageLayout.setBackgroundColor(0x292929)
                binding.tabs.setBackgroundColor(0x292929)
            }
        }

        detailViewModel= obtainViewModel(this@UserDetailActivity)
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val username = intent.getStringExtra(EXTRA_USER) as String
        var avatar_url : String? = null
        detailViewModel.findUserDetail(username)
        detailViewModel.userDetail.observe(this){ user ->
            avatar_url = user.avatarUrl
            Glide.with(this).load(avatar_url).circleCrop().into(activityUserDetailBinding.imgDetailPhoto)
            activityUserDetailBinding.tvDetailUsername.text = user.login
            activityUserDetailBinding.tvDetailName.text = user.name
            activityUserDetailBinding.tvDetailFollowing.text = user.following.toString().trim()
            activityUserDetailBinding.tvDetailFollower.text = user.followers.toString().trim()
            activityUserDetailBinding.tvDetailRepository.text = user.publicRepos.toString().trim()
            activityUserDetailBinding.tvDetailCompany.text = user.company
            activityUserDetailBinding.tvDetailLocation.text = user.location
        }

//        var isFavorited : Boolean
        detailViewModel.getFavByUsername(username).observe(this){ exists ->
            if(exists){
                activityUserDetailBinding.fabUnfavorite.visibility = View.VISIBLE
                activityUserDetailBinding.fabFavorite.visibility = View.GONE
            }else{
                activityUserDetailBinding.fabUnfavorite.visibility = View.GONE
                activityUserDetailBinding.fabFavorite.visibility = View.VISIBLE
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        activityUserDetailBinding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(activityUserDetailBinding.tabs, activityUserDetailBinding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        activityUserDetailBinding.fabUnfavorite.setOnClickListener { view ->
            if(view.id == R.id.fab_unfavorite){
                detailViewModel.deleteByUsername(username)
                Toast.makeText(this, "You unfavorited ${username}!", Toast.LENGTH_SHORT).show()
            }
        }
        activityUserDetailBinding.fabFavorite.setOnClickListener { view ->
            if(view.id == R.id.fab_favorite){
                val favorite = Favorite()
                favorite.login = username
                favorite.avatar_url = avatar_url
                detailViewModel.insert(favorite)
                Toast.makeText(this, "You added ${username} as a favorite!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        activityUserDetailBinding.detailProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity : AppCompatActivity) : DetailViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    companion object{
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}