package com.matchlessgift.donation.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// 1. Regular Donation Entity
@Entity(tableName = "donations")
data class DonationEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val devId: String? = null,
    val donorName: String,
    val donorMobile: String,
    val amount: Double,
    val purpose: String,
    val donationType: String, // e.g., 'cash', 'bank'
    val districtId: Int,
    val districtName: String,
    val thana: String,
    val address: String?,
    val counsellors: Int?,
    val depositDate: String,
    val note: String?,
    val country: String? = null,
    val city: String? = null,
    val foreignMobile: String? = null,
    val processCommittedWithSms: Int? = null,
    val isSynced: Boolean = false
)

// 2. Promised Donation Entity (Required for your store method)
@Entity(tableName = "promised_donations")
data class PromisedDonationEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val devId: String? = null,
    val donorName: String,
    val donorMobile: String,
    val amount: Double,
    val payAmount: Double = 0.0,
    val purpose: String,
    val donationType: String,
    val districtId: Int,
    val districtName: String,
    val thana: String,
    val address: String?,
    val counsellors: Int?,
    val promiseDate: String,
    val note: String?,
    val country: String? = null,
    val city: String? = null,
    val foreignMobile: String? = null,
    val sendSms: Int = 1,
    val isSynced: Boolean = false
)

// 3. District Entity
@Entity(tableName = "districts")
data class DistrictEntity(
    @PrimaryKey val id: Int,
    val name: String
)

// 4. Thana Entity
@Entity(tableName = "thanas")
data class ThanaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val districtId: Int,
    val name: String
)