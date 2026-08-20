package com.matchlessgiftikd.donation.repository

import android.content.Context

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

import com.matchlessgiftikd.donation.data.local.AppDatabase
import com.matchlessgiftikd.donation.data.local.DistrictEntity
import com.matchlessgiftikd.donation.data.local.DonationEntity
import com.matchlessgiftikd.donation.data.local.PromisedDonationEntity
import com.matchlessgiftikd.donation.data.local.ThanaEntity
import com.matchlessgiftikd.donation.data.remote.ApiService
import com.matchlessgiftikd.donation.data.remote.DistrictDto
import com.matchlessgiftikd.donation.worker.SyncWorker

import kotlinx.coroutines.flow.Flow

class DonationRepository(
    private val context: Context
) {

    private val db = AppDatabase.getDatabase(context)
    private val apiService = ApiService.create()

    // ============================================================
    // REGULAR DONATION
    // ============================================================

    /**
     * Save donation into Room first.
     *
     * IMPORTANT:
     * This does NOT require internet.
     */
    suspend fun saveDonation(
        donation: DonationEntity
    ): Long {

        val insertedId =
            db.donationDao().insertDonation(donation)

        // Queue background synchronization.
        scheduleSync()

        return insertedId
    }

    fun getAllLocalDonations(): Flow<List<DonationEntity>> {
        return db.donationDao().getAllDonations()
    }

    suspend fun getUnsyncedDonationCount(): Int {
        return db.donationDao().getUnsyncedDonationCount()
    }


    // ============================================================
    // PROMISED / COMMITTED DONATION
    // ============================================================

    /**
     * Save promised/committed donation locally first.
     *
     * Internet is NOT required here.
     */
    suspend fun savePromisedDonation(
        donation: PromisedDonationEntity
    ): Long {

        val insertedId =
            db.promisedDonationDao()
                .insertPromisedDonation(donation)

        // Queue synchronization.
        scheduleSync()

        return insertedId
    }

    fun getAllLocalPromisedDonations():
            Flow<List<PromisedDonationEntity>> {

        return db.promisedDonationDao()
            .getAllPromisedDonations()
    }

    suspend fun getUnsyncedPromisedDonationCount(): Int {
        return db.promisedDonationDao()
            .getUnsyncedPromisedDonationCount()
    }


    // ============================================================
    // TOTAL PENDING QUEUE
    // ============================================================

    suspend fun getTotalUnsyncedCount(): Int {

        val donations =
            db.donationDao()
                .getUnsyncedDonationCount()

        val promised =
            db.promisedDonationDao()
                .getUnsyncedPromisedDonationCount()

        return donations + promised
    }


    // ============================================================
    // META DATA
    // ============================================================

    suspend fun syncMetaData(): Boolean {

        return try {

            val response =
                apiService.fetchMetaData()

            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                val metaData =
                    response.body()!!

                val districts =
                    metaData.districts.map {
                        district: DistrictDto ->

                        DistrictEntity(
                            id = district.id,
                            name = district.name
                        )
                    }

                db.metaDataDao()
                    .insertDistricts(districts)

                true

            } else {

                false
            }

        } catch (e: Exception) {

            e.printStackTrace()

            false
        }
    }


    // ============================================================
    // WORKMANAGER
    // ============================================================

    /**
     * Queue synchronization.
     *
     * WorkManager will NOT run this worker while there is
     * no network connection.
     *
     * When network becomes available, WorkManager runs it.
     */
    fun scheduleSync() {

        val constraints =
            Constraints.Builder()
                .setRequiredNetworkType(
                    NetworkType.CONNECTED
                )
                .build()

        val syncRequest =
            OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                SYNC_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                syncRequest
            )
    }

    companion object {

        const val SYNC_WORK_NAME =
            "AutoDonationSync"
    }
}
