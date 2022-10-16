package com.ej.snackapp.fragment.bottom.snack.tab

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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.*
import com.ej.snackapp.adapter.SnackPickAdapter
import com.ej.snackapp.adapter.UserPickAdapter
import com.ej.snackapp.databinding.FragmentPickSnackBinding
import com.ej.snackapp.data.UserSnackInfo
import com.ej.snackapp.info.ShopDetailInfo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import kotlin.concurrent.thread


class PickSnackFragment : Fragment() {

    lateinit var pickSnackFragmentBinding : FragmentPickSnackBinding
    var foodpickAdapter : SnackPickAdapter? = null
    var drinkPickAdapter : SnackPickAdapter? = null

    var selectSnack : TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val act  =activity as MainActivity
        act.apiInit1()
        act.apiInit2()
        act.nowSnackSet()





    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val act = activity as MainActivity
        pickSnackFragmentBinding = FragmentPickSnackBinding.inflate(inflater)



        foodpickAdapter = createSnackPickAdapter(act.foodShopDetailInfo)
        drinkPickAdapter = createSnackPickAdapter(act.drinkShopDetailInfo)

        val userPickAdapter = createUserPickAdapter()

        val userSnackInfoList = act.userSnackInfoList
        val filterUserSnackInfoList = act.filterUserSnackInfoList
        val foodShopSnackList = act.foodShopDetailInfo.snackList
        val drinkShopSnackList = act.drinkShopDetailInfo.snackList





        foodShopSnackList?.observe(viewLifecycleOwner, Observer{
            it.let {
                foodpickAdapter?.submitList(it)
            }
        })
        drinkShopSnackList?.observe(viewLifecycleOwner, Observer{
            it.let {
                drinkPickAdapter?.submitList(it)
            }
        })

//        userSnackInfoList?.observe(viewLifecycleOwner, Observer {
//            it.let {
//                userPickAdapter.submitList(it)
//            }
//        })
        filterUserSnackInfoList?.observe(viewLifecycleOwner, Observer{
            it.let {
                userPickAdapter?.submitList(it)
            }
        })

        val recyclerView = pickSnackFragmentBinding.pickSnackRecycler
        recyclerView.adapter = userPickAdapter


        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        pickSnackFragmentBinding.snackSwipe.setOnRefreshListener{
            act.getUserSnackList(true)
            pickSnackFragmentBinding.nameInput.setText("")

            val inputMethodManager = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(pickSnackFragmentBinding.nameInput.windowToken,0)
            pickSnackFragmentBinding.snackSwipe.isRefreshing = false
        }

