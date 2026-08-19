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

class MainActivity : AppCompatActivity() {

    private lateinit var repository: DonationRepository
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = DonationRepository(this)

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

        val rgDonationType = findViewById<RadioGroup>(R.id.rgDonationType)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Default Date Format YYYY-MM-DD
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        etDepositDate.setText(dateFormat.format(calendar.time))

        // Date Picker Modal
        etDepositDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    etDepositDate.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Static Purpose Options
        val defaultPurposes = listOf("সাধারণ প্রণামী", "ভোগ প্রণামী", "অন্যান্য প্রনামী")
        val purposeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, defaultPurposes)
        purposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPurpose.adapter = purposeAdapter

        // Static District & Thana Setup
        val districts = listOf("Dhaka", "Rajshahi", "Chittagong", "Sylhet")
        val distAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
        distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDistrict.adapter = distAdapter

        val thanas = listOf("Godagari", "Boalia", "Rajpara", "Motihar")
        val thanaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, thanas)
        thanaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spThana.adapter = thanaAdapter

        // Background sync schedule
        lifecycleScope.launch {
            try {
                repository.syncMetaData()
                repository.scheduleSync()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnSave.setOnClickListener {
            val name = etDonorName.text.toString().trim()
            val mobile = etMobile.text.toString().trim()
            val amountStr = etAmount.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val note = etNote.text.toString().trim()
            val depositDate = etDepositDate.text.toString().trim()

            if (name.isEmpty() || mobile.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please enter Name, Mobile and Amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedDistrict = spDistrict.selectedItem?.toString() ?: "Dhaka"
            val selectedThana = spThana.selectedItem?.toString() ?: "Godagari"
            val selectedPurpose = spPurpose.selectedItem?.toString() ?: "সাধারণ প্রণামী"

            val donationType = if (rgDonationType.checkedRadioButtonId == R.id.rbBank) "bank" else "cash"

            val donation = DonationEntity(
                donorName = name,
                donorMobile = mobile,
                amount = amountStr.toDouble(),
                purpose = selectedPurpose,
                donationType = donationType,
                districtId = 1,
                districtName = selectedDistrict,
                thana = selectedThana,
                address = address,
                counsellors = null,
                depositDate = depositDate,
                note = note,
                isSynced = false
            )

            lifecycleScope.launch {
                repository.saveDonation(donation)
                Toast.makeText(this@MainActivity, "Saved locally! Auto sync queued.", Toast.LENGTH_LONG).show()

                etDonorName.text.clear()
                etMobile.text.clear()
                etAmount.text.clear()
                etAddress.text.clear()
                etNote.text.clear()
            }
        }
    }
}
