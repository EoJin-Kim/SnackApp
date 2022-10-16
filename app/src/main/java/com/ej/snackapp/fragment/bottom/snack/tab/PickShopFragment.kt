package com.ej.snackapp.fragment.bottom.snack.tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.MainActivity
import com.ej.snackapp.R
import com.ej.snackapp.adapter.ShopPickAdapter
import com.ej.snackapp.data.UserSnackInfo
import com.ej.snackapp.databinding.FragmentPickShopBinding
import com.ej.snackapp.info.ShopInfo
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject


class PickShopFragment : Fragment() {

    lateinit var pickShopFragmentBinding : FragmentPickShopBinding

    var nowDialog : AlertDialog? = null
    lateinit var nowFoodTextView : TextView
    lateinit var nowDrinkTextView : TextView

    var foodShopInfo: ShopInfo? = null
    var drinkShopInfo : ShopInfo? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val act = activity as MainActivity
        pickShopFragmentBinding = FragmentPickShopBinding.inflate(inflater)

        nowFoodTextView = pickShopFragmentBinding.selectFood

        nowDrinkTextView = pickShopFragmentBinding.selectDrink

        val foodShopInfoList = act.foodShopInfoList

        val drinkShopInfoList = act.drinkShopInfoList

        val shopPickAdapter = createShopPickAdapter()


        pickShopFragmentBinding.foodShopBtn.setOnClickListener {

            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.shop_pick_dialog,null)
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(view)
                .create()
            val shopRecycler = view.findViewById<RecyclerView>(R.id.shop_recycler)

            shopRecycler.adapter = shopPickAdapter
            shopRecycler.layoutManager = LinearLayoutManager(requireContext())
            shopPickAdapter.submitList(foodShopInfoList)
            nowDialog = alertDialog
            alertDialog.show()
        }

        pickShopFragmentBinding.drinkShopBtn.setOnClickListener {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.shop_pick_dialog,null)
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(view)
                .create()
            val shopRecycler = view.findViewById<RecyclerView>(R.id.shop_recycler)

            shopRecycler.adapter = shopPickAdapter
            shopRecycler.layoutManager = LinearLayoutManager(requireContext())
            shopPickAdapter.submitList(drinkShopInfoList)
            nowDialog = alertDialog
            alertDialog.show()
        }

        pickShopFragmentBinding.shopCompleteBtn.setOnClickListener {
            if(nowFoodTextView.text=="간식" || nowDrinkTextView.text=="음료") {
                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("가게를 선택해주세요")
                builder.setMessage("간식과 음료 가게를 선택 해주세요!!")

                builder.setPositiveButton("확인") { diologInterface, i ->
                }
                builder.show()
                return@setOnClickListener
            }

            val job1 = GlobalScope.launch(Dispatchers.Default) {
                snackShopInit().await()
            }
            val job2 = GlobalScope.launch(Dispatchers.Default) {
                pickSnackShop(foodShopInfo!!).await()
            }

            val job3 = GlobalScope.launch(Dispatchers.Default) {
                pickSnackShop(drinkShopInfo!!).await()
            }

            runBlocking {
                job1.join()
                job2.join()
                job3.join()
            }
            act.apiInit2()
            act.nowSnackSet()
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("가게선택 완료")
            builder.setMessage("가게선택 완료!!")

            builder.setPositiveButton("확인!") { diologInterface, i ->
            }
            builder.show()
        }
        return pickShopFragmentBinding.root
    }



    private fun createShopPickAdapter(): ShopPickAdapter {
        val funVal: (ShopInfo) -> Unit = { shopInfo -> shopPickAdapterNameOnClick(shopInfo) }
        val shopPickAdapter = ShopPickAdapter(funVal)
        return shopPickAdapter
    }

    private fun shopPickAdapterNameOnClick(shopInfo: ShopInfo){
        Log.d("onclick","${shopInfo.shopName}")
        val act = activity as MainActivity
        // shop 선택 api 보내고
        // 받은 데이터를 view에 셋팅
        if(shopInfo.shopType=="FOOD"){
            foodShopInfo=shopInfo
            nowFoodTextView.text = shopInfo.shopName
            nowDialog!!.dismiss()
        }
        else if(shopInfo.shopType=="DRINK"){
            drinkShopInfo = shopInfo
            nowDrinkTextView.text =shopInfo.shopName
            nowDialog!!.dismiss()
        }

    }

    fun snackShopInit() : Deferred<Unit>{
        val act = activity as MainActivity
        var result = CoroutineScope(Dispatchers.Default).async {

            val client : OkHttpClient = OkHttpClient()
            val url = "https://sheltered-castle-40247.herokuapp.com/api/snack/init"

            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {

            }

            return@async
        }
        return result
    }

    fun pickSnackShop(shopInfo : ShopInfo) : Deferred<Unit>{
        val act = activity as MainActivity
        var result = CoroutineScope(Dispatchers.Default).async {
            val client : OkHttpClient = OkHttpClient()
            val site = "https://sheltered-castle-40247.herokuapp.com/api/snack/${shopInfo.shopType}/${shopInfo.id}"

            val body = RequestBody.create(null, byteArrayOf());

            val request = Request.Builder()
                .url(site)
                .put(body)
                .build()

            val response = client.newCall(request).execute()

            if(response.isSuccessful){
                val resultText = response.body?.string()!!.trim()
                Log.d("test",resultText.toString())
                val root = JSONObject(resultText)


                val userSnackInfoList2 = ArrayList<UserSnackInfo>()

                val data = root.getJSONObject("data")

                val foodId = data.getInt("foodId")
                val drinkId = data.getInt("drinkId")


                act.nowFoodId = foodId
                act.nowDrinkId = drinkId

                act.nowSnackSet()

                nowFoodTextView.text =act.nowFoodType
                nowDrinkTextView.text = act.nowDrinkType
            }
            nowDialog!!.dismiss()
            return@async
        }
        return result
    }

}