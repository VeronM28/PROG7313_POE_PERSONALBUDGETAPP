package com.st10083866.prog7313_poe_personalbudgetapp.ui.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.st10083866.prog7313_poe_personalbudgetapp.R
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.firebase.Timestamp
import com.st10083866.prog7313_poe_personalbudgetapp.data.entities.Transaction
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentEditSpendingBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.CategoryViewModel
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditSpendingFragment : Fragment() {
    private var _binding: FragmentEditSpendingBinding? = null
    private val binding get() = _binding!!

    private val transactionViewModel: TransactionViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    private var transactionId: Int? = -1
    private var selectedCategoryId: String? = null
    private var selectedImagePath: String? = null
    private var userId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditSpendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionId = arguments?.getInt("TRANSACTION_ID", -1) ?: -1

        userId = arguments?.getInt("USER_ID", -1) ?: -1

        if (transactionId == -1 || userId == -1) {
            Toast.makeText(requireContext(), "Missing transaction or user ID", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return
        }

        setupDatePicker()
        setupCheckBoxes()
        setupReceiptPicker()
        setupPaymentMethods()

        loadTransaction()
        setupUpdateButton()
        setupDeleteButton()
    }

    private fun loadTransaction() {
        transactionId?.let { transactionViewModel.fetchTransactionById(it) }
        transactionViewModel.transaction.observe(viewLifecycleOwner) { txn ->
            txn?.let { transactions ->
                binding.edtAmount.setText(transactions.amount.toString())
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(transactions.date.toDate())
                binding.edtDate.setText(formattedDate)
                selectedCategoryId = transactions.categoryId
                selectedImagePath = transactions.uploadedPicturePath

                if (transactions.type == "income") binding.checkIncome.isChecked = true
                else binding.checkExpense.isChecked = true

                val paymentOptions = resources.getStringArray(com.st10083866.prog7313_poe_personalbudgetapp.R.array.payment_methods)
                val paymentIndex = paymentOptions.indexOfFirst { it == transactions.paymentMethod }
                if (paymentIndex >= 0) binding.spinnerPaymentMethod.setSelection(paymentIndex)

                if (!transactions.uploadedPicturePath.isNullOrEmpty()) {
                    val bitmap = BitmapFactory.decodeFile(transactions.uploadedPicturePath)
                    binding.receiptPreview.setImageBitmap(bitmap)
                }
                loadCategories(transactions.categoryId)
            }
        }
    }

    private fun loadCategories(selectedId: String?) {
        categoryViewModel.getCategoriesForUser(userId.toString()).observe(viewLifecycleOwner) { categories ->
            val names = categories.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, names)
            binding.categoryDropdown.setAdapter(adapter)

            val selectedIndex = categories.indexOfFirst { it.id == selectedId }
            if (selectedIndex >= 0) {
                binding.categoryDropdown.setText(names[selectedIndex], false)
                selectedCategoryId = categories[selectedIndex].id
            }

            binding.categoryDropdown.setOnItemClickListener { _, _, pos, _ ->
                selectedCategoryId = categories[pos].id
            }
        }
    }

    private fun setupDatePicker() {
        binding.edtDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                val dateStr = String.format("%04d-%02d-%02d", y, m + 1, d)
                binding.edtDate.setText(dateStr)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setupCheckBoxes() {
        binding.checkIncome.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.checkExpense.isChecked = false
        }
        binding.checkExpense.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.checkIncome.isChecked = false
        }
    }

    private fun setupPaymentMethods() {
        val methods = listOf("Cash", "Card", "EFT", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, methods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = adapter
    }

    private fun setupReceiptPicker() {
        binding.uploadReceiptCard.setOnClickListener {
            imagePicker.launch("image/*")
        }
    }

    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val path = getPathFromUri(it)
            if (path != null) {
                selectedImagePath = path
                val bitmap = BitmapFactory.decodeFile(path)
                binding.receiptPreview.setImageBitmap(bitmap)
            }
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

    private fun setupUpdateButton() {
        binding.btnEditEntry.setOnClickListener {
            val amount = binding.edtAmount.text.toString().toDoubleOrNull()
            val type = when {
                binding.checkIncome.isChecked -> "income"
                binding.checkExpense.isChecked -> "expense"
                else -> null
            }
            val dateStr = binding.edtDate.text.toString()
            val paymentMethod = binding.spinnerPaymentMethod.selectedItem.toString()

            if (amount == null || type == null || selectedCategoryId == null || dateStr.isBlank()) {
                Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dateMillis = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)?.time ?: 0L

            val updatedTxn = Transaction(
                id = transactionId.toString(),
                userOwnerId = userId.toString(),
                amount = amount,
                type = type,
                categoryId = selectedCategoryId,
                date = Timestamp(java.util.Date(dateMillis)),
                paymentMethod = paymentMethod,
                uploadedPicturePath = selectedImagePath
            )

            transactionViewModel.updateTransaction(updatedTxn) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Transaction updated", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupDeleteButton() {
        binding.btnDelete.setOnClickListener {
            transactionId?.let { it1 ->
                transactionViewModel.deleteTransactionById(it1) { success ->
                    if (success) {
                        Toast.makeText(requireContext(), "Transaction deleted", Toast.LENGTH_SHORT).show()
                        requireActivity().supportFragmentManager.popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "Delete failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}