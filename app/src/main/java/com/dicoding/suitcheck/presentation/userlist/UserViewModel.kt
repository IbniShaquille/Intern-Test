package com.dicoding.suitcheck.presentation.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.suitcheck.data.api.ApiConfig
import com.dicoding.suitcheck.data.model.Data
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _users = MutableLiveData<List<Data>>()
    val users: LiveData<List<Data>> get() = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var currentPage = 1
    private var totalPage = 1
    private val usersList = mutableListOf<Data>()

    init {
        fetchUsers(currentPage)
    }

    fun fetchUsers(page: Int) {
        if (page > totalPage) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getUsers(page, 10)
                usersList.addAll(response.data)
                _users.value = usersList
                currentPage = response.page
                totalPage = response.totalPages
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message
            }
        }
    }

    fun refreshUsers() {
        currentPage = 1
        usersList.clear()
        fetchUsers(currentPage)
    }

    fun loadMoreUsers() {
        if (currentPage < totalPage) {
            fetchUsers(currentPage + 1)
        }
    }
}
