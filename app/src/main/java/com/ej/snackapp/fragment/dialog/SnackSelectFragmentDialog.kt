package com.ej.snackapp.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ej.snackapp.adapter.SnackPickAdapter
import com.ej.snackapp.databinding.FragmentSnackSelectDialogBinding
import com.ej.snackapp.dto.SnackType
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SnackSelectFragmentDialog(
    private val pickSnackFun: (String) -> Unit,
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(snackType==SnackType.FOOD){
            binding.shopName.text = mainViewModel.foodShopName
        }
        else if(snackType==SnackType.DRINK){
            binding.shopName.text = mainViewModel.drinkShopName
        }

        setRecycler()
        binding.choiceBtn.setOnClickListener {
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
        pickSnackFun(snackName)
        binding.selectSnack.text = snackName;
    }

    companion object {
        @JvmStatic
        fun newInstance(pickSnackFun: (String) -> Unit, snackType: SnackType) = SnackSelectFragmentDialog(pickSnackFun,snackType)
    }
}