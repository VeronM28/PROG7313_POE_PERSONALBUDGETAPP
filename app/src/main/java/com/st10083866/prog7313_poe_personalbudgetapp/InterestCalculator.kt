package com.st10083866.prog7313_poe_personalbudgetapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlin.math.pow

class InterestCalculatorFragment : Fragment() {

    private lateinit var etPrincipal: EditText
    private lateinit var etRate: EditText
    private lateinit var etTime: EditText
    private lateinit var etFrequency: EditText
    private lateinit var btnCalculate: Button
    private lateinit var btnReset: Button
    private lateinit var tvResult: TextView
    private lateinit var rbSimple: RadioButton
    private lateinit var rbCompound: RadioButton

    private val prefsName = "InterestPrefs"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate your fragment layout here
        return inflater.inflate(R.layout.fragment_interest_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Bind views on the inflated view
        etPrincipal   = view.findViewById(R.id.etPrincipal)
        etRate        = view.findViewById(R.id.etRate)
        etTime        = view.findViewById(R.id.etTime)
        etFrequency   = view.findViewById(R.id.etFrequency)
        btnCalculate  = view.findViewById(R.id.btnCalculate)
        btnReset      = view.findViewById(R.id.btnReset)
        tvResult      = view.findViewById(R.id.tvResult)
        rbSimple      = view.findViewById(R.id.rbSimple)
        rbCompound    = view.findViewById(R.id.rbCompound)

        // Load saved prefs
        val prefs = requireContext()
            .getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        etPrincipal.setText(prefs.getString("principal", ""))
        etRate.setText(prefs.getString("rate", ""))
        etTime.setText(prefs.getString("time", ""))
        etFrequency.setText(prefs.getString("frequency", ""))
        rbSimple.isChecked = prefs.getBoolean("simple", true)
        rbCompound.isChecked = !rbSimple.isChecked

        btnCalculate.setOnClickListener { calculateAndSave() }
        btnReset.setOnClickListener    { resetFields() }
    }

    private fun calculateAndSave() {
        val pTxt = etPrincipal.text.toString().trim()
        val rTxt = etRate.text.toString().trim()
        val tTxt = etTime.text.toString().trim()
        val fTxt = etFrequency.text.toString().trim()

        if (pTxt.isBlank() || rTxt.isBlank() || tTxt.isBlank() || fTxt.isBlank()) {
            Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val principal = pTxt.toDoubleOrNull()
        val rate      = rTxt.toDoubleOrNull()
        val time      = tTxt.toDoubleOrNull()
        val freq      = fTxt.toIntOrNull()

        if (principal == null || rate == null || time == null || freq == null || freq <= 0) {
            Toast.makeText(requireContext(), "Invalid input values.", Toast.LENGTH_SHORT).show()
            return
        }

        // Simple vs Compound
        val interest = if (rbSimple.isChecked) {
            principal * rate * time / 100
        } else {
            val amount = principal * (1 + rate / (freq * 100)).pow(freq * time)
            amount - principal
        }
        val total = principal + interest

        tvResult.text = "Interest: %.2f\nTotal Amount: %.2f".format(interest, total)

        // Save prefs
        requireContext()
            .getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            .edit().apply {
                putString("principal", pTxt)
                putString("rate", rTxt)
                putString("time", tTxt)
                putString("frequency", fTxt)
                putBoolean("simple", rbSimple.isChecked)
                apply()
            }
    }

    private fun resetFields() {
        etPrincipal.text.clear()
        etRate.text.clear()
        etTime.text.clear()
        etFrequency.text.clear()
        rbSimple.isChecked = true
        tvResult.text = "Interest:"

        requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE).edit().clear().apply()
    }
}
