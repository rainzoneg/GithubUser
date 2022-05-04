package com.dicoding.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.GithubUserItem
import com.dicoding.githubuser.R
import com.dicoding.githubuser.helper.ViewModelFactory
import com.dicoding.githubuser.ui.main.ListUserAdapter

class FollowerFragment : Fragment() {

    private val followersList = ArrayList<GithubUserItem>()
    private val followingsList = ArrayList<GithubUserItem>()
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME) as String
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val progressBar : ProgressBar = view.findViewById(R.id.fragmentProgressBar)
        val rvFollowers : RecyclerView = view.findViewById(R.id.rv_followers)
        rvFollowers.layoutManager = LinearLayoutManager(requireActivity())
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            val frameLayout : FrameLayout = view.findViewById(R.id.detailFragmentLayout)
            frameLayout.setBackgroundColor(0x0d0d0d)
        }
        detailViewModel= obtainViewModel(requireActivity() as AppCompatActivity)
        detailViewModel.isLoading.observe(requireActivity()) {
            showLoading(it, progressBar)
        }
        when(index){
            1 -> {
                detailViewModel.findFollowers(username)
                detailViewModel.listFollowers.observe(requireActivity()){ followers ->
                    followersList.addAll(followers)
                    val listUserAdapter = ListUserAdapter(followersList)
                    rvFollowers.adapter = listUserAdapter
                    listUserAdapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback{
                        override fun onItemClicked(data: GithubUserItem) {
                            showClickedUser(data)
                        }
                    })
                }
            }
            else -> {
                detailViewModel.findFollowings(username)
                detailViewModel.listFollowings.observe(requireActivity()){ followings ->
                    followingsList.addAll(followings)
                    val listUserAdapter = ListUserAdapter(followingsList)
                    rvFollowers.adapter = listUserAdapter
                    listUserAdapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback{
                        override fun onItemClicked(data: GithubUserItem) {
                            showClickedUser(data)
                        }
                    })
                }
            }
        }
    }

    private fun showClickedUser(user: GithubUserItem){
        val moveIntent = Intent(requireContext(), UserDetailActivity::class.java)
        moveIntent.putExtra(UserDetailActivity.EXTRA_USER, user.login)
        startActivity(moveIntent)
    }

    private fun showLoading(isLoading: Boolean, progressBar: ProgressBar) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainViewModel(activity : AppCompatActivity) : DetailViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    companion object{
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }

}