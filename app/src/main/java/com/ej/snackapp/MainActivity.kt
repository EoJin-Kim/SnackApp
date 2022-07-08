package com.ej.snackapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ej.snackapp.databinding.ActivityMainBinding
import com.ej.snackapp.fragment.PickShopFragment
import com.ej.snackapp.fragment.PickSnackFragment
import com.ej.snackapp.fragment.ResultSnackFragment
import com.ej.snackapp.info.ServerInfo
import com.ej.snackapp.info.ShopDetailInfo
import com.ej.snackapp.info.ShopInfo
import com.ej.snackapp.data.UserSnackInfo
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
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

    var userSnackInfoList : MutableLiveData<ArrayList<UserSnackInfo>> = MutableLiveData<ArrayList<UserSnackInfo>> ()
    var filterUserSnackInfoList = mutableListOf<UserSnackInfo>()

    val foodShopInfoList = ArrayList<ShopInfo>()
    val drinkShopInfoList = ArrayList<ShopInfo>()



    var nowFoodId = -1
    var nowFoodType = ""

    var nowDrinkId = -1
    var nowDrinkType = ""


    var foodShopDetailInfo : ShopDetailInfo? = null
    var drinkShopDetailInfo : ShopDetailInfo? = null

    lateinit var mainActivityBinding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiInit1()
        Log.d("crt","foodShopInfoList.size : ${foodShopInfoList.size}")
        Log.d("crt","drinkShopInfoList.size : ${drinkShopInfoList.size}")
        Log.d("crt","nowFoodId : ${nowFoodId}")
        Log.d("crt","nowDrinkId: ${nowDrinkId}")
        apiInit2()
        Log.d("crt","foodShopDetailInfo?.snackList?.size : ${foodShopDetailInfo?.snackList?.value?.size}")
        Log.d("crt","drinkShopDetailInfo?.snackList?.size : ${drinkShopDetailInfo?.snackList?.value?.size}")
        Log.d("crt","user member size : ${userSnackInfoList.value?.size}")
        nowSnackSet()

        Log.d("crt","${nowFoodType}")
        Log.d("crt","${nowDrinkType}")
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




    fun apiInit1() {
        val job1 = GlobalScope.launch(Dispatchers.Default) {
            getDrinkSnackList().await()
        }

        val job2 = GlobalScope.launch(Dispatchers.Default) {
            getFoodSnackList().await()
        }

        val job3 = GlobalScope.launch(Dispatchers.Default){
            getNowSnackType().await()
        }



        runBlocking {
            job1.join()
            job2.join()
            job3.join()
        }
    }
    fun apiInit2(){
        val job4 = GlobalScope.launch(Dispatchers.Default){
            getFoodShopDetailInfo().await()
        }

        val job5 = GlobalScope.launch(Dispatchers.Default){
            getDrinkShopDetailInfo().await()
        }

        val job6 = GlobalScope.launch(Dispatchers.Default){
            getUserSnackList(true).await()
        }

        runBlocking {

            job4.join()
            job5.join()
            job6.join()
        }
    }
    fun getFoodSnackList() :Deferred<Unit> {
        foodShopInfoList.clear()

        val result = CoroutineScope(Dispatchers.Default).async {
            val client = OkHttpClient()

            val foodUrl = "${ServerInfo.SERVER_URL}/api/shop/FOOD"


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

                    val shopInfo = ShopInfo(id, shopName, menuURI)

                    foodShopInfoList.add(shopInfo)
                }
            }
            return@async
        }
        return result;
    }

    fun getDrinkSnackList() : Deferred<Unit>{
        drinkShopInfoList.clear()

        var result = CoroutineScope(Dispatchers.Default).async {
            val client = OkHttpClient()

            val drinkUrl = "${ServerInfo.SERVER_URL}/api/shop/DRINK"


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

                    val shopInfo = ShopInfo(id, shopName, menuURI)

                    drinkShopInfoList.add(shopInfo)
                }
            }
            return@async
        }
        return result
    }
    fun getNowSnackType() : Deferred<Unit>{
        var result = CoroutineScope(Dispatchers.Default).async {
            val client = OkHttpClient()

            val drinkUrl = "${ServerInfo.SERVER_URL}/api/snack"


            val request = Request.Builder().url(drinkUrl).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val resultText = response.body?.string()!!.trim()
                Log.d("test", resultText.toString())
                val root = JSONObject(resultText)

                val data = root.getJSONObject("data")
                val foodId = data.getInt("foodId")
                val drinkId = data.getInt("drinkId")

                nowFoodId = foodId
                nowDrinkId = drinkId
            }
            return@async
        }
        return result
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

    fun getFoodShopDetailInfo() : Deferred<Unit>{



        var result = CoroutineScope(Dispatchers.Default).async {

            val client = OkHttpClient()

            val url = "${ServerInfo.SERVER_URL}/api/shop/FOOD/${nowFoodId}"

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
                val shopDetailInfo = ShopDetailInfo(shopName,snackType,menuURI,
                    MutableLiveData(snackList)
                )
                foodShopDetailInfo = shopDetailInfo
            }
            return@async

        }
        return result
    }

    fun getDrinkShopDetailInfo() : Deferred<Unit> {
        var result = CoroutineScope(Dispatchers.Default).async {

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
                val shopDetailInfo = ShopDetailInfo(shopName,snackType,menuURI,MutableLiveData(snackList))
                drinkShopDetailInfo = shopDetailInfo
            }
            return@async
        }
        return result
    }

    fun getUserSnackList(filter:Boolean) : Deferred<Unit>{

//        act.userSnackInfoList!!.value!!.clear()
        filterUserSnackInfoList.clear()


        var result = CoroutineScope(Dispatchers.Default).async {

            val client = OkHttpClient()

            val url = "${ServerInfo.SERVER_URL}/api/snack/pick"

            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if(response.isSuccessful){
                val resultText = response.body?.string()!!.trim()
                Log.d("test",resultText.toString())
                val root = JSONObject(resultText)


                val userSnackInfoList2 = ArrayList<UserSnackInfo>()

                val data = root.getJSONArray("data")
                for (i in 0 until data.length()) {
                    val userSnackData = data.getJSONObject(i)
                    val id = userSnackData.getInt("id")
                    val name = userSnackData.getString("name")
                    val food = userSnackData.getString("food")
                    val foodOption = userSnackData.getString("foodOption")
                    val drink = userSnackData.getString("drink")
                    val drinkOption = userSnackData.getString("drinkOption")
                    val userInfo = UserSnackInfo(id,name, food, foodOption, drink, drinkOption)

                    userSnackInfoList2.add(userInfo)
                    filterUserSnackInfoList.add(userInfo)

                }
                userSnackInfoList.postValue(userSnackInfoList2)

            }
            return@async
        }
        return result
    }
}
