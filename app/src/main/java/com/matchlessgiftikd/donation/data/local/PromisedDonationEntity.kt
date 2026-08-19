package com.matchlessgiftikd.donation.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promised_donations")
data class PromisedDonationEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val donorName: String,
    val donorMobile: String,
    val amount: Double,
    val payAmount: Double,
    val purpose: String,
    val donationType: String,
    val districtId: Int,
    val districtName: String,
    val thana: String,
    val address: String,
    val counsellors: String,
    val promiseDate: String,
    val note: String,
    val isSynced: Boolean = false
)
