package com.example.yaplacaklarlistesi.View

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yaplacaklarlistesi.Model.Event
import com.example.yaplacaklarlistesi.R
import com.example.yaplacaklarlistesi.ViewModel.EventDetailViewModel
import com.example.yaplacaklarlistesi.databinding.FragmentEventDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailFragment : Fragment(R.layout.fragment_event_detail) {

    private val viewModel: EventDetailViewModel by viewModels()
    private lateinit var binding: FragmentEventDetailBinding
    private val args: EventDetailFragmentArgs by navArgs()
    private var event: Event? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventDetailBinding.bind(view)

        (activity as AppCompatActivity).setSupportActionBar(binding.fragmentDetailToolbar)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.fragmentDetailToolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        viewModel.getEvent(args.uid)
        observeViewModel()

        binding.fragmentDetailComplete.setOnCheckedChangeListener { compoundButton, checked ->
            viewModel.completeEvent(checked)
        }

        binding.fragmentDetailDelete.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Emin misin?")
            builder.setMessage("Bu kaydı silmek istediğine emin misin?")
            builder.setPositiveButton("Evet", object: DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    viewModel.deleteEvent(args.uid)
                    activity?.onBackPressed()
                }
            })
            builder.setNegativeButton("Hayır", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            })
            builder.create().show()
        }
    }

    private fun observeViewModel() {
        viewModel.event?.observe(viewLifecycleOwner, Observer{
            binding.apply {
                event = it
                fragmentDetailType.text = it.type.title
                fragmentDetailComplete.isChecked = it.completed
                fragmentDetailTitle.text = it.title
                fragmentDetailDescription.text = it.description
                fragmentDetailDate.text = viewModel.makeDate(it.year, it.month, it.day)
                fragmentDetailTime.text = viewModel.makeTime(it.hour, it.minute) ?: "Belirtilmedi"
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.event_detail_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        event?.let {
            findNavController().navigate(EventDetailFragmentDirections.actionEventDetailFragmentToAddEventFragment(event = it))
        }

        return super.onOptionsItemSelected(item)
    }

}