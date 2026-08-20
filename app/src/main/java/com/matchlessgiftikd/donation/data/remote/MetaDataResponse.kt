package com.matchlessgiftikd.donation.data.remote

import com.google.gson.annotations.SerializedName


// ============================================================
// META DATA RESPONSE
// ============================================================

data class MetaDataResponse(

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("districts")
    val districts: List<DistrictDto> = emptyList(),

    @SerializedName("thanas")
    val thanas: List<ThanaDto> = emptyList(),

    @SerializedName("donation_types")
    val donationTypes: List<String> = emptyList()
)


// ============================================================
// DISTRICT
// ============================================================

data class DistrictDto(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)


// ============================================================
// THANA
// ============================================================

data class ThanaDto(

    @SerializedName("id")
    val id: Int,

    @SerializedName("district_id")
    val districtId: Int,

    @SerializedName("name")
    val name: String
)
