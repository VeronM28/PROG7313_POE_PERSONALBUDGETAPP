package com.st10083866.prog7313_poe_personalbudgetapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.st10083866.prog7313_poe_personalbudgetapp.databinding.FragmentHelpInfoBinding
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.InfoAdapter
import com.st10083866.prog7313_poe_personalbudgetapp.viewmodel.InfoItem


class HelpInfoFragment : Fragment() {

    private var _binding: FragmentHelpInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var infoAdapter: InfoAdapter

    private val budgetingList = listOf(
        InfoItem("Track Expenses", "Monitor your daily spending using a budgeting app."),
        InfoItem("Set Limits", "Create monthly limits for each spending category.")
    )

    private val savingList = listOf(
        InfoItem("Automate Savings", "Set up automatic transfers to your savings account."),
        InfoItem("Emergency Fund", "Build a 3–6 month emergency savings fund.")
    )

    private val spendingList = listOf(
        InfoItem("Needs vs Wants", "Prioritize essentials before luxury items."),
        InfoItem("Smart Shopping", "Use discounts, buy in bulk, and compare prices.")
    )

    private val planningList = listOf(
        InfoItem("Set Goals", "Define short and long-term financial goals."),
        InfoItem("Review Regularly", "Adjust your budget based on life changes.")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        infoAdapter = InfoAdapter(budgetingList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = infoAdapter

        val tabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Budgeting"))
        tabLayout.addTab(tabLayout.newTab().setText("Saving"))
        tabLayout.addTab(tabLayout.newTab().setText("Spending"))
        tabLayout.addTab(tabLayout.newTab().setText("Planning"))

        tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab) {
                val list = when (tab.position) {
                    0 -> budgetingList
                    1 -> savingList
                    2 -> spendingList
                    3 -> planningList
                    else -> budgetingList
                }
                infoAdapter.updateList(list)
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
