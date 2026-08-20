package com.matchlessgiftikd.donation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.matchlessgiftikd.donation.repository.DonationRepository
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var repository: DonationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        repository = DonationRepository(this)

        val tvPending = findViewById<TextView>(R.id.tvPending)

        val btnDonation =
            findViewById<Button>(R.id.btnDonation)

        val btnPromised =
            findViewById<Button>(R.id.btnPromised)

        val btnDonations =
            findViewById<Button>(R.id.btnDonations)

        val btnPromisedList =
            findViewById<Button>(R.id.btnPromisedList)

        btnDonation.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }

        btnPromised.setOnClickListener {
            // We will connect the promised donation form here.
            // For now use the promised list screen.
            startActivity(
                Intent(
                    this,
                    PromisedDonationListActivity::class.java
                )
            )
        }

        btnDonations.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    DonationListActivity::class.java
                )
            )
        }

        btnPromisedList.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    PromisedDonationListActivity::class.java
                )
            )
        }

        lifecycleScope.launch {

            val pending =
                repository.getTotalUnsyncedCount()

            tvPending.text =
                "Pending synchronization: $pending"
        }
    }

    override fun onResume() {
        super.onResume()

        val tvPending =
            findViewById<TextView>(R.id.tvPending)

        lifecycleScope.launch {

            val pending =
                repository.getTotalUnsyncedCount()

            tvPending.text =
                "Pending synchronization: $pending"
        }
    }
}
