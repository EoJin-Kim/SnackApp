package com.ej.snackapp.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ej.snackapp.adapter.SnackPickAdapter
import com.ej.snackapp.databinding.FragmentSnackSelectDialogBinding
import com.ej.snackapp.enums.SnackType
import com.ej.snackapp.dto.response.MemberSnackInfoDto
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SnackSelectFragmentDialog(
    private val confirmSnackFun: (MemberSnackInfoDto, SnackType, String) -> Unit,
    private val memberSnackInfoDto: MemberSnackInfoDto,
    private val snackType: SnackType

) : DialogFragment() {
    lateinit var binding: FragmentSnackSelectDialogBinding
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSnackSelectDialogBinding.inflate(inflater)

        // dialog 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(snackType== SnackType.FOOD){
            binding.shopName.text = mainViewModel.foodShopName
        }
        else if(snackType== SnackType.DRINK){
            binding.shopName.text = mainViewModel.drinkShopName
        }

        setRecycler()
        binding.choiceBtn.setOnClickListener {
            confirmSnackFun(memberSnackInfoDto,snackType,binding.selectSnack.text.toString())
            dismiss()
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

    private fun setRecycler() {
        val pickSnackDialogFunVal : (String) -> Unit = { snack -> pickSnackDialogFun(snack)}
        val snackPickAdapter = SnackPickAdapter(pickSnackDialogFunVal)
        mainViewModel.apply {
            shopDetailInfo.observe(viewLifecycleOwner){
                snackPickAdapter.submitList(it.snackList)
            }
        }
        binding.snackRecycler.apply {
            adapter = snackPickAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        mainViewModel.fetchShopMenuInfo(snackType)
    }

    private fun pickSnackDialogFun(snackName : String){
        binding.selectSnack.text = snackName;
    }

    companion object {
        @JvmStatic
        fun newInstance(pickSnackFun: (MemberSnackInfoDto, SnackType, String) -> Unit, memberSnackInfoDto: MemberSnackInfoDto, snackType: SnackType) : SnackSelectFragmentDialog {
            return SnackSelectFragmentDialog(pickSnackFun,memberSnackInfoDto,snackType)
        }
    }
}