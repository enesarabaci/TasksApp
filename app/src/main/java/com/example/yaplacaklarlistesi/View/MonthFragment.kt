package com.example.yaplacaklarlistesi.View

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.yaplacaklarlistesi.Adapter.MonthAdapter
import com.example.yaplacaklarlistesi.R
import com.example.yaplacaklarlistesi.ViewModel.MonthViewModel
import com.example.yaplacaklarlistesi.databinding.FragmentMonthBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class MonthFragment : Fragment(R.layout.fragment_month) {

    private lateinit var binding: FragmentMonthBinding
    private val viewModel: MonthViewModel by viewModels()
    private lateinit var baseFragment: BaseFragment

    @Inject
    lateinit var adapter: MonthAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMonthBinding.bind(view)
        binding.fragmentMonthNext.setOnClickListener {
            viewModel.nextMonth()
        }
        binding.fragmentMonthPrevious.setOnClickListener {
            viewModel.previousMonth()
        }
        baseFragment = parentFragment as BaseFragment

        viewModel.updateData()
        observeViewModel()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.apply {
            fragmentMonthRv.layoutManager = GridLayoutManager(requireContext(), 7)
            fragmentMonthRv.adapter = adapter
        }
        adapter.setItemClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("${it.day}.${it.month}.${it.year}")
            builder.setItems(R.array.dialog, object: DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, position: Int) {
                    val date: Long = LocalDate.of(it.year, it.month, it.day).toEpochDay()
                    when (position) {
                        0 -> {
                            baseFragment.findNavController().navigate(BaseFragmentDirections.actionBaseFragmentToEventsFragment(date))
                        }
                        1 -> {
                            baseFragment.findNavController().navigate(BaseFragmentDirections.actionBaseFragmentToAddEventFragment(date))
                        }
                    }
                }
            })
            builder.create().show()
        }
    }

    private fun observeViewModel() {
        viewModel.title.observe(viewLifecycleOwner, Observer {
            binding.fragmentMonthTitle.text = it
        })
        viewModel.list.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                adapter.updateList(it)
            }
        })
    }
}