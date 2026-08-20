package com.matchlessgiftikd.donation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // ---------------------------------------------------------
        // Donation
        // ---------------------------------------------------------

        findViewById<Button>(R.id.btnDonation).setOnClickListener {

            startActivity(
                Intent(this, DonationActivity::class.java)
            )
        }

        // ---------------------------------------------------------
        // Promised Donation
        // ---------------------------------------------------------

        findViewById<Button>(R.id.btnPromisedDonation).setOnClickListener {

            startActivity(
                Intent(this, PromisedDonationActivity::class.java)
            )
        }

        // ---------------------------------------------------------
        // Donations List
        // ---------------------------------------------------------

        findViewById<Button>(R.id.btnDonations).setOnClickListener {

            startActivity(
                Intent(this, DonationsActivity::class.java)
            )
        }

        // ---------------------------------------------------------
        // Promised Donations List
        // ---------------------------------------------------------

        findViewById<Button>(R.id.btnPromisedDonations).setOnClickListener {

            startActivity(
                Intent(this, PromisedDonationsActivity::class.java)
            )
        }

        // ---------------------------------------------------------
        // Donor History
        // ---------------------------------------------------------

        findViewById<Button>(R.id.btnDonorHistory).setOnClickListener {

            startActivity(
                Intent(this, DonorHistoryActivity::class.java)
            )
        }
    }
}
