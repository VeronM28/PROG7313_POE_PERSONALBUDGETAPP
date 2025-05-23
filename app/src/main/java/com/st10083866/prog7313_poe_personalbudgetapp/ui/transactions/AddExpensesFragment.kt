package com.st10083866.prog7313_poe_personalbudgetapp.ui.transactions

import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import android.net.Uri
import android.provider.MediaStore
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.Timestamp
import com.google.type.Date
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentAddExpensesBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddExpensesFragment : Fragment() {

    private var _binding: FragmentAddExpensesBinding? = null
    private val binding get() = _binding!!

    private val transactionViewModel: TransactionViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    private var selectedImagePath: String? = null
    private var userId: Int = -1
    private var selectedCategoryId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt("USER_ID", -1) ?: -1

        createCategoryDropdown()
        createImageUpload()
        createDatePicker()
        createCheckboxes()
        createPaymentMethods()
        createSubmitButton()
    }

    private fun createPaymentMethods() {
        val methods = listOf("Cash", "Card", "EFT", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, methods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = adapter
    }

    private fun createCategoryDropdown() {
        categoryViewModel.getCategoriesForUser(userId.toString()).observe(viewLifecycleOwner) { categories ->
            if (categories.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No categories found", Toast.LENGTH_SHORT).show()
                return@observe
            }

            val categoryNames = categories.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.spinnerCategory.adapter = adapter
            selectedCategoryId = categories[0].id

            binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    selectedCategoryId = categories[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedCategoryId = null
                }
            }
        }
    }

    private fun createImageUpload() {
        binding.uploadReceiptCard.setOnClickListener {
            openGallery()
        }
    }

    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val path = getPathFromUri(it)
            if (path != null) {
                selectedImagePath = path
                val bitmap = BitmapFactory.decodeFile(path)
                binding.receiptPreview.setImageBitmap(bitmap)
            } else {
                Toast.makeText(requireContext(), "Unable to get image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createSubmitButton() {
        binding.btnAddEntry.setOnClickListener {
            val amount = binding.edtAmount.text.toString().toDoubleOrNull()
            val type = when {
                binding.chkIncome.isChecked -> "income"
                binding.chkExpense.isChecked -> "expense"
                else -> null
            }
            val dateStr = binding.edtDate.text.toString()
            val paymentMethod = binding.spinnerPaymentMethod.selectedItem.toString()

            if (amount == null || type == null || dateStr.isBlank() || selectedCategoryId == null) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dateMillis = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)?.time ?: 0L

            val transaction = Transaction(
                id = "", // Firestore will auto-generate ID unless you're assigning one
                userOwnerId = userId.toString(),
                amount = amount,
                type = type,
                categoryId = selectedCategoryId,
                date = Timestamp(java.util.Date(dateMillis)),
                paymentMethod = paymentMethod,
                uploadedPicturePath = selectedImagePath
            )

            transactionViewModel.insertTransaction(transaction) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Transaction added", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Failed to add transaction", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openGallery() {
        imagePicker.launch("image/*")
    }

    private fun createCheckboxes() {
        binding.chkIncome.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.chkExpense.isChecked = false
        }

        binding.chkExpense.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.chkIncome.isChecked = false
        }
    }

    private fun createDatePicker() {
        binding.edtDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val formatted = String.format("%04d-%02d-%02d", year, month + 1, day)
                    binding.edtDate.setText(formatted)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val path = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return path
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}