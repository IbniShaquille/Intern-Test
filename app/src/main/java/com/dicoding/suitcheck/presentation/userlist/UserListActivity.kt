package com.dicoding.suitcheck.presentation.userlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dicoding.suitcheck.R
import com.dicoding.suitcheck.databinding.ActivityUserListBinding

class UserListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserListBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.userList)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupBackAction()
        setupSwipeRefresh()
        setupRecyclerView()
        observeUserList()
    }

    private fun observeUserList() {
        userViewModel.apply {
            users.observe(this@UserListActivity, Observer { users ->
                userAdapter.submitList(users)
            })

            isLoading.observe(this@UserListActivity, Observer { isLoading ->
                swipeRefreshLayout.isRefreshing = isLoading
            })
//
//            error.observe(this@UserListActivity, Observer { errorMessage ->
//                Toast.makeText(this@UserListActivity, errorMessage, Toast.LENGTH_SHORT).show()
//                Log.d("UserListActivity", errorMessage)
//            })
        }
    }

    private fun setupRecyclerView() {
        val userRecyclerView = binding.userRecyclerView
        userAdapter = UserAdapter(mutableListOf()).apply {
            onItemClick = { user ->
                val resultIntent = Intent().apply {
                    putExtra("SELECTED_USER", "${user.firstName} ${user.lastName}")
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
        userRecyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@UserListActivity)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        userViewModel.loadMoreUsers()
                    }
                }
            })
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            userViewModel.refreshUsers()
        }
    }

    private fun setupBackAction() {
        binding.toolbar.setOnClickListener {
            onBackPressed()
        }
    }
}