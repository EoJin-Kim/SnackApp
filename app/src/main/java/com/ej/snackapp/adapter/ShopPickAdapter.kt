package com.ej.snackapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.R
import com.ej.snackapp.dto.ShopInfoDto
import com.ej.snackapp.info.ShopInfo

class ShopPickAdapter(private val onClick: (ShopInfoDto) -> Unit)
    : ListAdapter<ShopInfoDto, ShopPickAdapter.ShopPickViewHolder>(ShopPickDiffCallback){

    class ShopPickViewHolder(
        itemView : View,
        val onClick: (ShopInfoDto) -> Unit,
    ) : RecyclerView.ViewHolder(itemView){

        private var shopNameTextView : TextView = itemView.findViewById(R.id.one_name)
        private var shopInfo: ShopInfoDto? = null

        init{
            itemView.setOnClickListener {
                onClick(shopInfo!!)
            }
        }

        fun bind(shopInfo : ShopInfoDto){
            this.shopInfo = shopInfo
            shopNameTextView.text = shopInfo.shopName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPickViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_row, parent, false)
        return ShopPickAdapter.ShopPickViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ShopPickViewHolder, position: Int) {
        val shopInfo = getItem(position)
        holder.bind(shopInfo)
    }
}


object ShopPickDiffCallback : DiffUtil.ItemCallback<ShopInfoDto>(){
    override fun areItemsTheSame(oldItem: ShopInfoDto, newItem: ShopInfoDto): Boolean {
        return oldItem == newItem
    }


    override fun areContentsTheSame(oldItem: ShopInfoDto, newItem: ShopInfoDto): Boolean {
        return oldItem.shopName == newItem.shopName
    }
}