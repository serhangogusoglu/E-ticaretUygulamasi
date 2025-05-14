package com.example.e_ticaretuygulamasi.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_ticaretuygulamasi.R
import com.example.e_ticaretuygulamasi.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding

    private lateinit var editTextName: EditText
    private lateinit var editTextSurname: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextCardNumber: EditText
    private lateinit var editTextExpiry: EditText
    private lateinit var editTextCVV: EditText
    private lateinit var buttonPay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        editTextName = binding.editTextName
        editTextSurname = binding.editTextSurname
        editTextAddress = binding.editTextAddress
        editTextExpiry = binding.editTextExpiry
        editTextCardNumber = binding.editTextCardNumber
        editTextCVV = binding.editTextCVV
        buttonPay = binding.buttonPay

        editTextCardNumber.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private val maxCardLength = 19 // 16 rakam + 3 boşluk

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (isFormatting) {
                    return
                }

                if (charSequence != null) {
                    val textWithoutSpaces = charSequence.toString().replace(" ", "")
                    if (textWithoutSpaces.length > 16) {
                        // Kullanıcının 16 rakamdan fazla girmesini engelle
                        editTextCardNumber.setText(charSequence.toString().substring(0, start))
                        editTextCardNumber.setSelection(start)
                        return
                    }

                    isFormatting = true
                    val formattedText = StringBuilder()
                    for (i in textWithoutSpaces.indices) {
                        formattedText.append(textWithoutSpaces[i])
                        if (i > 0 && (i + 1) % 4 == 0 && i < 15) {
                            formattedText.append(" ")
                        }
                    }

                    editTextCardNumber.setText(formattedText.toString())
                    editTextCardNumber.setSelection(formattedText.length)
                    isFormatting = false
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        editTextExpiry.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isFormatting) {
                    return
                }

                if (s != null) {
                    isFormatting = true
                    val text = s.toString().replace("/", "")
                    val formattedText = StringBuilder()

                    for (i in text.indices) {
                        formattedText.append(text[i])
                        if (i == 1 && formattedText.length == 2 && text.length > 1) {
                            formattedText.append("/")
                        }
                        if (formattedText.length > 5) {
                            break // Maksimum 5 karakter (MM/YY)
                        }
                    }

                    editTextExpiry.setText(formattedText.toString())
                    editTextExpiry.setSelection(formattedText.length)
                    isFormatting = false
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        buttonPay.setOnClickListener {

            val name = editTextName.text.toString().trim()
            val surname = editTextSurname.text.toString().trim()
            val address = editTextAddress.text.toString().trim()
            val cardNumber = editTextCardNumber.text.toString().trim()
            val expiry = editTextExpiry.text.toString().trim()
            val cvv = editTextCVV.text.toString().trim()

            if (name.isEmpty() || surname.isEmpty() || address.isEmpty() ||
                cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(this, getString(R.string.fillAllFields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Expiry format kontrolü
            val expiryRegex = "^(0[1-9]|1[0-2])/((2[5-9])|(3[0-9]))$".toRegex()
            if (!expiryRegex.matches(expiry)) {
                Toast.makeText(this, getString(R.string.expiryFormat), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cvv.length != 3 || !cvv.all { it.isDigit() }) {
                Toast.makeText(this, getString(R.string.cvvFormat), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val regex = "^(\\d{4} \\d{4} \\d{4} \\d{4})$".toRegex()
            if (!regex.matches(cardNumber)) {
                Toast.makeText(this, getString(R.string.cardNumberFormat), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, ConfirmOrder::class.java)
            startActivity(intent)
            finish()
        }
    }
}