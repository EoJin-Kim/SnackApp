package com.ej.snackapp.fragment.snack.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ej.snackapp.MainActivity
import com.ej.snackapp.databinding.FragmentResultSnackBinding
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultSnackFragment : Fragment() {
    lateinit var binding : FragmentResultSnackBinding

    val act by lazy { activity as MainActivity }
    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultSnackBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val foodResultSb =StringBuilder()
        val drinkResultSb =StringBuilder()
        getResultSnack(foodResultSb,drinkResultSb)

        binding.foodType.text = mainViewModel.foodShopDetailInfo.value!!.shopName
        binding.drinkType.text = mainViewModel.foodShopDetailInfo.value!!.shopName

        binding.foodResult.setText(foodResultSb.toString())
        binding.drinkResult.setText(drinkResultSb.toString())
    }

    fun getResultSnack(foodResultSb: StringBuilder, drinkResultSb : StringBuilder) {

        val foodMap: MutableMap<String,Int> = mutableMapOf()
        val drinkMap: MutableMap<String,Int> = mutableMapOf()
        val userSnackInfoList = mainViewModel.userPickInfo.value

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