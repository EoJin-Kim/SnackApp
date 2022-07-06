package com.ej.snackapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ej.snackapp.databinding.ActivityMainBinding
import com.ej.snackapp.fragment.PickShopFragment
import com.ej.snackapp.fragment.PickSnackFragment
import com.ej.snackapp.fragment.ResultSnackFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val pickSnackFragment = PickSnackFragment()
    val resultSnackFragment = ResultSnackFragment()
    val pickShopFragment = PickShopFragment()
    val fragList = arrayOf(pickSnackFragment, resultSnackFragment, pickShopFragment)

    val tabNameList = arrayOf("Today Snack", "Result Snack", "Pick Shop")

    val userSnackInfoList = ArrayList<UserSnackInfo>()
    val foodShopInfoList = ArrayList<ShopInfo>()
    val drinkShopInfoList = ArrayList<ShopInfo>()

    var nowFoodId = -1
    var nowFoodType = ""

    var nowDrinkId = -1
    var nowDrinkType = ""


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

        getSnackList()
        getNowSnackType()

        while(nowFoodId!=-1 && nowDrinkId!=-1){
            SystemClock.sleep(500)
        }

        nowSnackSet()


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





    fun getSnackList(){

        drinkShopInfoList.clear()
        foodShopInfoList.clear()

        thread{
            val client = OkHttpClient()

            val foodUrl = "${ServerInfo.SERVER_URL}/api/shop/FOOD"


            val foodRequest = Request.Builder().url(foodUrl).build()
            val foodResponse = client.newCall(foodRequest).execute()

            if(foodResponse.isSuccessful){
                val resultText = foodResponse.body?.string()!!.trim()
                Log.d("test",resultText.toString())
                val root = JSONObject(resultText)

                val data = root.getJSONArray("data")
                for (i in 0 until data.length()) {
                    val shopData = data.getJSONObject(i)
                    val id = shopData.getInt("id")
                    val shopName = shopData.getString("shopName")
                    val menuURI = shopData.getString("menuURI")

                    val shopInfo = ShopInfo(id,shopName,menuURI)

                    foodShopInfoList.add(shopInfo)
                }
            }
        }
        thread{
            val client = OkHttpClient()

            val drinkUrl = "${ServerInfo.SERVER_URL}/api/shop/DRINK"


            val foodRequest = Request.Builder().url(drinkUrl).build()
            val foodResponse = client.newCall(foodRequest).execute()

            if(foodResponse.isSuccessful){
                val resultText = foodResponse.body?.string()!!.trim()
                Log.d("test",resultText.toString())
                val root = JSONObject(resultText)

                val data = root.getJSONArray("data")
                for (i in 0 until data.length()) {
                    val shopData = data.getJSONObject(i)
                    val id = shopData.getInt("id")
                    val shopName = shopData.getString("shopName")
                    val menuURI = shopData.getString("menuURI")

                    val shopInfo = ShopInfo(id,shopName,menuURI)

                    foodShopInfoList.add(shopInfo)
                }
            }
        }
    }

    fun getNowSnackType(){
        thread{
            val client = OkHttpClient()

            val drinkUrl = "${ServerInfo.SERVER_URL}/api/snack"


            val request = Request.Builder().url(drinkUrl).build()
            val response = client.newCall(request).execute()

            if(response.isSuccessful){
                val resultText = response.body?.string()!!.trim()
                Log.d("test",resultText.toString())
                val root = JSONObject(resultText)

                val data = root.getJSONObject("data")
                val foodId = data.getInt("foodId")
                val drinkId = data.getInt("drinkId")

                nowFoodId = foodId
                nowDrinkId = drinkId

            }
        }
    }

    fun nowSnackSet() : Boolean{
        if(nowFoodId == -1 || nowDrinkId ==-1) return false
        for (shopInfo in foodShopInfoList) {
            if(shopInfo.id==nowFoodId){
                nowFoodType = shopInfo.shopName
                break
            }
        }

        for (shopInfo in drinkShopInfoList) {
            if (shopInfo.id == nowDrinkId) {
                nowDrinkType = shopInfo.shopName
                break
            }
        }
        return true
    }
}
