package com.example.yaplacaklarlistesi.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.yaplacaklarlistesi.Model.Date
import com.example.yaplacaklarlistesi.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import javax.inject.Inject

class MonthAdapter @Inject constructor(@ApplicationContext val context: Context) :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    private var onItemClickListener: ((Date) -> Unit)? = null
    private val list = ArrayList<Date?>()
    private val now = LocalDate.now()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_month, parent, false)
        return ViewHolder(view)
    }

    fun setItemClickListener(listener: (Date) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: MonthAdapter.ViewHolder, position: Int) {
        holder.apply {
            list.get(position)?.let { date ->
                layout.visibility = View.VISIBLE
                day.text = date.day.toString()
                if (checkToday(date)) {
                    day.setTextColor(Color.RED)
                }
                mark.isVisible = date.mark
                mark.setBackground(ContextCompat.getDrawable(context, checkComplete(date)))
                layout.setOnClickListener {
                    onItemClickListener?.let {
                        it(date)
                    }
                }
            } ?: kotlin.run {
                layout.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView
        val mark: ImageView
        val layout: ConstraintLayout

        init {
            day = view.findViewById(R.id.row_month_day)
            mark = view.findViewById(R.id.row_month_mark)
            layout = view.findViewById(R.id.row_month_layout)
        }
    }

    fun updateList(newList: ArrayList<Date?>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    private fun checkComplete(date: Date) : Int {
        return when (date.completed) {
            true -> R.drawable.circle2
            false -> R.drawable.circle
        }
    }

    private fun checkToday(date: Date): Boolean {
        return now.year == date.year && now.monthValue == date.month && now.dayOfMonth == date.day
    }

}