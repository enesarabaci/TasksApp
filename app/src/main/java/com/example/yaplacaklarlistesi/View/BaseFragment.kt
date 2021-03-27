package com.example.yaplacaklarlistesi.View

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.yaplacaklarlistesi.*
import com.example.yaplacaklarlistesi.Adapter.ViewPagerAdapter
import com.example.yaplacaklarlistesi.databinding.FragmentBaseBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseFragment : Fragment(R.layout.fragment_base) {

    lateinit var binding: FragmentBaseBinding
    lateinit var mainActivity: MainActivity
    private lateinit var adapter: ViewPagerAdapter
    lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBaseBinding.bind(view)

        (activity as AppCompatActivity).setSupportActionBar(binding.fragmentBaseToolbar)
        setHasOptionsMenu(true)

        setupAdapter()
        val navHostFragment: NavHostFragment = mainActivity.supportFragmentManager.findFragmentById(
            R.id.fragment
        ) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.fragmentBaseNavView.setupWithNavController(navController)

        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            binding.fragmentBaseToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

    private fun setupAdapter() {
        adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(MonthFragment(), "Ay")
        adapter.addFragment(WeekFragment(), "Hafta")
        binding.apply {
            fragmentBaseViewPager.adapter = adapter
            fragmentBaseTab.setupWithViewPager(fragmentBaseViewPager)
            fragmentBaseViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(fragmentBaseTab))
        }
    }

}