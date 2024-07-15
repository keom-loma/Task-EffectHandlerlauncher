package com.example.effecthandlers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effecthandlers.data.UserRepository
import com.example.effecthandlers.model.UserModelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
	private val _userPost = MutableStateFlow<List<UserModelItem>>(emptyList())
	val resData: StateFlow<List<UserModelItem>> = _userPost




	 fun getUserPost() {
		viewModelScope.launch {
			runCatching {
				withContext(Dispatchers.IO) {
					userRepository.getUsers().collect {
						withContext(Dispatchers.Main) {
							_userPost.value = it
						}
					}

				}
			}.onSuccess {
				Log.i("onSuccess", "getUserPost: onSuccess: $it")
			}
				.onFailure {
					Log.i("onFailure", "getUserPost: onFailure: $it")
				}
		}

	}
}

