package com.poptato.data.model.response.backlog

import com.google.gson.annotations.SerializedName

data class BacklogListResponse(
    @SerializedName("totalCount")
    val totalCount: Int = -1,
    @SerializedName("backlogs")
    val backlogs: List<BacklogItemResponse> = emptyList(),
    @SerializedName("totalPageCount")
    val totalPageCount: Int = -1,
    @SerializedName("categoryName")
    val categoryName: String = ""
)
