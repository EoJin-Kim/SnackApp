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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ej.snackapp.*
import com.ej.snackapp.adapter.UserPickAdapter
import com.ej.snackapp.databinding.FragmentPickSnackBinding
import com.ej.snackapp.enums.SnackType
import com.ej.snackapp.dto.response.MemberSnackInfoDto
import com.ej.snackapp.fragment.dialog.SnackSelectFragmentDialog
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.thread

@AndroidEntryPoint
class PickSnackFragment : Fragment() {

    lateinit var binding: FragmentPickSnackBinding
    val act by lazy { activity as MainActivity }
    private val mainViewModel : MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPickSnackBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 사용자 간식 선택 리스트 recycler 데이터 셋팅
        setRecyclerData()

        // 테스트 용 필터 이름 셋팅
        binding.nameInput.setText("김어진")
        // 이름 필터링 함수
        nameFiltering()

        // swipe시 새로고침
        setSwipe()
    }

    private fun setRecyclerData() {
        val userPickAdapter = createUserPickAdapter()

        mainViewModel.userPickInfo.observe(viewLifecycleOwner) {
            userPickAdapter.submitList(it)
        }
        binding.pickSnackRecycler.apply {
            adapter = userPickAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        mainViewModel.fetchUserPickInfo()
    }

    private fun nameFiltering() {
        val textChangeListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                thread {
                    val filterMemberSnackInfoDtoList = mutableListOf<MemberSnackInfoDto>()
                    for (userinfo in mainViewModel.userPickInfo?.value!!) {

                        if (userinfo.name.contains(s.toString())) {
                            Log.d("test", "after : ${userinfo.name}")
                            filterMemberSnackInfoDtoList.add(userinfo)
                        }
                    }
                    // 필터링된 filterMemberSnackInfoDtoList adapter에 submit
                }
            }
        }
        binding.nameInput.addTextChangedListener(textChangeListener)
        binding.nameInput.setOnEditorActionListener { v, actionId, event ->
            Log.d("key", "enter")
            false
        }
    }

    private fun setSwipe() {
        binding.snackSwipe.setOnRefreshListener {
            binding.nameInput.setText("")
            val inputMethodManager =
                act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.nameInput.windowToken, 0)
            binding.snackSwipe.isRefreshing = false
        }
    }

    private fun createUserPickAdapter(): UserPickAdapter {
        val funSnackBtn: (MemberSnackInfoDto, SnackType) -> Unit = { userSnackInfoDto, snackType -> createSnackPickDialog(userSnackInfoDto,snackType) }
        val userPickAdapter = UserPickAdapter(funSnackBtn)
        return userPickAdapter
    }

    private fun createSnackPickDialog(memberSnackInfoDto: MemberSnackInfoDto, snackType: SnackType) {
        val funPickSnackVal : (MemberSnackInfoDto, SnackType, String) -> Unit = { userSnackInfoDto, snackType, snack -> dialogPickSnackOnClick(userSnackInfoDto,snackType,snack)}
        val dialog : SnackSelectFragmentDialog = SnackSelectFragmentDialog.newInstance(funPickSnackVal,memberSnackInfoDto,snackType)

        dialog.show(act.supportFragmentManager,"가게 선택")
    }

    private fun dialogPickSnackOnClick(memberSnackInfoDto: MemberSnackInfoDto, snackType: SnackType, snackName: String) {
        Log.d("selectSnack", "${memberSnackInfoDto}")
        Log.d("selectSnack", "${snackType}")
        Log.d("selectSnack", "${snackName}")
        mainViewModel.selectSnack(memberSnackInfoDto,snackType,snackName)
    }

    companion object{
        fun newInstance() = PickSnackFragment()
    }
}
