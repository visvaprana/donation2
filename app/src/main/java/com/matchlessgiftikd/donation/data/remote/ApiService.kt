package com.matchlessgiftikd.donation.data.remote

import com.google.gson.annotations.SerializedName

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


// ============================================================
// GENERIC API RESPONSE
// ============================================================

data class ApiResponse(

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    /**
     * Some Laravel responses may return an ID.
     * We keep it optional so the Android app remains compatible
     * if the API doesn't return it.
     */
    @SerializedName("id")
    val id: Long? = null
)


// ============================================================
// REGULAR DONATION REQUEST
// ============================================================

data class DonationRequest(

    @SerializedName("dev_id")
    val devId: String? = null,

    @SerializedName("donar_name")
    val donarName: String,

    @SerializedName("donar_mobile")
    val donarMobile: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("purpose")
    val purpose: String,

    @SerializedName("donation_type")
    val donationType: String,

    @SerializedName("district")
    val district: Int,

    @SerializedName("district_name")
    val districtName: String,

    @SerializedName("thana")
    val thana: String,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("counsellors")
    val counsellors: Int? = null,

    @SerializedName("deposit_date")
    val depositDate: String,

    @SerializedName("note")
    val note: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("foreign_mobile")
    val foreignMobile: String? = null,

    @SerializedName("process_committed_with_sms")
    val processCommittedWithSms: Int? = null,

    @SerializedName("promise_token")
val promiseToken: Int = 0,

    @SerializedName("send_sms")
    val sendSms: Int = 1
)


// ============================================================
// PROMISED / COMMITTED DONATION REQUEST
// ============================================================

data class PromisedDonationRequest(

    @SerializedName("dev_id")
    val devId: String? = null,

    @SerializedName("donar_name")
    val donarName: String,

    @SerializedName("donar_mobile")
    val donarMobile: String,

    /**
     * Total committed amount.
     */
    @SerializedName("amount")
    val amount: Double,

    /**
     * Amount paid at the time the commitment is created.
     */
    @SerializedName("pay_amount")
    val payAmount: Double,

    @SerializedName("purpose")
    val purpose: String,

    @SerializedName("donation_type")
    val donationType: String,

    @SerializedName("district")
    val district: Int,

    @SerializedName("district_name")
    val districtName: String,

    @SerializedName("thana")
    val thana: String,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("counsellors")
    val counsellors: Int? = null,

    @SerializedName("promise_date")
    val promiseDate: String,

    @SerializedName("note")
    val note: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("foreign_mobile")
    val foreignMobile: String? = null,

    @SerializedName("send_sms")
    val sendSms: Int = 1
)


// ============================================================
// API SERVICE
// ============================================================

interface ApiService {

    // ----------------------------------------------------------
    // REGULAR DONATION
    // ----------------------------------------------------------

    @POST("app/donations/store")
    suspend fun syncDonation(
        @Body request: DonationRequest
    ): Response<ApiResponse>


    // ----------------------------------------------------------
    // PROMISED / COMMITTED DONATION
    // ----------------------------------------------------------

    @POST("app/promised-donations/store")
    suspend fun syncPromisedDonation(
        @Body request: PromisedDonationRequest
    ): Response<ApiResponse>


    // ----------------------------------------------------------
    // META DATA
    // ----------------------------------------------------------

    @GET("app/promised-donations/meta-data")
    suspend fun fetchMetaData():
            Response<MetaDataResponse>


    // ----------------------------------------------------------
    // RETROFIT
    // ----------------------------------------------------------

    companion object {

        private const val BASE_URL =
            "https://donation.matchlessgiftikd.com/"

        fun create(): ApiService {

            return Retrofit.Builder()

                .baseUrl(BASE_URL)

                .addConverterFactory(
                    GsonConverterFactory.create()
                )

                .build()

                .create(ApiService::class.java)
        }
    }
}
