package com.ej.snackapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.R
import com.ej.snackapp.enums.SnackType
import com.ej.snackapp.dto.response.MemberSnackInfoDto

class UserPickAdapter(
    private val onClick: (MemberSnackInfoDto, SnackType) -> Unit,
)  : ListAdapter<MemberSnackInfoDto,UserPickAdapter.UserPickViewHolder>(UserPickDiffCallback){


    class UserPickViewHolder(
        itemView: View,
        val onClick: (MemberSnackInfoDto, SnackType) -> Unit,
    ) : RecyclerView.ViewHolder(itemView){

        private val usernameTextView : TextView = itemView.findViewById(R.id.snack_pick_name)
        private val foodPickBtn : Button = itemView.findViewById(R.id.pick_food_btn)
        private val drinkPickBtn : Button = itemView.findViewById(R.id.pick_drink_btn)
        private var currentPickUser : MemberSnackInfoDto? = null

        fun bind(memberSnackInfoDto: MemberSnackInfoDto) {
            currentPickUser = memberSnackInfoDto

            foodPickBtn.setOnClickListener {
                onClick(memberSnackInfoDto, SnackType.FOOD)
            }
            drinkPickBtn.setOnClickListener {
                onClick(memberSnackInfoDto, SnackType.DRINK)
            }

            usernameTextView.text = memberSnackInfoDto.name
            if(memberSnackInfoDto.food=="간식 선택" || memberSnackInfoDto.food == ""){
                foodPickBtn.setText("간식을 선택해주세요")
            }
            else{
                foodPickBtn.setText("${memberSnackInfoDto.food}")
            }

            if (memberSnackInfoDto.drink == "간식 선택" || memberSnackInfoDto.drink == "") {
                drinkPickBtn.setText("음료를 선택해주세요")
            }
            else{
                drinkPickBtn.setText("${memberSnackInfoDto.drink}")
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPickViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_snack_pick,parent,false)
        val holder = UserPickViewHolder(view,onClick)
        return holder
    }
    override fun onBindViewHolder(holder: UserPickViewHolder, position: Int) {
        val userSnackInfo = getItem(position)
        holder.bind(userSnackInfo)
    }
}

object UserPickDiffCallback : DiffUtil.ItemCallback<MemberSnackInfoDto>(){
    override fun areItemsTheSame(oldItem: MemberSnackInfoDto, newItem: MemberSnackInfoDto): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: MemberSnackInfoDto, newItem: MemberSnackInfoDto): Boolean {
        return oldItem.equals(newItem)
    }
}