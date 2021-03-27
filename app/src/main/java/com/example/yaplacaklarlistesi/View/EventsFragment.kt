package com.example.yaplacaklarlistesi.View

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yaplacaklarlistesi.Adapter.EventsAdapter
import com.example.yaplacaklarlistesi.R
import com.example.yaplacaklarlistesi.ViewModel.EventsViewModel
import com.example.yaplacaklarlistesi.databinding.FragmentEventsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventsFragment : Fragment(R.layout.fragment_events) {

    private lateinit var binding: FragmentEventsBinding
    private val args: EventsFragmentArgs by navArgs()
    private val viewModel: EventsViewModel by viewModels()
    private val adapter = EventsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventsBinding.bind(view)

        (activity as AppCompatActivity).setSupportActionBar(binding.fragmentEventsToolbar)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.fragmentEventsToolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })
        binding.fragmentEventsToolbar.setTitle(R.string.events)

        viewModel.searchQuery.value = ""

        viewModel.getEvents(args.LocalDate, args.Hour)
        observeViewModel()
        setupRecyclerView()

    }

    private fun observeViewModel() {
        viewModel.list?.observe(viewLifecycleOwner, Observer {
            binding.fragmentEventsProgress.visibility = View.GONE
            adapter.events = it
        })
        viewModel.title.observe(viewLifecycleOwner, Observer {
            binding.fragmentEventsToolbar.subtitle = it
        })
    }

    private fun setupRecyclerView() {
        binding.apply {
            fragmentEventsRv.layoutManager = LinearLayoutManager(requireContext())
            fragmentEventsRv.adapter = adapter
        }
        adapter.setItemCheckListener { position, checked ->
            viewModel.completeEvent(position, checked)
        }
        adapter.setItemClickListener { uid ->
            findNavController().navigate(EventsFragmentDirections.actionEventsFragmentToEventDetailFragment(uid))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.events_menu, menu)

        val hideItem = menu.findItem(R.id.hideCompleteEvents)
        hideItem.isChecked = viewModel.hideCompletes.value

        val searchItem = menu.findItem(R.id.searchEvents)
        val searchView = searchItem.actionView as SearchView

        var job: Job? = null
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                job?.cancel()
                if (!newText.isNullOrEmpty()) {
                    job = MainScope().launch {
                        delay(1000)
                        viewModel.searchQuery.value = newText
                    }
                }else {
                    viewModel.searchQuery.value = ""
                }
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.hideCompleteEvents -> {
                item.isChecked = !item.isChecked
                viewModel.hideCompletes.value = item.isChecked
            }
            R.id.deleteCompleteEvents -> {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Tamamlanmış bütün görevleri silmek istediğine emin misin?")
                builder.setPositiveButton("Evet", object: DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        viewModel.deleteAllCompleteEvents(args.LocalDate, args.Hour)
                    }
                }).setNegativeButton("Hayır", null)
                builder.create().show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

}