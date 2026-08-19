package com.matchlessgiftikd.donation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.matchlessgiftikd.donation.data.local.AppDatabase
import com.matchlessgiftikd.donation.data.local.DonationEntity
import com.matchlessgiftikd.donation.data.local.PromisedDonationEntity
import com.matchlessgiftikd.donation.data.remote.ApiService
import com.matchlessgiftikd.donation.data.remote.DonationRequest
import com.matchlessgiftikd.donation.data.remote.PromisedDonationRequest

class SyncWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(applicationContext)
        val apiService = ApiService.create()
        var hasError = false

        // 1. SYNC REGULAR DONATIONS
        val unsyncedDonations: List<DonationEntity> = database.donationDao().getUnsyncedDonations()
        for (item in unsyncedDonations) {
            try {
                val request = DonationRequest(
                    donarName = item.donorName,
                    donarMobile = item.donorMobile,
                    amount = item.amount,
                    purpose = item.purpose,
                    donationType = item.donationType,
                    district = item.districtId,
                    districtName = item.districtName,
                    thana = item.thana,
                    address = item.address,
                    counsellors = item.counsellors,
                    depositDate = item.depositDate,
                    note = item.note,
                    sendSms = 1
                )

                val response = apiService.syncDonation(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    database.donationDao().markAsSynced(item.localId)
                } else {
                    hasError = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                hasError = true
            }
        }

        // 2. SYNC PROMISED DONATIONS
        val unsyncedPromised: List<PromisedDonationEntity> = database.promisedDonationDao().getUnsyncedPromisedDonations()
        for (item in unsyncedPromised) {
            try {
                val request = PromisedDonationRequest(
                    donarName = item.donorName,
                    donarMobile = item.donorMobile,
                    amount = item.amount,
                    payAmount = item.payAmount,
                    purpose = item.purpose,
                    donationType = item.donationType,
                    district = item.districtId,
                    districtName = item.districtName,
                    thana = item.thana,
                    address = item.address,
                    counsellors = item.counsellors,
                    promiseDate = item.promiseDate,
                    note = item.note,
                    sendSms = 1
                )

                val response = apiService.syncPromisedDonation(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    database.promisedDonationDao().markAsSynced(item.localId)
                } else {
                    hasError = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                hasError = true
            }
        }

        return if (hasError) Result.retry() else Result.success()
    }
}
