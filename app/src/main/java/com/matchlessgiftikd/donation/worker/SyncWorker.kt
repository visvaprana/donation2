package com.matchlessgiftikd.donation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.matchlessgiftikd.donation.data.local.AppDatabase
import com.matchlessgiftikd.donation.data.remote.ApiService
import com.matchlessgiftikd.donation.data.remote.PromisedDonationRequest

class SyncWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(applicationContext)
        val apiService = ApiService.create()

        val unsyncedPromised = database.promisedDonationDao().getUnsyncedPromisedDonations()

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
                    counsellors = item.counsellors.toIntOrNull(),
                    promiseDate = item.promiseDate,
                    note = item.note,
                    sendSms = 1
                )

                val response = apiService.syncPromisedDonation(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    database.promisedDonationDao().markAsSynced(item.localId)
                }
            } catch (e: Exception) {
                return Result.retry()
            }
        }
        return Result.success()
    }
}
