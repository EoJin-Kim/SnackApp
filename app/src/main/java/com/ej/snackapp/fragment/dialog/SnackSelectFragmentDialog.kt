package com.ej.snackapp.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ej.snackapp.databinding.FragmentSnackSelectDialogBinding
import com.ej.snackapp.dto.SnackType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SnackSelectFragmentDialog(
    private val pickSnackFun: (String) -> Unit,
    private val snackType: SnackType
) : DialogFragment() {
    lateinit var snackSelectFragmentDialogBinding: FragmentSnackSelectDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        snackSelectFragmentDialogBinding = FragmentSnackSelectDialogBinding.inflate(inflater)
        return snackSelectFragmentDialogBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(pickSnackFun: (String) -> Unit, snackType: SnackType) = SnackSelectFragmentDialog(pickSnackFun,snackType)
    }
}