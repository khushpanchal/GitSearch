package com.khush.gitsearch.data.database

import com.khush.gitsearch.data.model.MainData
import kotlinx.coroutines.flow.Flow

interface DatabaseService {
    fun getAllData(): Flow<List<MainData>>
    fun deleteAllAndInsertAll(articles: List<MainData>)
}