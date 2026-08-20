package com.matchlessgiftikd.donation

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
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

        // ---------------------------------------------------------
        // Views
        // ---------------------------------------------------------

        val etDevId = findViewById<EditText>(R.id.etDevId)
        val etDonorName = findViewById<EditText>(R.id.etDonorName)
        val etMobile = findViewById<EditText>(R.id.etMobile)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val etNote = findViewById<EditText>(R.id.etNote)
        val etDepositDate = findViewById<EditText>(R.id.etDepositDate)

        val spDistrict = findViewById<Spinner>(R.id.spDistrict)
        val spThana = findViewById<Spinner>(R.id.spThana)
        val spPurpose = findViewById<Spinner>(R.id.spPurpose)

        val rgDonationType =
            findViewById<RadioGroup>(R.id.rgDonationType)

        val btnSave =
            findViewById<Button>(R.id.btnSave)

        // ---------------------------------------------------------
        // DATE
        // ---------------------------------------------------------

        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.US)

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

        // ---------------------------------------------------------
        // PURPOSE
        // ---------------------------------------------------------

        val purposes = listOf(
            "সাধারণ প্রণামী",
            "ভোগ প্রণামী",
            "অন্যান্য প্রনামী"
        )

        val purposeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            purposes
        )

        purposeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spPurpose.adapter = purposeAdapter

        // ---------------------------------------------------------
        // DISTRICT
        // ---------------------------------------------------------

        val districts = listOf(
            "Dhaka",
            "Rajshahi",
            "Chittagong",
            "Sylhet"
        )

        val districtAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            districts
        )

        districtAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spDistrict.adapter = districtAdapter

        // ---------------------------------------------------------
        // THANA
        // ---------------------------------------------------------

        val thanas = listOf(
            "Godagari",
            "Boalia",
            "Rajpara",
            "Motihar"
        )

        val thanaAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            thanas
        )

        thanaAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spThana.adapter = thanaAdapter

        // ---------------------------------------------------------
        // BACKGROUND SYNC
        // ---------------------------------------------------------

        lifecycleScope.launch {

            try {

                repository.syncMetaData()

                repository.scheduleSync()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }

        // ---------------------------------------------------------
        // SAVE DONATION
        // ---------------------------------------------------------

        btnSave.setOnClickListener {

            val name =
                etDonorName.text.toString().trim()

            val mobile =
                etMobile.text.toString().trim()

            val amountString =
                etAmount.text.toString().trim()

            val address =
                etAddress.text.toString().trim()

            val note =
                etNote.text.toString().trim()

            val devId =
                etDevId.text.toString().trim()

            val depositDate =
                etDepositDate.text.toString().trim()

            // -----------------------------------------------------
            // VALIDATION
            // -----------------------------------------------------

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

            if (amount == null || amount <= 0) {

                etAmount.error =
                    "Enter a valid amount"

                etAmount.requestFocus()

                return@setOnClickListener
            }

            // -----------------------------------------------------
            // SELECTED VALUES
            // -----------------------------------------------------

            val selectedDistrict =
                spDistrict.selectedItem?.toString()
                    ?: "Dhaka"

            val selectedThana =
                spThana.selectedItem?.toString()
                    ?: "Godagari"

            val selectedPurpose =
                spPurpose.selectedItem?.toString()
                    ?: "সাধারণ প্রণামী"

            val donationType =
                if (
                    rgDonationType.checkedRadioButtonId
                    == R.id.rbBank
                ) {
                    "bank"
                } else {
                    "cash"
                }

            // -----------------------------------------------------
            // LOCAL ROOM ENTITY
            // -----------------------------------------------------

            val donation = DonationEntity(

                devId =
                    devId.ifEmpty { null },

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
                    getDistrictId(selectedDistrict),

                districtName =
                    selectedDistrict,

                thana =
                    selectedThana,

                address =
                    address.ifEmpty { null },

                counsellors =
                    null,

                depositDate =
                    depositDate,

                note =
                    note.ifEmpty { null },

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

                // Not yet uploaded
                isSynced =
                    false
            )

            // -----------------------------------------------------
            // SAVE LOCALLY
            // -----------------------------------------------------

            lifecycleScope.launch {

                try {

                    repository.saveDonation(
                        donation
                    )

                    repository.scheduleSync()

                    Toast.makeText(
                        this@DonationActivity,
                        "Donation saved. Sync will happen automatically.",
                        Toast.LENGTH_LONG
                    ).show()

                    // Clear form
                    etDevId.text.clear()
                    etDonorName.text.clear()
                    etMobile.text.clear()
                    etAmount.text.clear()
                    etAddress.text.clear()
                    etNote.text.clear()

                } catch (e: Exception) {

                    Toast.makeText(
                        this@DonationActivity,
                        "Could not save donation: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // -------------------------------------------------------------
    // DISTRICT ID
    // -------------------------------------------------------------

    private fun getDistrictId(
        districtName: String
    ): Int {

        return when (districtName) {

            "Dhaka" -> 1

            "Rajshahi" -> 2

            "Chittagong" -> 3

            "Sylhet" -> 4

            else -> 1
        }
    }
}
