package com.ej.snackapp.fragment.snack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ej.snackapp.MainActivity
import com.ej.snackapp.databinding.FragmentSnackBinding
import com.ej.snackapp.fragment.snack.tab.PickShopFragment
import com.ej.snackapp.fragment.snack.tab.PickSnackFragment
import com.ej.snackapp.fragment.snack.tab.ResultSnackFragment
import com.ej.snackapp.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SnackFragment : Fragment() {
    lateinit var snackFragmentBinding : FragmentSnackBinding

    private val mainViewModel : MainViewModel by viewModels()

    val act by lazy{activity as MainActivity}

    val pickSnackFragment = PickSnackFragment()
    val resultSnackFragment = ResultSnackFragment()
    val pickShopFragment = PickShopFragment()
    val fragList = arrayOf(pickSnackFragment, resultSnackFragment, pickShopFragment)
    val tabNameList = arrayOf("Today Snack", "Result Snack", "Pick Shop")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        snackFragmentBinding= FragmentSnackBinding.inflate(inflater)
        return snackFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragList[position]
            }
        }


        snackFragmentBinding.pager2.adapter = tabAdapter

        // tab과 viewpager를 연결한다다
        TabLayoutMediator(
            snackFragmentBinding.tabs,
            snackFragmentBinding.pager2
        ) { tab: TabLayout.Tab, i: Int ->
            tab.text = tabNameList[i]
        }.attach()

    }
}