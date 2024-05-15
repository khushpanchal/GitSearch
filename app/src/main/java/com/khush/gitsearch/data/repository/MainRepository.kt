package com.khush.gitsearch.data.repository

import com.khush.gitsearch.data.database.DatabaseService
import com.khush.gitsearch.data.model.MainData
import com.khush.gitsearch.data.network.ApiInterface
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val databaseService: DatabaseService
) {

    suspend fun getMainData(searchQuery: String, pageNum: Int): List<MainData> {
        if (pageNum == 1) {
            fetchAndSaveToDb(searchQuery, pageNum)
            return databaseService.getAllData().first()
        }
        return apiInterface.getGitResponse(searchQuery, pageNum).repos
    }


    private suspend fun fetchAndSaveToDb(searchQuery: String, pageNum: Int) {
        try {
            val mainData = apiInterface.getGitResponse(searchQuery, pageNum).repos
            databaseService.deleteAllAndInsertAll(mainData)
        } catch (ignore: Exception) {

        }
    }

    suspend fun getDataFromDb(): List<MainData> {
        return databaseService.getAllData().first()
    }

}