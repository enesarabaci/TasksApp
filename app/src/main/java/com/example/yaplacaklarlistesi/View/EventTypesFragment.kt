package com.example.yaplacaklarlistesi.View

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaplacaklarlistesi.Adapter.EventTypesAdapter
import com.example.yaplacaklarlistesi.R
import com.example.yaplacaklarlistesi.ViewModel.EventTypesViewModel
import com.example.yaplacaklarlistesi.databinding.FragmentEventTypesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventTypesFragment : Fragment(R.layout.fragment_event_types) {

    private lateinit var binding: FragmentEventTypesBinding
    private val viewModel: EventTypesViewModel by viewModels()
    private val adapter = EventTypesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventTypesBinding.bind(view)

        (activity as AppCompatActivity).setSupportActionBar(binding.fragmentEventTypesToolbar)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.fragmentEventTypesToolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        binding.fragmentEventTypesFab.setOnClickListener {
            findNavController().navigate(EventTypesFragmentDirections.actionEventTypesFragmentToTypeDialogFragment())
        }

        viewModel.getEventTypes()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.apply {
            fragmentEventTypesRv.layoutManager = LinearLayoutManager(requireContext())
            fragmentEventTypesRv.adapter = adapter
        }
        adapter.setItemClickListener { type ->
            val builder = AlertDialog.Builder(requireContext())
            builder.setItems(R.array.type_dialog, object: DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, position: Int) {
                    when (position) {
                        0 -> {
                            findNavController().navigate(EventTypesFragmentDirections.actionEventTypesFragmentToTypeDialogFragment(type))
                        }
                        1 -> {
                            val deleteBuilder = AlertDialog.Builder(requireContext())
                            deleteBuilder.setMessage("Bu görev tipini silmek istediğinize emin misiniz?")
                            deleteBuilder.setPositiveButton("Evet", object: DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    viewModel.deleteType(type)
                                }
                            }).setNegativeButton("Hayır", null)
                            deleteBuilder.create().show()
                        }
                    }
                }
            })
            builder.create().show()
        }
    }

    private fun observeViewModel() {
        viewModel.list?.observe(viewLifecycleOwner, Observer {
            binding.fragmentEventTypesProgress.visibility = View.GONE
            adapter.types = it
        })
    }

}