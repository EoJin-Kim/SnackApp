package com.ej.snackapp.fragment.bottom.snack.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ej.snackapp.MainActivity
import com.ej.snackapp.databinding.FragmentResultSnackBinding


class ResultSnackFragment : Fragment() {
    lateinit var resultSnackFragmentBinding : FragmentResultSnackBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        resultSnackFragmentBinding = FragmentResultSnackBinding.inflate(inflater)

        return resultSnackFragmentBinding.root
    }

    override fun onResume() {
        super.onResume()
        val act = activity as MainActivity
        val foodResultSb =StringBuilder()
        val drinkResultSb =StringBuilder()
        getResultSnack(foodResultSb,drinkResultSb)

        resultSnackFragmentBinding.foodType.text = act.nowFoodType
        resultSnackFragmentBinding.drinkType.text = act.nowDrinkType

        resultSnackFragmentBinding.foodResult.setText(foodResultSb.toString())
        resultSnackFragmentBinding.drinkResult.setText(drinkResultSb.toString())
    }

    fun getResultSnack(foodResultSb: StringBuilder, drinkResultSb : StringBuilder) {
        val act = activity as MainActivity

        val foodMap: MutableMap<String,Int> = mutableMapOf()
        val drinkMap: MutableMap<String,Int> = mutableMapOf()
        val userSnackInfoList = act.userSnackInfoList.value

        for(userSnackInfo in userSnackInfoList!!){
            val foodCnt = foodMap.getOrDefault(userSnackInfo.food,0)
            foodMap.set(userSnackInfo.food,foodCnt+1)

            val drinkCnt = drinkMap.getOrDefault(userSnackInfo.drink,0)
            drinkMap.set(userSnackInfo.drink,drinkCnt+1)
        }
        foodMap.forEach { food, count ->
            if(food=="" || food =="간식 선택") return@forEach
            foodResultSb.append("${food} : ${count}개\n")
        }

        drinkMap.forEach{ drink, count ->
            if(drink=="" || drink == "간식 선택") return@forEach
            drinkResultSb.append("${drink} : ${count}개\n")
        }

    }

}