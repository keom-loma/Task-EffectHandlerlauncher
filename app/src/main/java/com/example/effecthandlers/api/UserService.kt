package com.example.effecthandlers.api

import com.example.effecthandlers.model.UserModelItem
import retrofit2.http.GET

interface UserService {
  @GET("/posts")
  suspend fun getUsers(): List<UserModelItem>
}