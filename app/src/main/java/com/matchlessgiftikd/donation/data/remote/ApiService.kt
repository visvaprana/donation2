package com.matchlessgiftikd.donation.data.remote

import com.google.gson.annotations.SerializedName

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


// ============================================================
// GENERIC API RESPONSE
// ============================================================

data class ApiResponse(

    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("message")
    val message: String? = null,

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

    @SerializedName("amount")
    val amount: Double,

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


// ============================================================
// DONATION LIST RESPONSE
// ============================================================

data class DonationListResponse(

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("total_amount")
    val totalAmount: Double = 0.0,

    @SerializedName("donations")
    val donations: DonationPagination? = null
)


// ============================================================
// DONATION PAGINATION
// ============================================================

data class DonationPagination(

    @SerializedName("current_page")
    val currentPage: Int = 1,

    @SerializedName("last_page")
    val lastPage: Int = 1,

    @SerializedName("per_page")
    val perPage: Int = 50,

    @SerializedName("total")
    val total: Int = 0,

    @SerializedName("data")
    val data: List<DonationListItem> = emptyList()
)


// ============================================================
// DONATION LIST ITEM
// ============================================================

data class DonationListItem(

    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("donar_name")
    val donorName: String? = null,

    @SerializedName("donar_mobile")
    val donorMobile: String? = null,

    @SerializedName("amount")
    val amount: Double = 0.0,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("deposit_date")
    val depositDate: String? = null,

    @SerializedName("purpose")
    val purpose: String? = null
)


// ============================================================
// PROMISED DONATION LIST RESPONSE
// ============================================================

data class PromisedDonationListResponse(

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("is_admin")
    val isAdmin: Boolean = false,

    @SerializedName("total_amount")
    val totalAmount: Double = 0.0,

    @SerializedName("total_paid_from_donations")
    val totalPaidFromDonations: Double = 0.0,

    @SerializedName("total_paid_from_promised")
    val totalPaidFromPromised: Double = 0.0,

    @SerializedName("total_paid_overall")
    val totalPaidOverall: Double = 0.0,

    @SerializedName("total_due_overall")
    val totalDueOverall: Double = 0.0,

    @SerializedName("promises")
    val promises: PromisedPagination? = null
)


// ============================================================
// PROMISED DONATION PAGINATION
// ============================================================

data class PromisedPagination(

    @SerializedName("current_page")
    val currentPage: Int = 1,

    @SerializedName("last_page")
    val lastPage: Int = 1,

    @SerializedName("total")
    val total: Int = 0,

    @SerializedName("data")
    val data: List<PromisedDonationListItem> = emptyList()
)


// ============================================================
// PROMISED DONATION LIST ITEM
// ============================================================

data class PromisedDonationListItem(

    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("donar_name")
    val donorName: String? = null,

    @SerializedName("donar_mobile")
    val donorMobile: String? = null,

    @SerializedName("amount")
    val amount: Double = 0.0,

    @SerializedName("payAmount")
    val payAmountWrong: Double? = null,

    @SerializedName("pay_amount")
    val payAmount: Double = 0.0,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("promise_date")
    val promiseDate: String? = null
)


// ============================================================
// DONOR SUMMARY
// ============================================================

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


// ============================================================
// API SERVICE
// ============================================================

interface ApiService {

    // ========================================================
    // REGULAR DONATION
    // ========================================================

    @POST("app/donations/store")
    suspend fun syncDonation(
        @Body request: DonationRequest
    ): Response<ApiResponse>


    // ========================================================
    // PROMISED / COMMITTED DONATION
    // ========================================================

    @POST("app/promised-donations/store")
    suspend fun syncPromisedDonation(
        @Body request: PromisedDonationRequest
    ): Response<ApiResponse>


    // ========================================================
    // META DATA
    // ========================================================

    @GET("app/promised-donations/meta-data")
    suspend fun fetchMetaData():
            Response<MetaDataResponse>


    // ========================================================
    // DONATIONS LIST
    // ========================================================

    @GET("app/donations/list")
    suspend fun fetchDonations(
        @Query("donar_name")
        donorName: String? = null,

        @Query("from_date")
        fromDate: String? = null,

        @Query("to_date")
        toDate: String? = null,

        @Query("type")
        type: String? = null,

        @Query("page")
        page: Int = 1
    ): Response<DonationListResponse>


    // ========================================================
    // PROMISED DONATIONS LIST
    // ========================================================

    @GET("app/promised-donations")
    suspend fun fetchPromisedDonations(
        @Query("donar_name")
        donorName: String? = null,

        @Query("from_date")
        fromDate: String? = null,

        @Query("to_date")
        toDate: String? = null,

        @Query("type")
        type: String? = null,

        @Query("page")
        page: Int = 1
    ): Response<PromisedDonationListResponse>


    // ========================================================
    // DONOR SUMMARY
    // ========================================================

    @GET("app/donor-summary")
    suspend fun fetchDonorSummary(
        @Query("donar_mobile")
        donarMobile: String,

        @Query("donar_name")
        donarName: String? = null
    ): Response<DonorSummaryResponse>


    // ========================================================
    // RETROFIT
    // ========================================================

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
