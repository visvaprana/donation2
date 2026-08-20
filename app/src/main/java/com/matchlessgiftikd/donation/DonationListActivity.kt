package com.matchlessgiftikd.donation

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.matchlessgiftikd.donation.data.remote.ApiService
import kotlinx.coroutines.launch

class DonationListActivity : AppCompatActivity() {

    private val api = ApiService.create()

    private lateinit var listContainer: LinearLayout
    private lateinit var tvTotal: TextView

    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_donation_list
        )

        listContainer =
            findViewById(R.id.listContainer)

        tvTotal =
            findViewById(R.id.tvTotal)

        val btnNext =
            findViewById<Button>(R.id.btnNext)

        val btnPrevious =
            findViewById<Button>(R.id.btnPrevious)

        loadPage()

        btnNext.setOnClickListener {

            currentPage++

            loadPage()
        }

        btnPrevious.setOnClickListener {

            if (currentPage > 1) {
                currentPage--
                loadPage()
            }
        }
    }

    private fun loadPage() {

        lifecycleScope.launch {

            try {

                val response =
                    api.fetchDonations(
                        page = currentPage
                    )

                if (!response.isSuccessful) {

                    Toast.makeText(
                        this@DonationListActivity,
                        "Server error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@launch
                }

                val body = response.body()

                if (body == null) {
                    return@launch
                }

                tvTotal.text =
                    "Total: ${body.totalAmount}"

                listContainer.removeAllViews()

                body.donations?.data?.forEach {

                    val text =
                        """
                        ${it.donarName ?: "Unknown"}
                        
                        Mobile: ${it.donarMobile ?: ""}
                        Amount: ${it.amount}
                        Type: ${it.type ?: ""}
                        Date: ${it.depositDate ?: ""}
                        """.trimIndent()

                    val item =
                        TextView(this@DonationListActivity)

                    item.text = text
                    item.textSize = 16f
                    item.setPadding(
                        12,
                        16,
                        12,
                        16
                    )

                    listContainer.addView(item)
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@DonationListActivity,
                    "Network unavailable",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
