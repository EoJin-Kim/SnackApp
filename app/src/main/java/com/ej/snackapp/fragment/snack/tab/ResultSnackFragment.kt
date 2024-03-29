package com.ej.snackapp.fragment.snack.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ej.snackapp.MainActivity
import com.ej.snackapp.adapter.ResultSnackAdapter
import com.ej.snackapp.databinding.FragmentResultSnackBinding
import com.ej.snackapp.dto.ResultSnackDto
import com.ej.snackapp.dto.response.MemberSnackInfoDto
import com.ej.snackapp.enums.SnackType
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultSnackFragment : Fragment() {
    lateinit var binding: FragmentResultSnackBinding
    val act by lazy { activity as MainActivity }
    private val mainViewModel: MainViewModel by activityViewModels()

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
        setUiData()
    }

    private fun setUiData() {
        binding.foodType.text = mainViewModel.foodShopName
        binding.drinkType.text = mainViewModel.drinkShopName
        setSnackResultRecycler(SnackType.FOOD)
        setSnackResultRecycler(SnackType.DRINK)
        setTotalResult()
    }

    private fun setTotalResult() {
        val userPickInfoList = mainViewModel.userPickInfo.value!!
        var foodCnt = 0;
        var drinkCnt = 0;
        for (memberSnackInfo in userPickInfoList) {
            if (memberSnackInfo.food != "") {
                foodCnt++;
            }
            if (memberSnackInfo.drink != "") {
                drinkCnt++;
            }
        }
        binding.foodTotalCount.text = "총 ${foodCnt}명 주문"
        binding.drinkTotalCount.text = "총 ${drinkCnt}명 주문"
    }

    private fun setSnackResultRecycler(snackType: SnackType) {
        val resultSnackAdapter = ResultSnackAdapter()
        val userPickInfo = mainViewModel.userPickInfo.value
        if (snackType == SnackType.FOOD) {
            val resultSnackList = getRecyclerListData(userPickInfo!!, SnackType.FOOD)
            resultSnackAdapter.submitList(resultSnackList)
            binding.recyclerFood.apply {
                adapter = resultSnackAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        } else if (snackType == SnackType.DRINK) {
            val resultSnackList = getRecyclerListData(userPickInfo!!, SnackType.DRINK)
            resultSnackAdapter.submitList(resultSnackList)
            binding.recyclerDrink.apply {
                adapter = resultSnackAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun getRecyclerListData(
        userPickInfoList: MutableList<MemberSnackInfoDto>,
        snackType: SnackType
    ): MutableList<ResultSnackDto> {
        val result: MutableList<ResultSnackDto> = mutableListOf()
        val resultMap: MutableMap<String, Int> = mutableMapOf()

        for (memberSnackInfo in userPickInfoList) {
            if (snackType == SnackType.FOOD) {
                if (memberSnackInfo.food != "") {
                    resultMap.putIfAbsent(memberSnackInfo.food, 0)
                    resultMap[memberSnackInfo.food] = resultMap[memberSnackInfo.food]!! + 1
                }
            } else if (snackType == SnackType.DRINK) {
                if (memberSnackInfo.drink != "") {
                    resultMap.putIfAbsent(memberSnackInfo.drink, 0)
                    resultMap[memberSnackInfo.drink] = resultMap[memberSnackInfo.drink]!! + 1
                }
            }
        }
        resultMap.forEach { snack, count ->
            result.add(ResultSnackDto(snack, count))
        }
        return result
    }

    companion object {
        fun newInstance() = ResultSnackFragment()
    }
}