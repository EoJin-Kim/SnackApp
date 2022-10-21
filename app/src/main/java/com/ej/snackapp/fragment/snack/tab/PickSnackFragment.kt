package com.ej.snackapp.fragment.snack.tab

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.*
import com.ej.snackapp.adapter.SnackPickAdapter
import com.ej.snackapp.adapter.UserPickAdapter
import com.ej.snackapp.databinding.FragmentPickSnackBinding
import com.ej.snackapp.dto.SnackType
import com.ej.snackapp.dto.UserSnackInfoDto
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.thread

@AndroidEntryPoint
class PickSnackFragment : Fragment() {

    lateinit var pickSnackFragmentBinding: FragmentPickSnackBinding

    val act by lazy { activity as MainActivity }
    private val mainViewModel: MainViewModel by viewModels()
    lateinit var selectSnack: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act.apiInit2()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        pickSnackFragmentBinding = FragmentPickSnackBinding.inflate(inflater)


//        drinkPickAdapter = createSnackPickAdapter(SnackType.DRINK)



        pickSnackFragmentBinding.snackSwipe.setOnRefreshListener {
            pickSnackFragmentBinding.nameInput.setText("")

            val inputMethodManager =
                act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                pickSnackFragmentBinding.nameInput.windowToken,
                0
            )
            pickSnackFragmentBinding.snackSwipe.isRefreshing = false
        }

        pickSnackFragmentBinding.nameInput.setText("김어진")
        val textChangeListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {


                thread {
//                    act.filterUserSnackInfoList.clear()
                    val filterUserSnackInfoDtoList = ArrayList<UserSnackInfoDto>()
                    for (userinfo in mainViewModel.userPickInfo?.value!!) {

                        if (userinfo.name.contains(s.toString())) {
                            Log.d("test", "after : ${userinfo.name}")
                            filterUserSnackInfoDtoList.add(userinfo)
                        }
                    }
                    act.filterUserSnackInfoDtoList.postValue(filterUserSnackInfoDtoList)

                }

            }
        }
        pickSnackFragmentBinding.nameInput.addTextChangedListener(textChangeListener)
        pickSnackFragmentBinding.nameInput.setOnEditorActionListener { v, actionId, event ->
            Log.d("key", "enter")
            false
        }

        return pickSnackFragmentBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPickAdapter = createUserPickAdapter()

        mainViewModel.userPickInfo.observe(viewLifecycleOwner) {
            userPickAdapter.submitList(it)
        }

        val recyclerView = pickSnackFragmentBinding.pickSnackRecycler
        recyclerView.apply {
            adapter = userPickAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        mainViewModel.fetchUserPickInfo()

    }

    private fun createSnackPickAdapter(snackType: SnackType): SnackPickAdapter {
        val funVal: (String) -> Unit = { str -> snackPickAdapterOnClick(str) }
        val snackPickAdapter = SnackPickAdapter(funVal)
        return snackPickAdapter
    }

    private fun createUserPickAdapter(): UserPickAdapter {
        val funSnackBtn: (SnackType) -> Unit = { snackType ->
            createSnackPickDialog(snackType) }

        val userPickAdapter = UserPickAdapter(funSnackBtn)

        return userPickAdapter
    }

    private fun createSnackPickDialog(snackType: SnackType) {


    }

    private fun snackPickAdapterOnClick(snackName: String) {
        selectSnack?.text = snackName
        Log.d("onclick", snackName)
    }


    // 사용자가 간식 선택 버튼 클릭 시
    private fun userFoodPickAdapterButtonOnClick(
        userSnackInfoDto: UserSnackInfoDto,
    ): String {





//        if(snackType==SnackType.FOOD){
//            mainViewModel.foodShopDetailInfo.observe(viewLifecycleOwner){
//                snackPickAdapter.submitList(it.snackList)
//            }
//            mainViewModel.fetchFoodShopMenuInfo()
//        }
//        else if(snackType==SnackType.DRINK){
//            mainViewModel.drinkShopDetailInfo.observe(viewLifecycleOwner){
//                snackPickAdapter.submitList(it.snackList)
//            }
//            mainViewModel.fetchDrinkShopMenuInfo()
//        }
        return "a"
    }


    private fun userDrinkPickAdapterButtonOnClick(
        userSnackInfoDto: UserSnackInfoDto,
    ): String {
        return "b"
    }

}
