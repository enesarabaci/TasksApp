package com.example.yaplacaklarlistesi.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.yaplacaklarlistesi.Model.Type
import com.example.yaplacaklarlistesi.R

class EventTypesAdapter : RecyclerView.Adapter<EventTypesAdapter.ViewHolder>() {

    private var onItemClickListener: ((Type) -> Unit)? = null

    private val diffUtil = object: DiffUtil.ItemCallback<Type>() {
        override fun areItemsTheSame(oldItem: Type, newItem: Type): Boolean {
            return oldItem.uid == newItem.uid
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Type, newItem: Type): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var types: List<Type>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventTypesAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_event_types, parent, false)
        return ViewHolder(view)
    }

    fun setItemClickListener(listener: (Type) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: EventTypesAdapter.ViewHolder, position: Int) {
        types.get(position).apply {
            holder.color.setCardBackgroundColor(Color.parseColor(color))
            holder.name.text = title
        }
        holder.settings.setOnClickListener {
            onItemClickListener?.let {
                it(types.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return types.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var color: CardView
        var name: TextView
        var settings: ImageButton
        init {
            color = view.findViewById(R.id.row_event_type_color)
            name = view.findViewById(R.id.row_event_type_name)
            settings = view.findViewById(R.id.row_event_type_settings)
        }
    }

}