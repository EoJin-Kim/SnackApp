package com.ej.snackapp.fragment.bottom.snack.tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.MainActivity
import com.ej.snackapp.R
import com.ej.snackapp.adapter.ShopPickAdapter
import com.ej.snackapp.databinding.FragmentPickShopBinding
import com.ej.snackapp.dto.ShopInfoDto
import com.ej.snackapp.dto.SnackType
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickShopFragment : Fragment() {

    lateinit var pickShopFragmentBinding: FragmentPickShopBinding

    private val mainViewModel: MainViewModel by viewModels()
    val act by lazy { activity as MainActivity }

    var nowDialog: AlertDialog? = null
    lateinit var nowFoodTextView: TextView
    lateinit var nowDrinkTextView: TextView

    var foodShopInfo: ShopInfoDto? = null
    var drinkShopInfo: ShopInfoDto? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        pickShopFragmentBinding = FragmentPickShopBinding.inflate(inflater)

        nowFoodTextView = pickShopFragmentBinding.selectFood
        nowDrinkTextView = pickShopFragmentBinding.selectDrink

        pickShopFragmentBinding.shopCompleteBtn.setOnClickListener {
            if (nowFoodTextView.text == "간식" || nowDrinkTextView.text == "음료") {
                shopSelectAlert("가게를 선택해주세요", "간식과 음료 가게를 선택 해주세요!!", "확인")
                return@setOnClickListener
            }
            mainViewModel.selectSnackShop(foodShopInfo!!.id, drinkShopInfo!!.id)
            shopSelectAlert("가게선택 완료", "가게선택 완료!!", "확인!")
        }
        return pickShopFragmentBinding.root
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

        pickShopFragmentBinding.foodShopBtn.setOnClickListener {
            mainViewModel.apply {
                fetchShopInfo(SnackType.FOOD)
                foodShopInfo.observe(viewLifecycleOwner) {
                    val foodShopPickAdapter = createShopPickAdapter(SnackType.FOOD)
                    createShopPickDialog(foodShopPickAdapter, it)
                }
            }
        }

        pickShopFragmentBinding.drinkShopBtn.setOnClickListener {
            mainViewModel.apply {
                fetchShopInfo(SnackType.DRINK)
                drinkShopInfo.observe(viewLifecycleOwner) {
                    val drinkShopPickAdapter = createShopPickAdapter(SnackType.DRINK)
                    createShopPickDialog(drinkShopPickAdapter, it)
                }
            }
        }
    }

    private fun createShopPickDialog(
        shopPickAdapter: ShopPickAdapter,
        shopList: MutableList<ShopInfoDto>
    ) {
        shopPickAdapter.submitList(shopList)
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.shop_pick_dialog, null)

        val shopRecycler = view.findViewById<RecyclerView>(R.id.shop_recycler)
        shopRecycler.apply {
            adapter = shopPickAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        nowDialog = alertDialog
        alertDialog.show()
    }

    private fun createShopPickAdapter(snackType: SnackType): ShopPickAdapter {
        val funVal: (ShopInfoDto) -> Unit =
            { shopInfo -> shopPickAdapterNameOnClick(shopInfo, snackType) }
        val shopPickAdapter = ShopPickAdapter(funVal)
        return shopPickAdapter
    }

    private fun shopPickAdapterNameOnClick(shopInfo: ShopInfoDto, snackType: SnackType) {
        Log.d("onclick", "${shopInfo.shopName}")
        // shop 선택 api 보내고
        // 받은 데이터를 view에 셋팅
        if (snackType == SnackType.FOOD) {
            foodShopInfo = shopInfo
            nowFoodTextView.text = shopInfo.shopName
        } else if (snackType == SnackType.DRINK) {
            drinkShopInfo = shopInfo
            nowDrinkTextView.text = shopInfo.shopName
        }
        nowDialog!!.dismiss()

    }

}