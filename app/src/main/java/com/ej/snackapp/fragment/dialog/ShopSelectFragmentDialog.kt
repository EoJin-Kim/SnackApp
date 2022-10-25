package com.ej.snackapp.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ej.snackapp.MainActivity
import com.ej.snackapp.adapter.ShopPickAdapter
import com.ej.snackapp.databinding.FragmentShopSelectDialogBinding
import com.ej.snackapp.dto.response.ShopInfoDto
import com.ej.snackapp.enums.SnackType
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopSelectFragmentDialog(
    private val funPickShop: (ShopInfoDto, SnackType) -> Unit,
    private val snackType: SnackType,
) : DialogFragment() {

    private val mainViewModel : MainViewModel by activityViewModels()
    val act by lazy { activity as MainActivity }

    lateinit var binding: FragmentShopSelectDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShopSelectDialogBinding.inflate(inflater)

        // dialog 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.apply {
            val funPickShopVal: (ShopInfoDto, SnackType) -> Unit =
                { shopInfoDto, snackType -> pickShop(shopInfoDto, snackType) }
            val snackShopPickAdapter = ShopPickAdapter(funPickShopVal, snackType)

            if (snackType == SnackType.FOOD) {
                foodShopInfo.observe(viewLifecycleOwner) {
                    setDataForAdapter(snackShopPickAdapter, it)
                }
            } else if (snackType == SnackType.DRINK) {
                drinkShopInfo.observe(viewLifecycleOwner) {
                    setDataForAdapter(snackShopPickAdapter, it)
                }
            }

            fetchShopInfo(snackType)
        }
    }

    private fun pickShop(shopInfoDto: ShopInfoDto, snackType: SnackType) {
        funPickShop(shopInfoDto, snackType)
        dismiss()
    }

    private fun setDataForAdapter(
        snackShopPickAdapter: ShopPickAdapter,
        shopInfoDtoList: MutableList<ShopInfoDto>?
    ) {
        snackShopPickAdapter.submitList(shopInfoDtoList)
        binding.shopRecycler.apply {
            adapter = snackShopPickAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()

        // dialog 넓이 80% 설정
        val params = dialog?.window?.attributes
        params?.width = resources.displayMetrics.widthPixels * 8 / 10
//        params?.height = resources.displayMetrics.heightPixels * 5 /10
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }


    companion object {
        @JvmStatic
        fun newInstance(
            funCreateGroupVal: (ShopInfoDto, SnackType) -> Unit,
            snackType: SnackType
        ): ShopSelectFragmentDialog {
            return ShopSelectFragmentDialog(funCreateGroupVal, snackType)
        }
    }
}