package com.matchlessgiftikd.donation

import android.app.DatePickerDialog
import android.os.Bundle
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

    private lateinit var repository: DonationRepository

    private lateinit var etDevId: EditText
    private lateinit var etDonorName: EditText
    private lateinit var etMobile: EditText
    private lateinit var etAddress: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDepositDate: EditText
    private lateinit var etNote: EditText

    private lateinit var spDistrict: Spinner
    private lateinit var spThana: Spinner
    private lateinit var spPurpose: Spinner

    private lateinit var rgDonationType: RadioGroup
    private lateinit var btnSave: Button

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        repository = DonationRepository(this)

        // ------------------------------------------------------------
        // FIND VIEWS
        // ------------------------------------------------------------

        etDevId = findViewById(R.id.etDevId)
        etDonorName = findViewById(R.id.etDonorName)
        etMobile = findViewById(R.id.etMobile)
        etAddress = findViewById(R.id.etAddress)
        etAmount = findViewById(R.id.etAmount)
        etDepositDate = findViewById(R.id.etDepositDate)
        etNote = findViewById(R.id.etNote)

        spDistrict = findViewById(R.id.spDistrict)
        spThana = findViewById(R.id.spThana)
        spPurpose = findViewById(R.id.spPurpose)

        rgDonationType = findViewById(R.id.rgDonationType)
        btnSave = findViewById(R.id.btnSave)

        // ------------------------------------------------------------
        // DATE
        // ------------------------------------------------------------

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

        // ------------------------------------------------------------
        // PURPOSE
        // ------------------------------------------------------------

        val purposes = listOf(
            "সাধারণ প্রণামী",
            "ভোগ প্রণামী",
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

        spPurpose.adapter = purposeAdapter

        // ------------------------------------------------------------
        // DISTRICT
        // Temporary safe list.
        // We can connect the complete server metadata after build is
        // stable.
        // ------------------------------------------------------------

     val districts = repository.getDistricts()

        val districtAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                districts
            )

        districtAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spDistrict.adapter = districtAdapter

        // ------------------------------------------------------------
        // THANA
        // ------------------------------------------------------------

    val thanas = repository.getThanasByDistrict(districtId)

        val thanaAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                thanas
            )

        thanaAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spThana.adapter = thanaAdapter

        // ------------------------------------------------------------
        // BACKGROUND SYNC
        // ------------------------------------------------------------

        lifecycleScope.launch {

            try {
                repository.syncMetaData()
                repository.scheduleSync()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // ------------------------------------------------------------
        // SAVE DONATION
        // ------------------------------------------------------------

        btnSave.setOnClickListener {

            saveDonation()
        }
    }

    // ================================================================
    // SAVE
    // ================================================================

    private fun saveDonation() {

        val donorId =
            etDevId.text.toString().trim()

        val donorName =
            etDonorName.text.toString().trim()

        val mobile =
            etMobile.text.toString().trim()

        val address =
            etAddress.text.toString().trim()

        val amountText =
            etAmount.text.toString().trim()

        val note =
            etNote.text.toString().trim()

        val depositDate =
            etDepositDate.text.toString().trim()

        if (donorName.isEmpty()) {

            Toast.makeText(
                this,
                "Please enter donor name",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (mobile.isEmpty()) {

            Toast.makeText(
                this,
                "Please enter mobile number",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (amountText.isEmpty()) {

            Toast.makeText(
                this,
                "Please enter amount",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val amount =
            amountText.toDoubleOrNull()

        if (amount == null || amount <= 0) {

            Toast.makeText(
                this,
                "Please enter a valid amount",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val selectedDistrict =
            spDistrict.selectedItem?.toString()
                ?: ""

        val selectedThana =
            spThana.selectedItem?.toString()
                ?: ""

        val selectedPurpose =
            spPurpose.selectedItem?.toString()
                ?: ""

        val donationType =
            if (
                rgDonationType.checkedRadioButtonId
                == R.id.rbBank
            ) {
                "bank"
            } else {
                "cash"
            }

        val donation =
            DonationEntity(

                donorName = donorName,

                donorMobile = mobile,

                amount = amount,

                purpose = selectedPurpose,

                donationType = donationType,

                districtId = 1,

                districtName = selectedDistrict,

                thana = selectedThana,

                address = address,

                counsellors = null,

                depositDate = depositDate,

                note = if (donorId.isNotEmpty()) {
                    "Donor ID: $donorId\n$note"
                } else {
                    note
                },

                isSynced = false
            )

        lifecycleScope.launch {

            try {

                repository.saveDonation(donation)

                Toast.makeText(
                    this@MainActivity,
                    "Donation saved successfully.\nAuto synchronization queued.",
                    Toast.LENGTH_LONG
                ).show()

                clearForm()

            } catch (e: Exception) {

                e.printStackTrace()

                Toast.makeText(
                    this@MainActivity,
                    "Error saving donation: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // ================================================================
    // CLEAR FORM
    // ================================================================

    private fun clearForm() {

        etDevId.text.clear()
        etDonorName.text.clear()
        etMobile.text.clear()
        etAddress.text.clear()
        etAmount.text.clear()
        etNote.text.clear()

        etDepositDate.setText(
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            ).format(Calendar.getInstance().time)
        )
    }
}
