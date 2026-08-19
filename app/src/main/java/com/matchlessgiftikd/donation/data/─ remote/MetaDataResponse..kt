package com.matchlessgiftikd.donation.data.remote

import com.google.gson.annotations.SerializedName

data class MetaDataResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("districts")
    val districts: List<DistrictDto>,
    @SerializedName("thanas")
    val thanas: List<ThanaDto>? = emptyList()
)

data class DistrictDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

data class ThanaDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("district_id")
    val districtId: Int,
    @SerializedName("name")
    val name: String
)
