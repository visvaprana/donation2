package com.matchlessgift.donation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.matchlessgift.donation.data.local.DonationEntity
import com.matchlessgift.donation.repository.DonationRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var repository: DonationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = DonationRepository(this)

        val etDonorName = findViewById<EditText>(R.id.etDonorName)
        val etMobile = findViewById<EditText>(R.id.etMobile)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        // Sync Metadata when internet is available
        lifecycleScope.launch {
            repository.syncMetaData()
            repository.scheduleSync()
        }

        btnSave.setOnClickListener {
            val name = etDonorName.text.toString().trim()
            val mobile = etMobile.text.toString().trim()
            val amountStr = etAmount.text.toString().trim()

            if (name.isEmpty() || mobile.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val donation = DonationEntity(
                donorName = name,
                donorMobile = mobile,
                amount = amountStr.toDouble(),
                purpose = "General Donation",
                donationType = "cash",
                districtId = 1,
                districtName = "Dhaka",
                thana = "Dhanmondi",
                address = "Road 27",
                counsellors = null,
                depositDate = "2026-08-19",
                note = "Saved via mobile app",
                isSynced = false
            )

            lifecycleScope.launch {
                repository.saveDonation(donation)
                Toast.makeText(this@MainActivity, "Saved locally! Will auto sync when online.", Toast.LENGTH_LONG).show()
                etDonorName.text.clear()
                etMobile.text.clear()
                etAmount.text.clear()
            }
        }
    }
}