package com.matchlessgiftikd.donation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
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

class DonationActivity : AppCompatActivity() {

    private lateinit var repository: DonationRepository

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_donation)

        repository = DonationRepository(this)

        // ============================================================
        // VIEWS
        // ============================================================

        val etDevId =
            findViewById<EditText>(R.id.etDevId)

        val etDonorName =
            findViewById<EditText>(R.id.etDonorName)

        val etMobile =
            findViewById<EditText>(R.id.etMobile)

        val etAddress =
            findViewById<EditText>(R.id.etAddress)

        val etAmount =
            findViewById<EditText>(R.id.etAmount)

        val etNote =
            findViewById<EditText>(R.id.etNote)

        val etDepositDate =
            findViewById<EditText>(R.id.etDepositDate)

        val spDistrict =
            findViewById<Spinner>(R.id.spDistrict)

        val spThana =
            findViewById<Spinner>(R.id.spThana)

        val spPurpose =
            findViewById<Spinner>(R.id.spPurpose)

        val rgDonationType =
            findViewById<RadioGroup>(R.id.rgDonationType)

        val btnSave =
            findViewById<Button>(R.id.btnSave)


        // ============================================================
        // DATE
        // ============================================================

        val dateFormat =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            )

        etDepositDate.setText(
            dateFormat.format(calendar.time)
        )

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
                        dateFormat.format(calendar.time)
                    )
                },

                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

            ).show()
        }


        // ============================================================
        // PURPOSE
        // ============================================================

        val purposes = listOf(

            "ভোগ প্রনামী",

            "ট্যুর বা বাহিরের ভক্তদের প্রসাদ",

            "বৈষ্ণব সেবা",

            "সাধারণ প্রণামী",

            "পূজা",

            "নির্মাণ বা উন্নয়ন",

            "ভূমি দান",

            "গুরুকুল উন্নয়ন",

            "উৎসব",

            "অতিথিশালার রুম ভাড়া",

            "নামহট্ট প্রচার",

            "নিত্যসেবা",

            "গো-সেবা",

            "ইয়ূথ ফোরাম",

            "নগর সংকীর্তন",

            "ট্যুর ও ট্র্যাভেলস",

            "BIVS",

            "কৃষি ক্ষেত",

            "গ্রন্থ বিক্রয়",

            "প্যারামাল বিক্রয়",

            "বেকারির জিনিস বিক্রয়",

            "অন্যান্য প্রনামী"
        )

        val purposeAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                purposes
            )

        purposeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spPurpose.adapter =
            purposeAdapter


        // ============================================================
        // DISTRICT + THANA
        // ============================================================
        //
        // IMPORTANT:
        // NO HARDCODED DISTRICT LIST.
        //
        // Districts come from Room.
        // Room receives them from Laravel API.
        //
        // Therefore all districts are supported.
        // ============================================================

        lifecycleScope.launch {

            try {

                // ----------------------------------------------------
                // First try to synchronize metadata from Laravel.
                //
                // If internet is unavailable, this returns false,
                // but we can still use previously cached Room data.
                // ----------------------------------------------------

                repository.syncMetaData()

                // ----------------------------------------------------
                // Load ALL districts from Room.
                // ----------------------------------------------------

                val districts =
                    repository.getDistricts()

                if (districts.isEmpty()) {

                    Toast.makeText(
                        this@DonationActivity,
                        "District data is not available yet. Please connect to internet and try again.",
                        Toast.LENGTH_LONG
                    ).show()

                    return@launch
                }

                val districtNames =
                    districts.map {
                        it.name
                    }

                val districtAdapter =
                    ArrayAdapter(
                        this@DonationActivity,
                        android.R.layout.simple_spinner_item,
                        districtNames
                    )

                districtAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
                )

                spDistrict.adapter =
                    districtAdapter


                // ----------------------------------------------------
                // DISTRICT SELECTION
                // ----------------------------------------------------

                spDistrict.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            if (
                                position < 0 ||
                                position >= districts.size
                            ) {
                                return
                            }

                            val selectedDistrict =
                                districts[position]

                            val districtId =
                                selectedDistrict.id

                            // ----------------------------------------
                            // Load ONLY Thanas belonging to
                            // selected district.
                            // ----------------------------------------

                            lifecycleScope.launch {

                                try {

                                    val thanas =
                                        repository
                                            .getThanasByDistrict(
                                                districtId
                                            )

                                    val thanaNames =
                                        thanas.map {
                                            it.name
                                        }

                                    val thanaAdapter =
                                        ArrayAdapter(
                                            this@DonationActivity,
                                            android.R.layout.simple_spinner_item,
                                            thanaNames
                                        )

                                    thanaAdapter
                                        .setDropDownViewResource(
                                            android.R.layout
                                                .simple_spinner_dropdown_item
                                        )

                                    spThana.adapter =
                                        thanaAdapter

                                } catch (e: Exception) {

                                    e.printStackTrace()

                                    Toast.makeText(
                                        this@DonationActivity,
                                        "Could not load Thanas",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onNothingSelected(
                            parent: AdapterView<*>?
                        ) {
                        }
                    }

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@DonationActivity,
                    "Could not load district information",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        // ============================================================
        // SAVE DONATION
        // ============================================================

        btnSave.setOnClickListener {

            val name =
                etDonorName.text
                    .toString()
                    .trim()

            val mobile =
                etMobile.text
                    .toString()
                    .trim()

            val amountString =
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

            val devId =
                etDevId.text
                    .toString()
                    .trim()

            val depositDate =
                etDepositDate.text
                    .toString()
                    .trim()


            // ========================================================
            // VALIDATION
            // ========================================================

            if (name.isEmpty()) {

                etDonorName.error =
                    "Donor name is required"

                etDonorName.requestFocus()

                return@setOnClickListener
            }


            if (mobile.isEmpty()) {

                etMobile.error =
                    "Mobile number is required"

                etMobile.requestFocus()

                return@setOnClickListener
            }


            if (amountString.isEmpty()) {

                etAmount.error =
                    "Amount is required"

                etAmount.requestFocus()

                return@setOnClickListener
            }


            val amount =
                amountString.toDoubleOrNull()

            if (
                amount == null ||
                amount <= 0
            ) {

                etAmount.error =
                    "Enter a valid amount"

                etAmount.requestFocus()

                return@setOnClickListener
            }


            // ========================================================
            // SELECTED DISTRICT
            // ========================================================

            val selectedDistrict =
                spDistrict.selectedItem
                    ?.toString()
                    ?: ""


            // ========================================================
            // SELECTED THANA
            // ========================================================

            val selectedThana =
                spThana.selectedItem
                    ?.toString()
                    ?: ""


            // ========================================================
            // SELECTED PURPOSE
            // ========================================================

            val selectedPurpose =
                spPurpose.selectedItem
                    ?.toString()
                    ?: "সাধারণ প্রণামী"


            // ========================================================
            // DONATION TYPE
            // ========================================================

            val donationType =
                if (
                    rgDonationType.checkedRadioButtonId
                    == R.id.rbBank
                ) {
                    "bank"
                } else {
                    "cash"
                }


            // ========================================================
            // GET DISTRICT ID
            // ========================================================
            //
            // Instead of a hardcoded:
            //
            // "Dhaka" -> 1
            // "Rajshahi" -> 2
            //
            // we search the actual Room district record.
            // ========================================================

            lifecycleScope.launch {

                try {

                    val districts =
                        repository.getDistricts()

                    val selectedDistrictEntity =
                        districts.firstOrNull {
                            it.name == selectedDistrict
                        }

                    if (
                        selectedDistrictEntity == null
                    ) {

                        Toast.makeText(
                            this@DonationActivity,
                            "Please select a valid district",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@launch
                    }

                    val districtId =
                        selectedDistrictEntity.id


                    // =================================================
                    // CREATE ROOM ENTITY
                    // =================================================

                    val donation =
                        DonationEntity(

                            devId =
                                devId.ifEmpty {
                                    null
                                },

                            donorName =
                                name,

                            donorMobile =
                                mobile,

                            amount =
                                amount,

                            purpose =
                                selectedPurpose,

                            donationType =
                                donationType,

                            districtId =
                                districtId,

                            districtName =
                                selectedDistrict,

                            thana =
                                selectedThana,

                            address =
                                address.ifEmpty {
                                    null
                                },

                            counsellors =
                                null,

                            depositDate =
                                depositDate,

                            note =
                                note.ifEmpty {
                                    null
                                },

                            country =
                                null,

                            city =
                                null,

                            foreignMobile =
                                null,

                            // Normal donation
                            promiseToken =
                                0,

                            processCommittedWithSms =
                                null,

                            // Not synchronized yet
                            isSynced =
                                false
                        )


                    // =================================================
                    // SAVE LOCALLY
                    // =================================================

                    repository.saveDonation(
                        donation
                    )

                    repository.scheduleSync()


                    Toast.makeText(
                        this@DonationActivity,
                        "Donation saved. Sync will happen automatically.",
                        Toast.LENGTH_LONG
                    ).show()


                    // =================================================
                    // CLEAR FORM
                    // =================================================

                    etDevId.text.clear()

                    etDonorName.text.clear()

                    etMobile.text.clear()

                    etAmount.text.clear()

                    etAddress.text.clear()

                    etNote.text.clear()

                } catch (e: Exception) {

                    e.printStackTrace()

                    Toast.makeText(
                        this@DonationActivity,
                        "Could not save donation: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
