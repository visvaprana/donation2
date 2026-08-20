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

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(
    appContext,
    workerParams
) {

    override suspend fun doWork(): Result {

        val database =
            AppDatabase.getDatabase(
                applicationContext
            )

        val apiService =
            ApiService.create()

        // ========================================================
        // 1. REGULAR DONATIONS
        // ========================================================

        val unsyncedDonations =
            database
                .donationDao()
                .getUnsyncedDonations()

        for (item: DonationEntity in unsyncedDonations) {

            try {

                val request =
                    DonationRequest(

                        donarName =
                            item.donorName,

                        donarMobile =
                            item.donorMobile,

                        amount =
                            item.amount,

                        purpose =
                            item.purpose,

                        donationType =
                            item.donationType,

                        district =
                            item.districtId,

                        districtName =
                            item.districtName,

                        thana =
                            item.thana,

                        address =
                            item.address,

                        counsellors =
                            item.counsellors,

                        depositDate =
                            item.depositDate,

                        note =
                            item.note,

                        sendSms = 1
                    )

                val response =
                    apiService.syncDonation(request)

                // =================================================
                // ONLY MARK SYNCED AFTER SERVER SUCCESS
                // =================================================

                if (
                    response.isSuccessful &&
                    response.body()?.success == true
                ) {

                    database
                        .donationDao()
                        .markAsSynced(item.localId)

                } else {

                    // Do NOT mark synced.
                    //
                    // The record remains in Room and will
                    // be retried later.

                    return Result.retry()
                }

            } catch (e: Exception) {

                e.printStackTrace()

                // Network/server failure.
                // Keep the local record.
                return Result.retry()
            }
        }


        // ========================================================
        // 2. PROMISED / COMMITTED DONATIONS
        // ========================================================

        val unsyncedPromised =
            database
                .promisedDonationDao()
                .getUnsyncedPromisedDonations()

        for (
            item: PromisedDonationEntity
            in unsyncedPromised
        ) {

            try {

                val request =
                    PromisedDonationRequest(

                        donarName =
                            item.donorName,

                        donarMobile =
                            item.donorMobile,

                        amount =
                            item.amount,

                        payAmount =
                            item.payAmount,

                        purpose =
                            item.purpose,

                        donationType =
                            item.donationType,

                        district =
                            item.districtId,

                        districtName =
                            item.districtName,

                        thana =
                            item.thana,

                        address =
                            item.address,

                        counsellors =
                            item.counsellors,

                        promiseDate =
                            item.promiseDate,

                        note =
                            item.note,

                        sendSms =
                            item.sendSms
                    )

                val response =
                    apiService
                        .syncPromisedDonation(request)

                // =================================================
                // ONLY MARK SYNCED AFTER SERVER SUCCESS
                // =================================================

                if (
                    response.isSuccessful &&
                    response.body()?.success == true
                ) {

                    database
                        .promisedDonationDao()
                        .markAsSynced(item.localId)

                } else {

                    return Result.retry()
                }

            } catch (e: Exception) {

                e.printStackTrace()

                return Result.retry()
            }
        }


        // ========================================================
        // 3. IMPORTANT: CHECK AGAIN
        // ========================================================
        //
        // If another entry was saved while this Worker was
        // running, make sure we don't finish while something
        // remains unsynced.
        //

        val remainingDonations =
            database
                .donationDao()
                .getUnsyncedDonationCount()

        val remainingPromised =
            database
                .promisedDonationDao()
                .getUnsyncedPromisedDonationCount()

        return if (
            remainingDonations == 0 &&
            remainingPromised == 0
        ) {

            Result.success()

        } else {

            Result.retry()
        }
    }
}
