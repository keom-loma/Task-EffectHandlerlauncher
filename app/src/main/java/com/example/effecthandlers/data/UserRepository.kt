package com.example.effecthandlers.data

import com.example.effecthandlers.api.UserService
import com.example.effecthandlers.model.UserModelItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {
	suspend fun getUsers(): Flow<List<UserModelItem>> {
		return  flow {
        val res = userService.getUsers()
		emit(res)
		}
	}
}