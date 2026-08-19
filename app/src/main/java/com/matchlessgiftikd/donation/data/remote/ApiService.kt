package com.matchlessgiftikd.donation.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class ApiResponse(
    val success: Boolean,
    val message: String
)

data class DistrictDto(
    val id: Int,
    val name: String
)

data class MetaDataResponse(
    val districts: List<DistrictDto>
)

data class PromisedDonationRequest(
    @SerializedName("donar_name") val donarName: String,
    @SerializedName("donar_mobile") val donarMobile: String,
    val amount: Double,
    @SerializedName("pay_amount") val payAmount: Double,
    val purpose: String,
    @SerializedName("donation_type") val donationType: String,
    val district: Int,
    @SerializedName("district_name") val districtName: String,
    val thana: String,
    val address: String?,
    val counsellors: Int?,
    @SerializedName("promise_date") val promiseDate: String,
    val note: String?,
    @SerializedName("send_sms") val sendSms: Int = 1
)

data class DonationRequest(
    @SerializedName("donar_name") val donarName: String,
    @SerializedName("donar_mobile") val donarMobile: String,
    val amount: Double,
    val purpose: String,
    @SerializedName("donation_type") val donationType: String,
    val district: Int,
    @SerializedName("district_name") val districtName: String,
    val thana: String,
    val address: String?,
    val counsellors: Int?,
    @SerializedName("deposit_date") val depositDate: String,
    val note: String?,
    @SerializedName("send_sms") val sendSms: Int = 1
)

interface ApiService {
    // Aligned with web.php: Route::post('/app/donations/store', ...)
    @POST("app/donations/store")
    suspend fun syncDonation(@Body request: DonationRequest): Response<ApiResponse>

    // Aligned with web.php: Route::post('/app/promised-donations/store', ...)
    @POST("app/promised-donations/store")
    suspend fun syncPromisedDonation(@Body request: PromisedDonationRequest): Response<ApiResponse>

    // Aligned with web.php: Route::get('/app/promised-donations/meta-data', ...)
    @GET("app/promised-donations/meta-data")
    suspend fun fetchMetaData(): Response<MetaDataResponse>

    companion object {
        private const val BASE_URL = "https://donation.matchlessgiftikd.com/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
