package com.matchlessgiftikd.donation.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


// ============================================================
// 1. REGULAR DONATION
// ============================================================

@Entity(tableName = "donations")
data class DonationEntity(

    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,

    // --------------------------------------------------------
    // Donor information
    // --------------------------------------------------------

    val devId: String? = null,

    val donorName: String,

    val donorMobile: String,

    // --------------------------------------------------------
    // Donation information
    // --------------------------------------------------------

    val amount: Double,

    val purpose: String,

    /**
     * cash / bank
     */
    val donationType: String,

    // --------------------------------------------------------
    // Location
    // --------------------------------------------------------

    val districtId: Int,

    val districtName: String,

    val thana: String,

    val address: String?,

    // --------------------------------------------------------
    // Counsellor
    // --------------------------------------------------------

    val counsellors: Int?,

    // --------------------------------------------------------
    // Deposit information
    // --------------------------------------------------------

    val depositDate: String,

    val note: String?,

    // --------------------------------------------------------
    // Foreign donor information
    // --------------------------------------------------------

    val country: String? = null,

    val city: String? = null,

    val foreignMobile: String? = null,

    // --------------------------------------------------------
    // Promised Donation adjustment
    // --------------------------------------------------------

    /**
     * 0 = normal donation
     *
     * 1 = this donation is being adjusted against
     *     a promised/committed donation.
     *
     * IMPORTANT:
     * This value is decided when the user saves the donation.
     * It must survive offline storage and later synchronization.
     */
    val promiseToken: Int = 0,

    /**
     * Existing application field.
     *
     * Keep this separate from promiseToken.
     */
    val processCommittedWithSms: Int? = null,

    // --------------------------------------------------------
    // Offline synchronization
    // --------------------------------------------------------

    /**
     * false = waiting for Laravel
     *
     * true = Laravel successfully accepted the record
     */
    val isSynced: Boolean = false
)


// ============================================================
// 2. PROMISED / COMMITTED DONATION
// ============================================================

@Entity(tableName = "promised_donations")
data class PromisedDonationEntity(

    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,

    // --------------------------------------------------------
    // Donor information
    // --------------------------------------------------------

    val devId: String? = null,

    val donorName: String,

    val donorMobile: String,

    // --------------------------------------------------------
    // Commitment
    // --------------------------------------------------------

    /**
     * Total committed amount.
     */
    val amount: Double,

    /**
     * Amount already paid when the commitment is created.
     */
    val payAmount: Double = 0.0,

    val purpose: String,

    /**
     * cash / bank
     */
    val donationType: String,

    // --------------------------------------------------------
    // Location
    // --------------------------------------------------------

    val districtId: Int,

    val districtName: String,

    val thana: String,

    val address: String?,

    // --------------------------------------------------------
    // Counsellor
    // --------------------------------------------------------

    val counsellors: Int?,

    // --------------------------------------------------------
    // Promise information
    // --------------------------------------------------------

    val promiseDate: String,

    val note: String?,

    // --------------------------------------------------------
    // Foreign donor information
    // --------------------------------------------------------

    val country: String? = null,

    val city: String? = null,

    val foreignMobile: String? = null,

    // --------------------------------------------------------
    // SMS
    // --------------------------------------------------------

    val sendSms: Int = 1,

    // --------------------------------------------------------
    // Offline synchronization
    // --------------------------------------------------------

    val isSynced: Boolean = false
)


// ============================================================
// 3. DISTRICT
// ============================================================

@Entity(tableName = "districts")
data class DistrictEntity(

    @PrimaryKey
    val id: Int,

    val name: String
)


// ============================================================
// 4. THANA
// ============================================================

@Entity(tableName = "thanas")
data class ThanaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val districtId: Int,

    val name: String
)
