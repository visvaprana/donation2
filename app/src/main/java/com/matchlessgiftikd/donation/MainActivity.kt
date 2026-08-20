package com.matchlessgiftikd.donation

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import com.matchlessgiftikd.donation.data.local.DonationEntity
import com.matchlessgiftikd.donation.repository.DonationRepository

import kotlinx.coroutines.launch

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {

    // ============================================================
    // REPOSITORY
    // ============================================================

    private lateinit var repository: DonationRepository

    // ============================================================
    // DATE
    // ============================================================

    private val calendar =
        Calendar.getInstance()

    private val dateFormat =
        SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        )

    // ============================================================
    // DISTRICTS
    // ============================================================

    private var districts =
        emptyList<com.matchlessgiftikd.donation.data.local.DistrictEntity>()


    // ============================================================
    // ON CREATE
    // ============================================================

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_main
        )

        repository =
            DonationRepository(this)

        // --------------------------------------------------------
        // FIND VIEWS
        // --------------------------------------------------------

        val etDevId =
            findViewById<EditText>(
                R.id.etDevId
            )

        val etDonorName =
            findViewById<EditText>(
                R.id.etDonorName
            )

        val etMobile =
            findViewById<EditText>(
                R.id.etMobile
            )

        val etAddress =
            findViewById<EditText>(
                R.id.etAddress
            )

        val etAmount =
            findViewById<EditText>(
                R.id.etAmount
            )

        val etNote =
            findViewById<EditText>(
                R.id.etNote
            )

        val etDepositDate =
            findViewById<EditText>(
                R.id.etDepositDate
            )

        val spDistrict =
            findViewById<Spinner>(
                R.id.spDistrict
            )

        val spThana =
            findViewById<Spinner>(
                R.id.spThana
            )

        val spPurpose =
            findViewById<Spinner>(
                R.id.spPurpose
            )

        val rgDonationType =
            findViewById<RadioGroup>(
                R.id.rgDonationType
            )

        val btnSave =
            findViewById<Button>(
                R.id.btnSave
            )


        // ========================================================
        // DEFAULT DATE
        // ========================================================

        etDepositDate.setText(
            dateFormat.format(
                calendar.time
            )
        )


        // ========================================================
        // DATE PICKER
        // ========================================================

        etDepositDate.setOnClickListener {

            DatePickerDialog(
                this,

                { _, year, month, dayOfMonth ->

                    calendar.set(
                        Calendar.YEAR,
                        year
                    )

                    calendar.set(
                        Calendar.MONTH,
                        month
                    )

                    calendar.set(
                        Calendar.DAY_OF_MONTH,
                        dayOfMonth
                    )

                    etDepositDate.setText(
                        dateFormat.format(
                            calendar.time
                        )
                    )
                },

                calendar.get(
                    Calendar.YEAR
                ),

                calendar.get(
                    Calendar.MONTH
                ),

                calendar.get(
                    Calendar.DAY_OF_MONTH
                )

            ).show()
        }


        // ========================================================
        // PURPOSE
        // ========================================================

        val defaultPurposes =
            listOf(
                "সাধারণ প্রণামী",
                "ভোগ প্রণামী",
                "অন্যান্য প্রনামী"
            )

        val purposeAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                defaultPurposes
            )

        purposeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spPurpose.adapter =
            purposeAdapter


        // ========================================================
        // LOAD DISTRICTS + THANAS
        // ========================================================

        lifecycleScope.launch {

            try {

                // ------------------------------------------------
                // Download latest metadata from Laravel.
                //
                // Repository also stores it in Room.
                // If internet is unavailable, Room data can still
                // be used if it was previously downloaded.
                // ------------------------------------------------

                repository.syncMetaData()

                districts =
                    repository.getDistricts()


                // ------------------------------------------------
                // DISTRICT SPINNER
                // ------------------------------------------------

                val districtNames =
                    districts.map {
                        it.name
                    }

                val districtAdapter =
                    ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        districtNames
                    )

                districtAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
                )

                spDistrict.adapter =
                    districtAdapter


                // ------------------------------------------------
                // LOAD FIRST DISTRICT'S THANAS
                // ------------------------------------------------

                if (districts.isNotEmpty()) {

                    loadThanas(
                        districts[0].id,
                        spThana
                    )
                }


                // ------------------------------------------------
                // DISTRICT SELECTION
                // ------------------------------------------------

                spDistrict.onItemSelectedListener =
                    object :
                        AdapterView.OnItemSelectedListener {

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: android.view.View?,
                            position: Int,
                            id: Long
                        ) {

                            if (
                                position >= 0 &&
                                position < districts.size
                            ) {

                                val districtId =
                                    districts[position].id

                                loadThanas(
                                    districtId,
                                    spThana
                                )
                            }
                        }


                        override fun onNothingSelected(
                            parent: AdapterView<*>?
                        ) {
                            // Nothing required.
                        }
                    }


                // ------------------------------------------------
                // START BACKGROUND SYNC
                // ------------------------------------------------

                repository.scheduleSync()

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@MainActivity,
                    "Could not load district data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        // ========================================================
        // SAVE DONATION
        // ========================================================

        btnSave.setOnClickListener {

            val name =
                etDonorName.text
                    .toString()
                    .trim()

            val mobile =
                etMobile.text
                    .toString()
                    .trim()

            val amountStr =
                etAmount.text
                    .toString()
                    .trim()

            val address =
                etAddress.text
                    .toString()
                    .trim()

            val note =
                etNote.text
                    .toString()
                    .trim()

            val depositDate =
                etDepositDate.text
                    .toString()
                    .trim()


            // ----------------------------------------------------
            // VALIDATION
            // ----------------------------------------------------

            if (
                name.isEmpty() ||
                mobile.isEmpty() ||
                amountStr.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please enter Name, Mobile and Amount",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            val amount =
                amountStr.toDoubleOrNull()

            if (
                amount == null ||
                amount <= 0
            ) {

                Toast.makeText(
                    this,
                    "Please enter a valid amount",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // ----------------------------------------------------
            // SELECTED DISTRICT
            // ----------------------------------------------------

            val districtPosition =
                spDistrict.selectedItemPosition

            val selectedDistrict =
                districts.getOrNull(
                    districtPosition
                )

            if (
                selectedDistrict == null
            ) {

                Toast.makeText(
                    this,
                    "Please select a district",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // ----------------------------------------------------
            // SELECTED THANA
            // ----------------------------------------------------

            val selectedThana =
                spThana.selectedItem
                    ?.toString()
                    ?.trim()
                    ?: ""


            if (
                selectedThana.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please select a Thana",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // ----------------------------------------------------
            // PURPOSE
            // ----------------------------------------------------

            val selectedPurpose =
                spPurpose.selectedItem
                    ?.toString()
                    ?: "সাধারণ প্রণামী"


            // ----------------------------------------------------
            // DONATION TYPE
            // ----------------------------------------------------

            val donationType =

                if (
                    rgDonationType.checkedRadioButtonId
                    == R.id.rbBank
                ) {

                    "bank"

                } else {

                    "cash"
                }


            // ----------------------------------------------------
            // DEV ID
            // ----------------------------------------------------

            val devId =
                etDevId.text
                    .toString()
                    .trim()
                    .ifEmpty {
                        null
                    }


            // ====================================================
            // CREATE LOCAL DONATION
            // ====================================================

            val donation =
                DonationEntity(

                    devId = devId,

                    donorName = name,

                    donorMobile = mobile,

                    amount = amount,

                    purpose = selectedPurpose,

                    donationType = donationType,

                    districtId =
                        selectedDistrict.id,

                    districtName =
                        selectedDistrict.name,

                    thana =
                        selectedThana,

                    address =
                        address.ifEmpty {
                            null
                        },

                    counsellors = null,

                    depositDate =
                        depositDate,

                    note =
                        note.ifEmpty {
                            null
                        },

                    country = null,

                    city = null,

                    foreignMobile = null,

                    promiseToken = 0,

                    processCommittedWithSms = null,

                    isSynced = false
                )


            // ====================================================
            // SAVE TO ROOM
            // ====================================================

            lifecycleScope.launch {

                try {

                    repository.saveDonation(
                        donation
                    )


                    Toast.makeText(
                        this@MainActivity,
                        "Saved locally! Auto sync queued.",
                        Toast.LENGTH_LONG
                    ).show()


                    // ------------------------------------------------
                    // CLEAR FORM
                    // ------------------------------------------------

                    etDevId.text.clear()

                    etDonorName.text.clear()

                    etMobile.text.clear()

                    etAmount.text.clear()

                    etAddress.text.clear()

                    etNote.text.clear()


                    // Return date to today.

                    calendar.time =
                        Calendar.getInstance().time

                    etDepositDate.setText(
                        dateFormat.format(
                            calendar.time
                        )
                    )


                } catch (e: Exception) {

                    e.printStackTrace()

                    Toast.makeText(
                        this@MainActivity,
                        "Could not save donation",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    // ============================================================
    // LOAD THANAS
    // ============================================================

    private fun loadThanas(
        districtId: Int,
        spinner: Spinner
    ) {

        lifecycleScope.launch {

            try {

                val thanas =
                    repository.getThanasByDistrict(
                        districtId
                    )


                val thanaNames =
                    thanas.map {
                        it.name
                    }


                val adapter =
                    ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        thanaNames
                    )


                adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
                )


                spinner.adapter =
                    adapter

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@MainActivity,
                    "Could not load Thana data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
