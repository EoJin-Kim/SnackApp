package com.ej.snackapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.ej.snackapp.databinding.ActivityMainBinding
import com.ej.snackapp.info.ServerInfo
import com.ej.snackapp.info.ShopDetailInfo
import com.ej.snackapp.info.ShopInfo
import com.ej.snackapp.data.UserSnackInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    var userSnackInfoList : MutableLiveData<ArrayList<UserSnackInfo>> = MutableLiveData<ArrayList<UserSnackInfo>> ()
    var filterUserSnackInfoList : MutableLiveData<ArrayList<UserSnackInfo>> = MutableLiveData<ArrayList<UserSnackInfo>> ()

    val foodShopInfoList = ArrayList<ShopInfo>()
    val drinkShopInfoList = ArrayList<ShopInfo>()



    var nowFoodId = -1
    var nowFoodType = ""

    var nowDrinkId = -1
    var nowDrinkType = ""


    var foodShopDetailInfo : ShopDetailInfo = ShopDetailInfo("","","", MutableLiveData())
    var drinkShopDetailInfo : ShopDetailInfo = ShopDetailInfo("","","",MutableLiveData())

    lateinit var mainActivityBinding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        apiInit1()
//        Log.d("crt","foodShopInfoList.size : ${foodShopInfoList.size}")
//        Log.d("crt","drinkShopInfoList.size : ${drinkShopInfoList.size}")
//        Log.d("crt","nowFoodId : ${nowFoodId}")
//        Log.d("crt","nowDrinkId: ${nowDrinkId}")
//        apiInit2()
//        Log.d("crt","foodShopDetailInfo?.snackList?.size : ${foodShopDetailInfo?.snackList?.value?.size}")
//        Log.d("crt","drinkShopDetailInfo?.snackList?.size : ${drinkShopDetailInfo?.snackList?.value?.size}")
//        Log.d("crt","user member size : ${userSnackInfoList.value?.size}")
//        nowSnackSet()
//
//        Log.d("crt","${nowFoodType}")
//        Log.d("crt","${nowDrinkType}")
        setTheme(R.style.Theme_SnackApp)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(mainActivityBinding.root)






    }




    fun apiInit1() {

        runBlocking{
            val scope = CoroutineScope(Dispatchers.IO)

            val job1 = async(Dispatchers.IO) {
                getDrinkSnackList()
            }
            val job2 = scope.async {
                getFoodSnackList()
            }

            val job3 = scope.async{
                getNowSnackType()
            }
//            job1.await()
//            job2.await()
//            job1.join()
//            job2.join()
//            job3.join()

        }


    }
    fun apiInit2(){



        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            val job4 = scope.async {
                getFoodShopDetailInfo()
            }

            val job5 = scope.async{
                getDrinkShopDetailInfo()
            }

            val job6 = scope.async{
                getUserSnackList(true)
            }
//            job4.await()
//            job5.await()
//            job6.await()
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
//        var result = CoroutineScope(Dispatchers.Default).async {
//            val client = OkHttpClient()
//
//            val drinkUrl = "${ServerInfo.SERVER_URL}/api/shop/${snackType}"
//
//
//            val foodRequest = Request.Builder().url(drinkUrl).build()
//            val foodResponse = client.newCall(foodRequest).execute()
//
//            if (foodResponse.isSuccessful) {
//                val resultText = foodResponse.body?.string()!!.trim()
//                Log.d("test", resultText.toString())
//                val root = JSONObject(resultText)
//
//                val data = root.getJSONArray("data")
//                for (i in 0 until data.length()) {
//                    val shopData = data.getJSONObject(i)
//                    val id = shopData.getInt("id")
//                    val shopName = shopData.getString("shopName")
//                    val menuURI = shopData.getString("menuURI")
//
//                    val shopInfo = ShopInfo(id, shopName, menuURI,snackType)
//
//                    drinkShopInfoList.add(shopInfo)
//                }
//            }
//            return@async
//        }
//        return result

    fun getNowSnackType(){

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

    fun getFoodShopDetailInfo(){




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
                foodShopDetailInfo.shopName=shopName
                foodShopDetailInfo.snackType = snackType
                foodShopDetailInfo.menuURI = menuURI
                foodShopDetailInfo.snackList.postValue(snackList)

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
                drinkShopDetailInfo.snackList.postValue(snackList)
            }


    }

    fun getUserSnackList(filter:Boolean) {

//        act.userSnackInfoList!!.value!!.clear()





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


                }
                filterUserSnackInfoList.postValue(userSnackInfoList2)
                userSnackInfoList.postValue(userSnackInfoList2)

            }
    }
}
