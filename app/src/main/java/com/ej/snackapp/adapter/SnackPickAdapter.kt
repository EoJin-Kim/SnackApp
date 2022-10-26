package com.ej.snackapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.R
import dagger.hilt.android.AndroidEntryPoint


class SnackPickAdapter (private val onClick: (String) -> Unit)
    : ListAdapter<String, SnackPickAdapter.SnackPickViewHolder>(SnackPickDiffCallback){
    class SnackPickViewHolder(
        itemView : View,
        val onClick: (String) -> Unit,
    ) : RecyclerView.ViewHolder(itemView){
        private val snackNameTextView : TextView = itemView.findViewById(R.id.one_name)
        private var snackName: String? = null
        init{
            itemView.setOnClickListener {
                onClick(snackName!!)
            }
        }
        fun bind(snackName : String){
            this.snackName = snackName
            snackNameTextView.text = snackName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnackPickViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row, parent, false)
        return SnackPickViewHolder(view, onClick)
    }
    override fun onBindViewHolder(holder: SnackPickViewHolder, position: Int) {
        val snackName = getItem(position)
        holder.bind(snackName)
    }
}

object SnackPickDiffCallback : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.equals(newItem)
    }
}