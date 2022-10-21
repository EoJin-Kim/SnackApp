package com.ej.snackapp.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.ej.snackapp.MainActivity
import com.ej.snackapp.adapter.ShopPickAdapter
import com.ej.snackapp.databinding.FragmentShopSelectDialogBinding
import com.ej.snackapp.dto.ShopInfoDto
import com.ej.snackapp.dto.SnackType
import com.ej.snackapp.viewmodel.MainViewModel


class ShopSelectFragmentDialog(
    private val funCreateGroupVal: (ShopInfoDto,SnackType) -> Unit,
    private val snackType: SnackType,
) : DialogFragment() {

    private val mainViewModel: MainViewModel by viewModels()
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
            fetchShopInfo(snackType)
            drinkShopInfo.observe(viewLifecycleOwner) {
                val drinkShopPickAdapter = createShopPickAdapter(snackType)
                drinkShopPickAdapter.submitList(it)
                binding.shopRecycler.adapter = drinkShopPickAdapter
            }
        }
    }

    private fun createShopPickAdapter(snackType: SnackType): ShopPickAdapter {

        val shopPickAdapter = ShopPickAdapter(funCreateGroupVal,snackType)
        return shopPickAdapter
    }

    override fun onResume() {
        super.onResume()

        // dialog 넓이 80% 설정
        val params = dialog?.window?.attributes
        params?.width = resources.displayMetrics.widthPixels * 8 /10
        params?.height = resources.displayMetrics.heightPixels * 5 /10
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }



    companion object {
        @JvmStatic
        fun newInstance(funCreateGroupVal: (ShopInfoDto,SnackType) -> Unit,snackType: SnackType) {
            ShopSelectFragmentDialog(funCreateGroupVal,snackType)
        }
    }
}