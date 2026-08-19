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
import com.matchlessgiftikd.donation.data.remote.ApiService
import com.matchlessgiftikd.donation.data.remote.DistrictDto
import com.matchlessgiftikd.donation.worker.SyncWorker

class DonationRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val apiService = ApiService.create()

    suspend fun saveDonation(donation: DonationEntity) {
        // 1. Save locally to Room DB first
        db.donationDao().insertDonation(donation)

        // 2. Schedule background WorkManager task as reliable backup
        scheduleSync()
    }

    suspend fun syncMetaData() {
        try {
            val response = apiService.fetchMetaData()
            if (response.isSuccessful && response.body() != null) {
                val metaData = response.body()!!
                val districts = metaData.districts.map { district: DistrictDto ->
                    DistrictEntity(id = district.id, name = district.name)
                }
                db.metaDataDao().insertDistricts(districts)
            }
        } catch (e: Exception) {
            // Keep existing cached database offline
        }
    }

    fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        // CHANGED: Use REPLACE so new saves immediately enqueue a execution job
        WorkManager.getInstance(context).enqueueUniqueWork(
            "AutoDonationSync",
            ExistingWorkPolicy.REPLACE, 
            syncRequest
        )
    }
}
            syncRequest
        )
    }
}
