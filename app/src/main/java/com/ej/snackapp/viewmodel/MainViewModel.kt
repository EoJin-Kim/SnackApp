package com.ej.snackapp.viewmodel

import androidx.lifecycle.ViewModel
import com.ej.snackapp.api.SnackApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val snackApi: SnackApi
) : ViewModel(){
}