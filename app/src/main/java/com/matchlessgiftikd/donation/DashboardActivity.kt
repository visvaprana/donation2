package com.matchlessgiftikd.donation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)


        val donationButton = findViewById<Button>(R.id.btnDonation)
        val promisedDonationButton = findViewById<Button>(R.id.btnPromisedDonation)
        val donationsButton = findViewById<Button>(R.id.btnDonations)
        val promisedDonationsButton = findViewById<Button>(R.id.btnPromisedDonations)
        val donorHistoryButton = findViewById<Button>(R.id.btnDonorHistory)


        donationButton.setOnClickListener {
            startActivity(Intent(this, DonationActivity::class.java))
        }


        promisedDonationButton.setOnClickListener {
            startActivity(Intent(this, PromisedDonationActivity::class.java))
        }


        donationsButton.setOnClickListener {
            startActivity(Intent(this, DonationListActivity::class.java))
        }


        promisedDonationsButton.setOnClickListener {
            startActivity(Intent(this, PromisedDonationListActivity::class.java))
        }


        donorHistoryButton.setOnClickListener {
            startActivity(Intent(this, DonorHistoryActivity::class.java))
        }

    }
}
