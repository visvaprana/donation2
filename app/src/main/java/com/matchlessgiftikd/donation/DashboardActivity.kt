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

    private lateinit var tvPending: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        repository = DonationRepository(this)

        tvPending = findViewById(R.id.tvPending)

        val btnDonation =
            findViewById<Button>(R.id.btnDonation)

        val btnPromised =
            findViewById<Button>(R.id.btnPromised)

        val btnDonations =
            findViewById<Button>(R.id.btnDonations)

        val btnPromisedList =
            findViewById<Button>(R.id.btnPromisedList)

        // ----------------------------------------------------
        // NEW REGULAR DONATION
        // ----------------------------------------------------

        btnDonation.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }

        // ----------------------------------------------------
        // NEW PROMISED / COMMITTED DONATION
        // ----------------------------------------------------

        btnPromised.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    PromisedDonationActivity::class.java
                )
            )
        }

        // ----------------------------------------------------
        // REGULAR DONATION LIST
        // ----------------------------------------------------

        btnDonations.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DonationListActivity::class.java
                )
            )
        }

        // ----------------------------------------------------
        // PROMISED DONATION LIST
        // ----------------------------------------------------

        btnPromisedList.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    PromisedDonationListActivity::class.java
                )
            )
        }

        updatePendingCount()
    }

    override fun onResume() {
        super.onResume()

        updatePendingCount()
    }

    private fun updatePendingCount() {

        lifecycleScope.launch {

            try {

                val pending =
                    repository.getTotalUnsyncedCount()

                tvPending.text =
                    "Pending synchronization: $pending"

            } catch (e: Exception) {

                tvPending.text =
                    "Pending synchronization: --"
            }
        }
    }
}
