package com.example.effecthandlers.di


import com.example.effecthandlers.api.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRetrofit {
	@Provides
	@Singleton
	fun providerUserRetrofit(): UserService {
	return Retrofit.Builder()
		.baseUrl("https://jsonplaceholder.typicode.com/")
		.addConverterFactory(GsonConverterFactory.create())
		.build()
		.create(UserService::class.java)
	}
}