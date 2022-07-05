package com.ej.snackapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ej.snackapp.databinding.ActivityMainBinding
import com.ej.snackapp.fragment.PickShopFragment
import com.ej.snackapp.fragment.PickSnackFragment
import com.ej.snackapp.fragment.ResultSnackFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    val pickSnackFragment = PickSnackFragment()
    val resultSnackFragment = ResultSnackFragment()
    val pickShopFragment = PickShopFragment()
    val fragList = arrayOf(pickSnackFragment, resultSnackFragment, pickShopFragment)

    val tabNameList = arrayOf("Today Snack", "Result Snack", "Pick Shop")

    val UserSnackInfoList = ArrayList<UserSnackInfo>()


    lateinit var mainActivityBinding: ActivityMainBinding


    init{
        instance = this
    }

    companion object{
        private var instance:MainActivity? = null
        fun getInstance(): MainActivity? {
            return instance
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemClock.sleep(1000)
        setTheme(R.style.Theme_SnackApp)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mainActivityBinding.root)


        val adapter1 = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragList[position]
            }
        }

        mainActivityBinding.pager2.adapter = adapter1

        // tab과 viewpager를 연결한다다
        TabLayoutMediator(
            mainActivityBinding.tabs,
            mainActivityBinding.pager2
        ) { tab: TabLayout.Tab, i: Int ->
            tab.text = tabNameList[i]

        }.attach()
    }

}
