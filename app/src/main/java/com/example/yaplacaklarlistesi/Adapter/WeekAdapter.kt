package com.example.yaplacaklarlistesi.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yaplacaklarlistesi.Model.Date
import com.example.yaplacaklarlistesi.R

class WeekAdapter : RecyclerView.Adapter<WeekAdapter.ViewHolder>() {

    private val hours = arrayListOf(
        "00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
        "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
        "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
        "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
    )

    private var onItemClickListener: (((Int), (Int)) -> Unit)? = null
    private val list = ArrayList<Date>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_week, parent, false)
        return ViewHolder(view)
    }

    fun setItemClickListener(listener: ((Int), (Int)) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: WeekAdapter.ViewHolder, position: Int) {
        holder.hourText.text = hours.get(position)

        val marks = arrayListOf(
            holder.mark1.also { it.visibility = View.GONE },
            holder.mark2.also { it.visibility = View.GONE },
            holder.mark3.also { it.visibility = View.GONE },
            holder.mark4.also { it.visibility = View.GONE },
            holder.mark5.also { it.visibility = View.GONE },
            holder.mark6.also { it.visibility = View.GONE },
            holder.mark7.also { it.visibility = View.GONE }
        )
        var i = 0
        for (data in list) {
            data.times?.let {
                if (it.contains(position)) {
                    marks.get(i).visibility = View.VISIBLE
                }
            }
            i++
        }

        holder.dayBox1.setOnClickListener {
            onItemClickListener?.let {
                it(0, position)
            }
        }
        holder.dayBox2.setOnClickListener {
            onItemClickListener?.let {
                it(1, position)
            }
        }
        holder.dayBox3.setOnClickListener {
            onItemClickListener?.let {
                it(2, position)
            }
        }
        holder.dayBox4.setOnClickListener {
            onItemClickListener?.let {
                it(3, position)
            }
        }
        holder.dayBox5.setOnClickListener {
            onItemClickListener?.let {
                it(4, position)
            }
        }
        holder.dayBox6.setOnClickListener {
            onItemClickListener?.let {
                it(5, position)
            }
        }
        holder.dayBox7.setOnClickListener {
            onItemClickListener?.let {
                it(6, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return 24
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var hourText: TextView
        var dayBox1: LinearLayout
        var dayBox2: LinearLayout
        var dayBox3: LinearLayout
        var dayBox4: LinearLayout
        var dayBox5: LinearLayout
        var dayBox6: LinearLayout
        var dayBox7: LinearLayout
        var mark1: View
        var mark2: View
        var mark3: View
        var mark4: View
        var mark5: View
        var mark6: View
        var mark7: View

        init {
            hourText = view.findViewById(R.id.row_week_hour)
            dayBox1 = view.findViewById(R.id.row_week_day_box_1)
            dayBox2 = view.findViewById(R.id.row_week_day_box_2)
            dayBox3 = view.findViewById(R.id.row_week_day_box_3)
            dayBox4 = view.findViewById(R.id.row_week_day_box_4)
            dayBox5 = view.findViewById(R.id.row_week_day_box_5)
            dayBox6 = view.findViewById(R.id.row_week_day_box_6)
            dayBox7 = view.findViewById(R.id.row_week_day_box_7)
            mark1 = view.findViewById(R.id.row_week_box_mark_1)
            mark2 = view.findViewById(R.id.row_week_box_mark_2)
            mark3 = view.findViewById(R.id.row_week_box_mark_3)
            mark4 = view.findViewById(R.id.row_week_box_mark_4)
            mark5 = view.findViewById(R.id.row_week_box_mark_5)
            mark6 = view.findViewById(R.id.row_week_box_mark_6)
            mark7 = view.findViewById(R.id.row_week_box_mark_7)
        }
    }

    fun updateList(newList: ArrayList<Date>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}