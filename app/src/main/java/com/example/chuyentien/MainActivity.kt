package com.example.chuyentien

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val amountEditText = findViewById<EditText>(R.id.amountEditText)
        val resultEditText = findViewById<EditText>(R.id.resultEditText)
        val fromCurrencySpinner = findViewById<Spinner>(R.id.fromCurrencySpinner)
        val toCurrencySpinner = findViewById<Spinner>(R.id.toCurrencySpinner)
        val exchangeRateTextView = findViewById<TextView>(R.id.exchangeRateTextView)
        val fromCurrencySymbol = findViewById<TextView>(R.id.fromCurrencySymbol)
        val toCurrencySymbol = findViewById<TextView>(R.id.toCurrencySymbol)

        val currencies = listOf(
            "United States - Dollar (USD)",
            "Euro - EUR",
            "British Pound - GBP",
            "Japanese Yen - JPY",
            "Vietnam - Dong (VND)"
        )

        val symbols = mapOf(
            "USD" to "$", "EUR" to "€", "GBP" to "£", "JPY" to "¥", "VND" to "đ"
        )

        val exchangeRates = mapOf(
            "USD" to 1.0,
            "EUR" to 0.85,
            "GBP" to 0.75,
            "JPY" to 110.0,
            "VND" to 23185.0
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromCurrencySpinner.adapter = adapter
        toCurrencySpinner.adapter = adapter

        fromCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                updateConversion(amountEditText, resultEditText, fromCurrencySpinner, toCurrencySpinner, exchangeRateTextView, symbols, exchangeRates, fromCurrencySymbol, toCurrencySymbol)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        toCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                updateConversion(amountEditText, resultEditText, fromCurrencySpinner, toCurrencySpinner, exchangeRateTextView, symbols, exchangeRates, fromCurrencySymbol, toCurrencySymbol)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        amountEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                updateConversion(amountEditText, resultEditText, fromCurrencySpinner, toCurrencySpinner, exchangeRateTextView, symbols, exchangeRates, fromCurrencySymbol, toCurrencySymbol)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun updateConversion(
        amountEditText: EditText,
        resultEditText: EditText,
        fromCurrencySpinner: Spinner,
        toCurrencySpinner: Spinner,
        exchangeRateTextView: TextView,
        symbols: Map<String, String>,
        exchangeRates: Map<String, Double>,
        fromCurrencySymbol: TextView,
        toCurrencySymbol: TextView
    ) {
        val fromCurrency = fromCurrencySpinner.selectedItem.toString().takeLast(4).trim('(', ')')
        val toCurrency = toCurrencySpinner.selectedItem.toString().takeLast(4).trim('(', ')')
        fromCurrencySymbol.text = symbols[fromCurrency] ?: "$"
        toCurrencySymbol.text = symbols[toCurrency] ?: "đ"

        val fromRate = exchangeRates[fromCurrency] ?: 1.0
        val toRate = exchangeRates[toCurrency] ?: 1.0
        val exchangeRate = toRate / fromRate
        exchangeRateTextView.text = "1 $fromCurrency = ${DecimalFormat("#,##0.00").format(exchangeRate)} $toCurrency"

        val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val convertedAmount = amount * exchangeRate
        resultEditText.setText(DecimalFormat("#,##0.00").format(convertedAmount))
    }
}
