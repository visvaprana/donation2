package com.matchlessgiftikd.donation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        findViewById<Button>(R.id.btnDonation).setOnClickListener {
            startActivity(
                Intent(this, DonationActivity::class.java)
            )
        }

        findViewById<Button>(R.id.btnPromisedDonation).setOnClickListener {
            startActivity(
                Intent(this, PromisedDonationActivity::class.java)
            )
        }

        findViewById<Button>(R.id.btnDonations).setOnClickListener {
            startActivity(
                Intent(this, DonationListActivity::class.java)
            )
        }

        findViewById<Button>(R.id.btnPromisedDonations).setOnClickListener {
            startActivity(
                Intent(this, PromisedDonationListActivity::class.java)
            )
        }

        findViewById<Button>(R.id.btnDonorHistory).setOnClickListener {
            startActivity(
                Intent(this, DonorHistoryActivity::class.java)
            )
        }
    }
}
