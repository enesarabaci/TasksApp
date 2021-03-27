package com.example.yaplacaklarlistesi.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.AsyncListUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yaplacaklarlistesi.Model.Date
import com.example.yaplacaklarlistesi.Model.Event
import com.example.yaplacaklarlistesi.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    private var onItemCheckListener: (((Int), (Boolean)) -> Unit)? = null
    private var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_events, parent, false)
        return ViewHolder(view)
    }

    fun setItemCheckListener(listener: ((Int), (Boolean)) -> Unit) {
        onItemCheckListener = listener
    }
    fun setItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.uid == newItem.uid
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return (oldItem == newItem)
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var events: List<Event>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onBindViewHolder(holder: EventsAdapter.ViewHolder, position: Int) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr"))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        events.get(position).apply {
            holder.type.setBackgroundColor(Color.parseColor("${type.color}"))
            holder.date.text = LocalDate.of(year, month, day).format(dateFormatter)
            if (hour != null && minute != null) {
                holder.time.text = LocalTime.of(hour!!, minute!!)?.format(timeFormatter)
            }
            holder.title.text = title
            holder.completed.isChecked = completed
        }

        holder.completed.setOnClickListener{
            events = events.apply {
                get(position).completed = holder.completed.isChecked
            }
            onItemCheckListener?.let {
                it(position, holder.completed.isChecked)
            }
        }
        holder.layout.setOnClickListener {
            onItemClickListener?.let {
                it(events.get(position).uid)
            }
        }

    }

    override fun getItemCount(): Int {
        return events.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val type: View
        val date: TextView
        val time: TextView
        val title: TextView
        val completed: CheckBox
        val layout: RelativeLayout

        init {
            type = view.findViewById(R.id.row_events_view)
            date = view.findViewById(R.id.row_events_date)
            time = view.findViewById(R.id.row_events_time)
            title = view.findViewById(R.id.row_events_title)
            completed = view.findViewById(R.id.row_events_checkbox)
            layout = view.findViewById(R.id.row_events)
        }
    }

}