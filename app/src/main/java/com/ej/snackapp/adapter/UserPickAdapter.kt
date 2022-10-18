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
import com.ej.snackapp.dto.UserSnackInfoDto

class UserPickAdapter(
    private val onClick1: (UserSnackInfoDto, Int) -> Unit,
    private val onClick2: (UserSnackInfoDto, Int) -> Unit
)  : ListAdapter<UserSnackInfoDto,UserPickAdapter.UserPickViewHolder>(UserPickDiffCallback){


    class UserPickViewHolder(
        itemView : View,
        val onClick1: (UserSnackInfoDto, Int) -> Unit,
        val onClick2: (UserSnackInfoDto, Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView){

        private val usernameTextView : TextView = itemView.findViewById(R.id.snack_pick_name)
        private val foodPickBtn : Button = itemView.findViewById(R.id.pick_food_btn)
        private val drinkPickBtn : Button = itemView.findViewById(R.id.pick_drink_btn)
        private var currentPickUser : UserSnackInfoDto? = null

        init {
//            itemView.setOnClickListener {
////                currentPickUser?.let{
////                    onClick(it)
////                }
//                onClick1(currentPickUser!!)
//            }
            foodPickBtn.setOnClickListener {
                onClick1(currentPickUser!!, layoutPosition)
//                foodPickBtn.setText(onClick2())
            }
            drinkPickBtn.setOnClickListener {
                onClick2(currentPickUser!!,layoutPosition)
            }
        }

        fun bind(userSnackInfoDto: UserSnackInfoDto) {
            currentPickUser = userSnackInfoDto

            usernameTextView.text = userSnackInfoDto.name
            if(userSnackInfoDto.food=="간식 선택" || userSnackInfoDto.food == ""){
                foodPickBtn.setText("간식을 선택해주세요")
            }
            else{
                foodPickBtn.setText("${userSnackInfoDto.food}")
            }

            if (userSnackInfoDto.drink == "간식 선택" || userSnackInfoDto.drink == "") {
                drinkPickBtn.setText("음료를 선택해주세요")
            }
            else{
                drinkPickBtn.setText("${userSnackInfoDto.drink}")
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPickViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.snack_pick_row,parent,false)
        val holder = UserPickViewHolder(view,onClick1,onClick2)
        return holder
    }

    override fun onBindViewHolder(holder: UserPickViewHolder, position: Int) {
        val userSnackInfo = getItem(position)
        holder.bind(userSnackInfo)
    }
}

object UserPickDiffCallback : DiffUtil.ItemCallback<UserSnackInfoDto>(){
    override fun areItemsTheSame(oldItem: UserSnackInfoDto, newItem: UserSnackInfoDto): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UserSnackInfoDto, newItem: UserSnackInfoDto): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.food == newItem.food &&
                oldItem.drink== newItem.drink &&
                oldItem.drinkOption == newItem.drinkOption &&
                oldItem.foodOption == newItem.foodOption
    }
}