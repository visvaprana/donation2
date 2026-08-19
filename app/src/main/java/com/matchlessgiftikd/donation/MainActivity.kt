package com.matchlessgiftikd.donation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
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

        // Default Purposes
        val defaultPurposes = listOf("সাধারণ প্রণামী", "ভোগ প্রণামী", "অন্যান্য প্রনামী")
        val purposeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, defaultPurposes)
        purposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPurpose.adapter = purposeAdapter

        // Sync Metadata on App Launch & Populate District Dropdown
        lifecycleScope.launch {
            repository.syncMetaData()
            repository.scheduleSync()

            val districtList = repository.getDistrictsFromDb()
            if (districtList.isNotEmpty()) {
                val distNames = districtList.map { it.name }
                val distAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, distNames)
                distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spDistrict.adapter = distAdapter

                // On District Select -> Load Thanas dynamically
                spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selectedDistrict = districtList[position]
                        lifecycleScope.launch {
                            val thanaList = repository.getThanasFromDb(selectedDistrict.id)
                            val thanaNames = thanaList.map { it.name }
                            val thanaAdapter = ArrayAdapter(
                                this@MainActivity,
                                android.R.layout.simple_spinner_item,
                                thanaNames
                            )
                            thanaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spThana.adapter = thanaAdapter
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
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
            val selectedThana = spThana.selectedItem?.toString() ?: "Default Thana"
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
