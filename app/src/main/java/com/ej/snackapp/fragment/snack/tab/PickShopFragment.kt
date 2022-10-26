package com.ej.snackapp.fragment.snack.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.ej.snackapp.MainActivity
import com.ej.snackapp.databinding.FragmentPickShopBinding
import com.ej.snackapp.dto.response.ShopInfoDto
import com.ej.snackapp.enums.SnackType
import com.ej.snackapp.fragment.dialog.ShopSelectFragmentDialog
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickShopFragment : Fragment() {

    lateinit var binding: FragmentPickShopBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    val act by lazy { activity as MainActivity }

    lateinit var nowFoodTextView: TextView
    lateinit var nowDrinkTextView: TextView

    var foodShopInfo: ShopInfoDto? = null
    var drinkShopInfo: ShopInfoDto? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPickShopBinding.inflate(inflater)
        return binding.root
    }

    private fun shopSelectAlert(title: String, message: String, btnText: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(btnText) { diologInterface, i -> }
            show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nowFoodTextView = binding.selectFood
        nowDrinkTextView = binding.selectDrink

        binding.shopCompleteBtn.setOnClickListener {

            if (foodShopInfo == null || drinkShopInfo == null) {
                shopSelectAlert("가게를 선택해주세요", "간식과 음료 가게를 선택 해주세요!!", "확인")
            } else {
                mainViewModel.selectSnackShop(foodShopInfo!!, drinkShopInfo!!)
                shopSelectAlert("가게선택 완료", "가게선택 완료!!", "확인!")
            }
        }

        binding.foodShopBtn.setOnClickListener {
            createShopPickDialog(SnackType.FOOD)
        }

        binding.drinkShopBtn.setOnClickListener {
            createShopPickDialog(SnackType.DRINK)
        }
    }

    private fun createShopPickDialog(
        snackType: SnackType
    ) {
        val funPickShopVal: (ShopInfoDto, SnackType) -> Unit =
            { shopInfoDto, snackType -> dialogPickShop(shopInfoDto, snackType) }
        val dialog: ShopSelectFragmentDialog =
            ShopSelectFragmentDialog.newInstance(funPickShopVal, snackType)

        dialog.show(act.supportFragmentManager, "가게 선택")
    }

    private fun dialogPickShop(shopInfoDto: ShopInfoDto, snackType: SnackType) {
        if (snackType == SnackType.FOOD) {
            foodShopInfo = shopInfoDto
            nowFoodTextView.text = shopInfoDto.shopName
        } else if (snackType == SnackType.DRINK) {
            drinkShopInfo = shopInfoDto
            nowDrinkTextView.text = shopInfoDto.shopName
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PickShopFragment()
    }

}