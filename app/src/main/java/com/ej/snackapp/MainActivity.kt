package com.ej.snackapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.ej.snackapp.databinding.ActivityMainBinding
import com.ej.snackapp.info.ServerInfo
import com.ej.snackapp.dto.ShopDetailInfo
import com.ej.snackapp.dto.ShopInfo
import com.ej.snackapp.dto.UserSnackInfoDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    var filterUserSnackInfoDtoList : MutableLiveData<ArrayList<UserSnackInfoDto>> = MutableLiveData<ArrayList<UserSnackInfoDto>> ()

    val foodShopInfoList = ArrayList<ShopInfo>()
    val drinkShopInfoList = ArrayList<ShopInfo>()



    var nowFoodId = -1
    var nowFoodType = ""

    var nowDrinkId = -1
    var nowDrinkType = ""


    var foodShopDetailInfo : ShopDetailInfo = ShopDetailInfo("","","", mutableListOf<String>())
    var drinkShopDetailInfo : ShopDetailInfo = ShopDetailInfo("","","",mutableListOf<String>())

    lateinit var mainActivityBinding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_SnackApp)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)


    }


    fun apiInit2(){


        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)


            val job5 = scope.async{
                getDrinkShopDetailInfo()
            }
        }
    }
    fun getFoodSnackList() {
        val snackType = "FOOD"
        foodShopInfoList.clear()


        val client = OkHttpClient()

        val foodUrl = "${ServerInfo.SERVER_URL}/api/shop/${snackType}"


        val foodRequest = Request.Builder().url(foodUrl).build()
        val foodResponse = client.newCall(foodRequest).execute()
        if (foodResponse.isSuccessful) {
            val resultText = foodResponse.body?.string()!!.trim()
            Log.d("test", resultText.toString())
            val root = JSONObject(resultText)

            val data = root.getJSONArray("data")
            for (i in 0 until data.length()) {
                val shopData = data.getJSONObject(i)
                val id = shopData.getInt("id")
                val shopName = shopData.getString("shopName")
                val menuURI = shopData.getString("menuURI")

                val shopInfo = ShopInfo(id, shopName, menuURI,snackType)

                foodShopInfoList.add(shopInfo)
            }
        }

    }

    fun getDrinkSnackList() {
        val snackType = "DRINK"
        drinkShopInfoList.clear()


        val client = OkHttpClient()

        val drinkUrl = "${ServerInfo.SERVER_URL}/api/shop/${snackType}"


        val foodRequest = Request.Builder().url(drinkUrl).build()
        val foodResponse = client.newCall(foodRequest).execute()

        if (foodResponse.isSuccessful) {
            val resultText = foodResponse.body?.string()!!.trim()
            Log.d("test", resultText.toString())
            val root = JSONObject(resultText)

            val data = root.getJSONArray("data")
            for (i in 0 until data.length()) {
                val shopData = data.getJSONObject(i)
                val id = shopData.getInt("id")
                val shopName = shopData.getString("shopName")
                val menuURI = shopData.getString("menuURI")

                val shopInfo = ShopInfo(id, shopName, menuURI, snackType)

                drinkShopInfoList.add(shopInfo)
            }
        }
    }


    fun getDrinkShopDetailInfo() {

            val client = OkHttpClient()

            val url = "${ServerInfo.SERVER_URL}/api/shop/DRINK/${nowDrinkId}"

            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val resultText = response.body?.string()!!.trim()
                Log.d("test", resultText.toString())
                val root = JSONObject(resultText)


                val data = root.getJSONObject("data")
                val shopName = data.getString("shopName")
                val snackType = data.getString("snackType")
                val menuURI = data.getString("menuURI")
                val snackListData = data.getJSONArray("snackList")
                val snackList = ArrayList<String>()
                for (i in 0 until snackListData.length()) {
                    val snackName = snackListData.get(i).toString()
                    snackList.add(snackName)
                }
                drinkShopDetailInfo.shopName=shopName
                drinkShopDetailInfo.snackType = snackType
                drinkShopDetailInfo.menuURI = menuURI
                drinkShopDetailInfo.snackList=snackList
            }


    }
}