        pickSnackFragmentBinding.nameInput.setText("김어진")
        val textChangeListener = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {


                thread {
//                    act.filterUserSnackInfoList.clear()
                    val filterUserSnackInfoList = ArrayList<UserSnackInfo>()
                    for (userinfo in act.userSnackInfoList?.value!!) {

                        if (userinfo.name.contains(s.toString())) {
                            Log.d("test","after : ${userinfo.name}")
                            filterUserSnackInfoList.add(userinfo)
                        }
                    }
                    act.filterUserSnackInfoList.postValue(filterUserSnackInfoList)

                }

            }
        }
        pickSnackFragmentBinding.nameInput.addTextChangedListener(textChangeListener)
        pickSnackFragmentBinding.nameInput.setOnEditorActionListener { v, actionId, event ->
            Log.d("key","enter")
            false
        }

        return pickSnackFragmentBinding.root

    }

    override fun onResume() {
        super.onResume()
        val act  =activity as MainActivity
        act.apiInit1()
        act.apiInit2()
        act.nowSnackSet()

        // 가게가 선택되어있지 않으면
//        if(act.nowFoodId == 0 || act.nowFoodId == -1 || act.nowDrinkId == 0 || act.nowDrinkId == -1){
//            act.mainActivityBinding.pager2.currentItem = act.mainActivityBinding.pager2.currentItem+2
//        }
    }

    private fun createSnackPickAdapter(shopDetailInfo: ShopDetailInfo) : SnackPickAdapter{
        val funVal : (String) -> Unit = { str ->snackPickAdapterOnClick(str) }
        val snackPickAdapter = SnackPickAdapter(funVal)
        snackPickAdapter.submitList(shopDetailInfo.snackList.value)

        return snackPickAdapter
    }

    private fun createUserPickAdapter(): UserPickAdapter {
//        val funVal: (UserSnackInfo) -> Unit = { userSnackInfo -> userPickAdapterNameOnClick(userSnackInfo) }
        val funFoodBtnVal: (UserSnackInfo,Int) -> Unit = { userSnackInfo,position-> userFoodPickAdapterButtonOnClick(userSnackInfo,position) }
        val funDrinkBtnVal: (UserSnackInfo,Int) -> Unit = { userSnackInfo,position-> userDrinkPickAdapterButtonOnClick(userSnackInfo,position) }
        val userPickAdapter = UserPickAdapter(funFoodBtnVal, funDrinkBtnVal)

        return userPickAdapter
    }

    private fun snackPickAdapterOnClick(snackName : String){
        selectSnack?.text=snackName
        Log.d("onclick",snackName)
    }

    private fun userPickAdapterNameOnClick(userSnackInfo: UserSnackInfo){

        Log.d("onclick","name click")
    }

    private fun userFoodPickAdapterButtonOnClick(userSnackInfo : UserSnackInfo, position: Int) : String{
        val act = activity as MainActivity
        Log.d("onclick","btn click")

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.snack_pick_dialog,null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        val snackRecycler = view.findViewById<RecyclerView>(R.id.snack_recycler)
        val shopNameText = view.findViewById<TextView>(R.id.shop_name)
        selectSnack = view.findViewById<TextView>(R.id.select_snack)
        val confirmButton = view.findViewById<View>(R.id.choice_btn)


        snackRecycler.adapter = foodpickAdapter
        snackRecycler.layoutManager = LinearLayoutManager(requireContext())

        shopNameText.text = act.nowFoodType

        confirmButton.setOnClickListener{
            val currentList = act.filterUserSnackInfoList.value
            val newList = act.filterUserSnackInfoList.value?.clone() as ArrayList<UserSnackInfo>
            val currentUser = currentList?.get(position)
            val updateUser = currentUser?.copy()
            updateUser?.food = selectSnack?.text.toString()
            newList.set(position,updateUser!!)
            act.filterUserSnackInfoList.postValue(newList)
            thread {
                val client : OkHttpClient = OkHttpClient()
                val site = "https://sheltered-castle-40247.herokuapp.com/api/snack/pick"
                val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
                val json : JSONObject = JSONObject()
                json.put("memberId",currentUser.id)
                json.put("option","")
                json.put("snack",selectSnack?.text.toString())
                json.put("snackType","FOOD")
                val body = RequestBody.create(JSON, json.toString());

//                val request = Request.Builder().url(url).build()
//                val response = client.newCall(request).execute()
                val request = Request.Builder()
                    .url(site)
                    .post(body)
                    .build()

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
//                    act.filterUserSnackInfoList.postValue(userSnackInfoList2)
                    act.userSnackInfoList.postValue(userSnackInfoList2)

                }
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
        return "btn!!"
    }


    private fun userDrinkPickAdapterButtonOnClick(userSnackInfo : UserSnackInfo, position: Int) : String{
        val act = activity as MainActivity
        Log.d("onclick","btn click")

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.snack_pick_dialog,null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        val snackRecycler = view.findViewById<RecyclerView>(R.id.snack_recycler)
        val shopNameText = view.findViewById<TextView>(R.id.shop_name)
        selectSnack = view.findViewById<TextView>(R.id.select_snack)
        val confirmButton = view.findViewById<View>(R.id.choice_btn)


        snackRecycler.adapter = drinkPickAdapter
        snackRecycler.layoutManager = LinearLayoutManager(requireContext())

        shopNameText.text = act.nowDrinkType

        confirmButton.setOnClickListener{
            val currentList = act.filterUserSnackInfoList.value
            val newList = act.filterUserSnackInfoList.value?.clone() as ArrayList<UserSnackInfo>
            val currentUser = currentList?.get(position)
            val updateUser = currentUser?.copy()
            updateUser?.drink = selectSnack?.text.toString()
            newList.set(position,updateUser!!)
            act.filterUserSnackInfoList.postValue(newList)
            thread {
                val client : OkHttpClient = OkHttpClient()
                val site = "https://sheltered-castle-40247.herokuapp.com/api/snack/pick"
                val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
                val json : JSONObject = JSONObject()
                json.put("memberId",currentUser.id)
                json.put("option","")
                json.put("snack",selectSnack?.text.toString())
                json.put("snackType","DRINK")
                val body = RequestBody.create(JSON, json.toString());

//                val request = Request.Builder().url(url).build()
//                val response = client.newCall(request).execute()
                val request = Request.Builder()
                    .url(site)
                    .post(body)
                    .build()

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
//                    act.filterUserSnackInfoList.postValue(userSnackInfoList2)
                    act.userSnackInfoList.postValue(userSnackInfoList2)

                }
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
        return "btn!!"
    }

}
