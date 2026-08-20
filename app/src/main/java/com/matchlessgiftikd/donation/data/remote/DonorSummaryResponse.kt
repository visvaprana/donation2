package com.matchlessgiftikd.donation.data.remote

import com.google.gson.annotations.SerializedName

data class DonorSummaryResponse(

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("donor_name")
    val donorName: String? = null,

    @SerializedName("donor_mobile")
    val donorMobile: String? = null,

    @SerializedName("total_promised")
    val totalPromised: Double = 0.0,

    @SerializedName("total_paid")
    val totalPaid: Double = 0.0,

    @SerializedName("total_due")
    val totalDue: Double = 0.0,

    @SerializedName("data")
    val data: List<DonorSummaryItem> = emptyList()
)

data class DonorSummaryItem(

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("promise_token")
    val promiseToken: Int = 0,

    @SerializedName("promised_amount")
    val promisedAmount: Double = 0.0,

    @SerializedName("paid_amount")
    val paidAmount: Double = 0.0,

    @SerializedName("difference")
    val difference: Double? = null,

    @SerializedName("donar_mobile")
    val donorMobile: String? = null,

    @SerializedName("donar_name")
    val donorName: String? = null
)
