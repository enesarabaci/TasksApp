package com.example.yaplacaklarlistesi.View

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yaplacaklarlistesi.Model.Type
import com.example.yaplacaklarlistesi.R
import com.example.yaplacaklarlistesi.ViewModel.AddEventViewModel
import com.example.yaplacaklarlistesi.databinding.FragmentAddEventBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AddEventFragment : Fragment(R.layout.fragment_add_event),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentAddEventBinding
    private val viewModel: AddEventViewModel by viewModels()
    private val args: AddEventFragmentArgs by navArgs()
    private var date: LocalDate? = null
    private var time: LocalTime? = null
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private val types = ArrayList<Type>()
    private var choosenType = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEventBinding.bind(view)

        (activity as AppCompatActivity).setSupportActionBar(binding.fragmentAddEventToolbar)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.fragmentAddEventToolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                activity?.onBackPressed()
            }
        })

        getArgs()
        observeViewModel()

        binding.fragmentAddEventDateEdit.setOnClickListener {
            dateButton()
        }
        binding.fragmentAddEventTimeEdit.setOnClickListener {
            timeButton()
        }
        binding.fragmentAddEvetTypeEdit.setOnClickListener {
            typeEdit()
        }

    }

    private fun observeViewModel() {
        viewModel.types.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (!it.isNullOrEmpty()) {
                types.clear()
                types.addAll(it)
            }
        })

    }

    fun dateButton() {
        date?.let {
            datePickerDialog =
                DatePickerDialog(requireContext(), this, it.year, it.monthValue - 1, it.dayOfMonth)
            datePickerDialog.show()
        }
    }

    fun timeButton() {
        time?.let {
            timePickerDialog = TimePickerDialog(requireContext(), this, it.hour, it.minute, true)
            timePickerDialog.show()
        } ?: kotlin.run {
            val localTime = LocalTime.now()
            timePickerDialog =
                TimePickerDialog(requireContext(), this, localTime.hour, localTime.minute, true)
            timePickerDialog.show()
        }
    }


    private fun getArgs() {
        args.event?.let {
            viewModel.event = args.event
            date = LocalDate.of(it.year, it.month, it.day)
            if (it.hour != null && it.minute != null) {
                time = LocalTime.of(it.hour!!, it.minute!!)
            }
            binding.fragmentAddEventTitleEdit.setText(it.title)
            binding.fragmentAddEventDescriptionEdit.setText(it.description)
            binding.fragmentAddEventCardview.setCardBackgroundColor(Color.parseColor(it.type.color))
            binding.fragmentAddEventTypeText.text = it.type.title
        } ?: kotlin.run {
            date = LocalDate.ofEpochDay(args.date)
            if (args.time != -1) {
                time = LocalTime.ofSecondOfDay(args.time.toLong())
            }
        }
        updateButtons()
    }

    private fun updateButtons() {
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr"))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        binding.fragmentAddEventDateEdit.text = date?.format(dateFormatter)
        time?.let {
            binding.fragmentAddEventTimeEdit.text = it.format(timeFormatter)
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        date = LocalDate.of(year, month + 1, day)
        updateButtons()
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        time = LocalTime.of(hour, minute)
        updateButtons()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_event_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        binding.apply {
            if (fragmentAddEventTitleEdit.text.toString()
                    .isNotEmpty() && date != null && types.isNotEmpty()
            ) {
                val title = fragmentAddEventTitleEdit.text.toString()
                var description: String? = null
                val type = types.get(choosenType)
                if (fragmentAddEventDescriptionEdit.text.toString().isNotEmpty()) {
                    description = fragmentAddEventDescriptionEdit.text.toString()
                }
                viewModel.saveData(title, description, date!!, time, type)

                if (viewModel.event != null) {
                    activity?.onBackPressed()
                }else {
                    val action = AddEventFragmentDirections.actionAddEventFragmentToBaseFragment()
                    findNavController().navigate(action)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Zorunlu alanları doldurmalısınız.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun typeEdit() {
        val list = viewModel.makeTypeList(types)
        val builder = AlertDialog.Builder(requireContext())
        builder.setItems(list, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, position: Int) {
                binding.fragmentAddEventCardview.setCardBackgroundColor(
                    Color.parseColor(
                        types.get(
                            position
                        ).color
                    )
                )
                binding.fragmentAddEventTypeText.text = types.get(position).title
                choosenType = position
            }
        })
        val dialog = builder.create()
        dialog.show()
    }

}