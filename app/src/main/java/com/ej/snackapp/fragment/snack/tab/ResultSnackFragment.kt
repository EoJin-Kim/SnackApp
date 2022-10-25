package com.ej.snackapp.fragment.snack.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ej.snackapp.MainActivity
import com.ej.snackapp.databinding.FragmentResultSnackBinding
import com.ej.snackapp.enums.SnackType
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultSnackFragment : Fragment() {
    lateinit var binding : FragmentResultSnackBinding

    val act by lazy { activity as MainActivity }
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultSnackBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.shopDetailInfo.observe(viewLifecycleOwner){
            setUiData()
        }
    }

    private fun setUiData() {
        binding.foodType.text = mainViewModel.foodShopName
        binding.drinkType.text = mainViewModel.drinkShopName
        binding.foodResult.setText(getResultSnack(SnackType.FOOD))
        binding.drinkResult.setText(getResultSnack(SnackType.DRINK))
    }

    fun getResultSnack(snackType: SnackType) :String{

        val snackResultSb  = StringBuilder()
        val snackMap: MutableMap<String,Int> = mutableMapOf()
        val userSnackInfoList = mainViewModel.userPickInfo.value

        for(userSnackInfo in userSnackInfoList!!){
            var snackCnt = 0;
            if (snackType == SnackType.FOOD) {
                snackCnt = snackMap.getOrDefault(userSnackInfo.food,0)
                snackMap.set(userSnackInfo.food,snackCnt+1)
            }
            else if (snackType == SnackType.DRINK) {
                snackCnt = snackMap.getOrDefault(userSnackInfo.drink,0)
                snackMap.set(userSnackInfo.drink,snackCnt+1)
            }
        }
        snackMap.forEach { snack, count ->
            if(snack=="" || snack =="간식 선택") return@forEach
            snackResultSb.append("${snack} : ${count}개\n")
        }
        return snackResultSb.toString()
    }

    companion object{
        fun newInstance() = ResultSnackFragment()
    }
}