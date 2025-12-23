package com.cerebra.app.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface PoetryDbApi {
    @GET("random/{count}")
    suspend fun getRandomPoems(@Path("count") count: Int): List<PoetryDbItem>

    @GET("title/{title}")
    suspend fun getPoemByTitle(@Path("title") title: String): List<PoetryDbItem>

    @GET("author/{author}")
    suspend fun getPoemsByAuthor(@Path("author") author: String): List<PoetryDbItem>
    
    @GET("title/{term}")
    suspend fun searchPoems(@Path("term") term: String): List<PoetryDbItem>
}
