package com.matchlessgiftikd.donation

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.matchlessgiftikd.donation.data.local.PromisedDonationEntity
import com.matchlessgiftikd.donation.repository.DonationRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PromisedDonationActivity : AppCompatActivity() {

    private lateinit var repository: DonationRepository

    private val calendar = Calendar.getInstance()

    private lateinit var spDistrict: Spinner
    private lateinit var spThana: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_promised_donation
        )

        repository = DonationRepository(this)

        // -----------------------------------------------------
        // VIEWS
        // -----------------------------------------------------

        val etDevId =
            findViewById<EditText>(R.id.etPromiseDevId)

        val etDonorName =
            findViewById<EditText>(R.id.etPromiseDonorName)

        val etMobile =
            findViewById<EditText>(R.id.etPromiseMobile)

        val etAddress =
            findViewById<EditText>(R.id.etPromiseAddress)

        val etAmount =
            findViewById<EditText>(R.id.etPromiseAmount)

        val etPayAmount =
            findViewById<EditText>(R.id.etPromisePayAmount)

        val etPromiseDate =
            findViewById<EditText>(R.id.etPromiseDate)

        val etNote =
            findViewById<EditText>(R.id.etPromiseNote)

        val spPurpose =
            findViewById<Spinner>(R.id.spPromisePurpose)

        spDistrict =
            findViewById(R.id.spPromiseDistrict)

        spThana =
            findViewById(R.id.spPromiseThana)

        val rgDonationType =
            findViewById<RadioGroup>(
                R.id.rgPromiseDonationType
            )

        val cbSendSms =
            findViewById<CheckBox>(
                R.id.cbPromiseSendSms
            )

        val btnSave =
            findViewById<Button>(
                R.id.btnSavePromise
            )

        // -----------------------------------------------------
        // DATE
        // -----------------------------------------------------

        val dateFormat =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            )

        etPromiseDate.setText(
            dateFormat.format(calendar.time)
        )

        etPromiseDate.setOnClickListener {

            DatePickerDialog(
                this,

                { _, year, month, day ->

                    calendar.set(
                        Calendar.YEAR,
                        year
                    )

                    calendar.set(
                        Calendar.MONTH,
                        month
                    )

                    calendar.set(
                        Calendar.DAY_OF_MONTH,
                        day
                    )

                    etPromiseDate.setText(
                        dateFormat.format(
                            calendar.time
                        )
                    )
                },

                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

            ).show()
        }

        // -----------------------------------------------------
        // PURPOSE
        // -----------------------------------------------------

        val purposes = listOf(
            "সাধারণ প্রণামী",
            "ভোগ প্রণামী",
            "অন্যান্য প্রনামী"
        )

        val purposeAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                purposes
            )

        purposeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spPurpose.adapter = purposeAdapter

        // -----------------------------------------------------
        // TEMPORARY DISTRICT / THANA
        //
        // We will replace these with Room/database data
        // tomorrow.
        // -----------------------------------------------------

        val districts = listOf(
            "Dhaka",
            "Rajshahi",
            "Chittagong",
            "Sylhet"
        )

        val districtAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                districts
            )

        districtAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spDistrict.adapter = districtAdapter

        val thanas = listOf(
            "Godagari",
            "Boalia",
            "Rajpara",
            "Motihar"
        )

        val thanaAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                thanas
            )

        thanaAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spThana.adapter = thanaAdapter

        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        btnSave.setOnClickListener {

            val name =
                etDonorName.text
                    .toString()
                    .trim()

            val mobile =
                etMobile.text
                    .toString()
                    .trim()

            val amountText =
                etAmount.text
                    .toString()
                    .trim()

            val payAmountText =
                etPayAmount.text
                    .toString()
                    .trim()

            // -------------------------------------------------
            // VALIDATION
            // -------------------------------------------------

            if (name.isEmpty()) {

                etDonorName.error =
                    "Donor name is required"

                etDonorName.requestFocus()

                return@setOnClickListener
            }

            if (mobile.isEmpty()) {

                etMobile.error =
                    "Mobile number is required"

                etMobile.requestFocus()

                return@setOnClickListener
            }

            if (amountText.isEmpty()) {

                etAmount.error =
                    "Committed amount is required"

                etAmount.requestFocus()

                return@setOnClickListener
            }

            val amount =
                amountText.toDoubleOrNull()

            if (amount == null || amount <= 0) {

                etAmount.error =
                    "Enter a valid amount"

                etAmount.requestFocus()

                return@setOnClickListener
            }

            val payAmount =
                if (payAmountText.isEmpty()) {

                    0.0

                } else {

                    payAmountText.toDoubleOrNull()
                        ?: -1.0
                }

            if (payAmount < 0) {

                etPayAmount.error =
                    "Enter a valid payment"

                etPayAmount.requestFocus()

                return@setOnClickListener
            }

            if (payAmount > amount) {

                etPayAmount.error =
                    "Payment cannot exceed committed amount"

                etPayAmount.requestFocus()

                return@setOnClickListener
            }

            // -------------------------------------------------
            // SELECTED VALUES
            // -------------------------------------------------

            val district =
                spDistrict.selectedItem
                    ?.toString()
                    ?: ""

            val thana =
                spThana.selectedItem
                    ?.toString()
                    ?: ""

            val purpose =
                spPurpose.selectedItem
                    ?.toString()
                    ?: "সাধারণ প্রণামী"

            val donationType =
                if (
                    rgDonationType.checkedRadioButtonId ==
                    R.id.rbPromiseBank
                ) {
                    "bank"
                } else {
                    "cash"
                }

            // -------------------------------------------------
            // ENTITY
            // -------------------------------------------------

            val promisedDonation =
                PromisedDonationEntity(

                    devId =
                        etDevId.text
                            .toString()
                            .trim()
                            .ifEmpty {
                                null
                            },

                    donorName =
                        name,

                    donorMobile =
                        mobile,

                    amount =
                        amount,

                    payAmount =
                        payAmount,

                    purpose =
                        purpose,

                    donationType =
                        donationType,

                    // Temporary until location system
                    // is replaced tomorrow.
                    districtId =
                        1,

                    districtName =
                        district,

                    thana =
                        thana,

                    address =
                        etAddress.text
                            .toString()
                            .trim()
                            .ifEmpty {
                                null
                            },

                    counsellors =
                        null,

                    promiseDate =
                        etPromiseDate.text
                            .toString()
                            .trim(),

                    note =
                        etNote.text
                            .toString()
                            .trim()
                            .ifEmpty {
                                null
                            },

                    country =
                        null,

                    city =
                        null,

                    foreignMobile =
                        null,

                    sendSms =
                        if (cbSendSms.isChecked) {
                            1
                        } else {
                            0
                        },

                    isSynced =
                        false
                )

            // -------------------------------------------------
            // SAVE LOCALLY
            // -------------------------------------------------

            lifecycleScope.launch {

                try {

                    repository.savePromisedDonation(
                        promisedDonation
                    )

                    Toast.makeText(
                        this@PromisedDonationActivity,
                        "Promised donation saved offline",
                        Toast.LENGTH_LONG
                    ).show()

                    // Clear form

                    etDevId.text.clear()
                    etDonorName.text.clear()
                    etMobile.text.clear()
                    etAddress.text.clear()
                    etAmount.text.clear()
                    etPayAmount.text.clear()
                    etNote.text.clear()

                    etDonorName.requestFocus()

                } catch (e: Exception) {

                    e.printStackTrace()

                    Toast.makeText(
                        this@PromisedDonationActivity,
                        "Could not save donation",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
