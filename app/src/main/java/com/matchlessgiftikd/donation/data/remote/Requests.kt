package com.matchlessgiftikd.donation.data.remote

import com.google.gson.annotations.SerializedName

// ১. জেনেরিক এপিআই রেসপন্স (Generic API Response)
data class ApiResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Any? = null
)

// ২. মেটাডেটা রেসপন্স (Districts, Thanas & Types Sync করার জন্য)
data class MetaDataResponse(
    @SerializedName("status") val status: String,
    @SerializedName("districts") val districts: List<DistrictDto>,
    @SerializedName("donation_types") val donationTypes: List<String>
)

data class DistrictDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

// ৩. সাধারণ ডোনেশন রিকোয়েস্ট (Regular Donation Request Body)
data class DonationRequest(
    @SerializedName("donar_name") val donarName: String,
    @SerializedName("donar_mobile") val donarMobile: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("purpose") val purpose: String,
    @SerializedName("donation_type") val donationType: String,
    @SerializedName("district") val districtId: Int,
    @SerializedName("district_name") val districtName: String,
    @SerializedName("thana") val thana: String,
    @SerializedName("address") val address: String?,
    @SerializedName("counsellors") val counsellors: Int?,
    @SerializedName("deposit_date") val depositDate: String,
    @SerializedName("note") val note: String?,
    @SerializedName("country") val country: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("foreign_mobile") val foreignMobile: String? = null,
    @SerializedName("process_committed_with_sms") val processCommittedWithSms: Int? = null
)

// ৪. প্রতিশ্রুত ডোনেশন রিকোয়েস্ট (Promised Donation Request Body)
data class PromisedDonationRequest(
    @SerializedName("donar_name") val donarName: String,
    @SerializedName("donar_mobile") val donarMobile: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("pay_amount") val payAmount: Double = 0.0,
    @SerializedName("purpose") val purpose: String,
    @SerializedName("donation_type") val donationType: String,
    @SerializedName("district") val districtId: Int,
    @SerializedName("district_name") val districtName: String,
    @SerializedName("thana") val thana: String,
    @SerializedName("address") val address: String?,
    @SerializedName("counsellors") val counsellors: Int?,
    @SerializedName("promise_date") val promiseDate: String,
    @SerializedName("note") val note: String?,
    @SerializedName("country") val country: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("foreign_mobile") val foreignMobile: String? = null,
    @SerializedName("send_sms") val sendSms: Int = 1
)
