package com.example.yaplacaklarlistesi.View

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaplacaklarlistesi.Adapter.WeekAdapter
import com.example.yaplacaklarlistesi.Model.Date
import com.example.yaplacaklarlistesi.R
import com.example.yaplacaklarlistesi.ViewModel.WeekViewModel
import com.example.yaplacaklarlistesi.databinding.FragmentWeekBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalTime

@AndroidEntryPoint
class WeekFragment : Fragment(R.layout.fragment_week) {

    lateinit var binding: FragmentWeekBinding
    private val viewModel: WeekViewModel by viewModels()
    private val adapter = WeekAdapter()
    private var list = ArrayList<Date>()
    private lateinit var baseFragment: BaseFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeekBinding.bind(view)
        binding.fragmentWeekPrevious.setOnClickListener {
            viewModel.previousWeek()
        }
        binding.fragmentWeekNext.setOnClickListener {
            viewModel.nextWeek()
        }
        viewModel.updateData()

        baseFragment = parentFragment as BaseFragment
        observeViewModel()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.apply {
            fragmentWeekRv.layoutManager = LinearLayoutManager(requireContext())
            fragmentWeekRv.adapter = adapter
        }
        adapter.setItemClickListener { day, hour ->

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("${list.get(day).day}.${list.get(day).month}.${list.get(day).year} | ${hour}:00")
            builder.setItems(R.array.dialog, object: DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, position: Int) {
                    val date: Long = LocalDate.of(list.get(day).year, list.get(day).month, list.get(day).day).toEpochDay()
                    val time: Int = LocalTime.of(hour, 0).toSecondOfDay()
                    when (position) {
                        0 -> {
                            baseFragment.findNavController().navigate(BaseFragmentDirections.actionBaseFragmentToEventsFragment(date, hour))
                        }
                        1 -> {
                            baseFragment.findNavController().navigate(BaseFragmentDirections.actionBaseFragmentToAddEventFragment(date, time))
                        }
                    }
                }
            })
            builder.create().show()
        }
    }

    private fun observeViewModel() {
        viewModel.list.observe(viewLifecycleOwner, Observer {
            list = it
            updateDayTexts()
            adapter.updateList(it)
        })
        viewModel.title.observe(viewLifecycleOwner, Observer {
            binding.fragmentWeekTitle.text = it
        })
    }

    private fun updateDayTexts() {
        binding.fragmentWeekDay1.text = list.get(0).day.toString()
        binding.fragmentWeekDay2.text = list.get(1).day.toString()
        binding.fragmentWeekDay3.text = list.get(2).day.toString()
        binding.fragmentWeekDay4.text = list.get(3).day.toString()
        binding.fragmentWeekDay5.text = list.get(4).day.toString()
        binding.fragmentWeekDay6.text = list.get(5).day.toString()
        binding.fragmentWeekDay7.text = list.get(6).day.toString()
    }

}