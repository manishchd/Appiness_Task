package com.appinesstask

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MainVM : ViewModel() {

    var navController: NavController? = null

}