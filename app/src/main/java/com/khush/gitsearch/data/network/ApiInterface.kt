package com.khush.gitsearch.data.network

import com.khush.gitsearch.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("repositories")
    suspend fun getGitResponse(
        @Query("q") searchQuery: String,
        @Query("page") pageNum: Int = 1,
        @Query("per_page") pageSize: Int = 10,
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "stars",
    ): ApiResponse
}
