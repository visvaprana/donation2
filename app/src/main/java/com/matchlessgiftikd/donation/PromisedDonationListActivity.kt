package com.matchlessgiftikd.donation

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.matchlessgiftikd.donation.data.remote.ApiService
import kotlinx.coroutines.launch

class PromisedDonationListActivity : AppCompatActivity() {

    private val api = ApiService.create()

    private lateinit var listContainer: LinearLayout
    private lateinit var tvTotals: TextView

    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_promised_donation_list
        )

        listContainer =
            findViewById(R.id.listContainer)

        tvTotals =
            findViewById(R.id.tvTotals)

        findViewById<Button>(
            R.id.btnNext
        ).setOnClickListener {

            currentPage++
            loadPage()
        }

        findViewById<Button>(
            R.id.btnPrevious
        ).setOnClickListener {

            if (currentPage > 1) {
                currentPage--
                loadPage()
            }
        }

        loadPage()
    }

    private fun loadPage() {

        lifecycleScope.launch {

            try {

                val response =
                    api.fetchPromisedDonations(
                        page = currentPage
                    )

                if (!response.isSuccessful) {

                    Toast.makeText(
                        this@PromisedDonationListActivity,
                        "Server error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@launch
                }

                val body = response.body()
                    ?: return@launch

                tvTotals.text =
                    """
                    Committed: ${body.totalAmount}
                    Paid: ${body.totalPaidOverall}
                    Due: ${body.totalDueOverall}
                    """.trimIndent()

                listContainer.removeAllViews()

                body.promises?.data?.forEach {

                    val item =
                        TextView(
                            this@PromisedDonationListActivity
                        )

                    item.text =
                        """
                        ${it.donarName ?: "Unknown"}
                        
                        Mobile: ${it.donarMobile ?: ""}
                        Committed: ${it.amount}
                        Paid: ${it.payAmount}
                        Type: ${it.type ?: ""}
                        Date: ${it.promiseDate ?: ""}
                        """.trimIndent()

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
                    this@PromisedDonationListActivity,
                    "Network unavailable",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
