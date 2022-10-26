package com.ej.snackapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.R
import com.ej.snackapp.dto.ResultSnackDto
import com.ej.snackapp.dto.response.ShopInfoDto
import com.ej.snackapp.enums.SnackType

class ResultSnackAdapter :
    ListAdapter<ResultSnackDto, ResultSnackAdapter.ResultSnackViewHolder>(ResultSnackDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultSnackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_snack_result, parent, false)
        return ResultSnackAdapter.ResultSnackViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultSnackViewHolder, position: Int) {
        val resultSnackDto = getItem(position)
        holder.bind(resultSnackDto)
    }

    class ResultSnackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var resultSnackNameTextView: TextView =
            itemView.findViewById(R.id.result_snack_name)
        private var resultSnackCountTextView: TextView =
            itemView.findViewById(R.id.result_snack_count)
        private var resultSnackDto: ResultSnackDto? = null

        fun bind(resultSnackDto: ResultSnackDto) {
            this.resultSnackDto = resultSnackDto
            resultSnackNameTextView.text = resultSnackDto.snackName
            resultSnackCountTextView.text = "${resultSnackDto.snackCount}"
        }
    }
}
object ResultSnackDiffCallback : DiffUtil.ItemCallback<ResultSnackDto>() {
    override fun areItemsTheSame(oldItem: ResultSnackDto, newItem: ResultSnackDto): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: ResultSnackDto, newItem: ResultSnackDto): Boolean {
        return oldItem.equals(newItem)
    }
}