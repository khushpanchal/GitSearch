package com.khush.gitsearch.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApiResponse(
    @SerializedName("total_count")
    val totalResults: Int,
    @SerializedName("items")
    val repos: List<MainData>
)

@Entity(
    tableName = "cached_data",
    indices = [Index(
        value = ["id"],
        unique = true
    )]
)
data class MainData (
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    val name: String,
    @Embedded
    val owner: Owner,
    @SerializedName("html_url")
    val repoLink: String,
    @SerializedName("description")
    val description: String? = "",
    @SerializedName("stargazers_count")
    val stars: Int
): Serializable

data class Owner (
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
): Serializable