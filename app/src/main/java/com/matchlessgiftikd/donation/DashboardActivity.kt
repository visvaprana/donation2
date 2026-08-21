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

        // ========================================================
        // VIEWS
        // ========================================================

        tvPending =
            findViewById(R.id.tvPending)

        val btnDonation =
            findViewById<Button>(R.id.btnDonation)

        val btnPromisedDonation =
            findViewById<Button>(
                R.id.btnPromisedDonation
            )

        val btnDonations =
            findViewById<Button>(
                R.id.btnDonations
            )

        val btnPromisedDonations =
            findViewById<Button>(
                R.id.btnPromisedDonations
            )

        val btnDonorHistory =
            findViewById<Button>(
                R.id.btnDonorHistory
            )

        // ========================================================
        // NEW DONATION
        // ========================================================

        btnDonation.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }

        // ========================================================
        // NEW PROMISED DONATION
        // ========================================================

        btnPromisedDonation.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    PromisedDonationActivity::class.java
                )
            )
        }

        // ========================================================
        // DONATION LIST
        // ========================================================

        btnDonations.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DonationListActivity::class.java
                )
            )
        }

        // ========================================================
        // PROMISED DONATION LIST
        // ========================================================

        btnPromisedDonations.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    PromisedDonationListActivity::class.java
                )
            )
        }

        // ========================================================
        // DONOR HISTORY
        // ========================================================

        btnDonorHistory.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    DonorHistoryActivity::class.java
                )
            )
        }

        // ========================================================
        // INITIAL PENDING COUNT
        // ========================================================

        updatePendingCount()
    }

    // ============================================================
    // UPDATE PENDING SYNCHRONIZATION COUNT
    // ============================================================

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

    // ============================================================
    // REFRESH WHEN RETURNING TO DASHBOARD
    // ============================================================

    override fun onResume() {

        super.onResume()

        if (::repository.isInitialized) {
            updatePendingCount()
        }
    }
}
