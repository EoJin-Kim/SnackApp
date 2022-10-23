package com.ej.snackapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.ej.snackapp.databinding.ActivityMainBinding
import com.ej.snackapp.dto.ShopDetailInfo
import com.ej.snackapp.dto.ShopInfo
import com.ej.snackapp.dto.UserSnackInfoDto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    lateinit var binding: ActivityMainBinding

    var filterUserSnackInfoDtoList : MutableLiveData<ArrayList<UserSnackInfoDto>> = MutableLiveData<ArrayList<UserSnackInfoDto>> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SnackApp)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
