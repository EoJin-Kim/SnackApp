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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.*
import com.ej.snackapp.adapter.SnackPickAdapter
import com.ej.snackapp.adapter.UserPickAdapter
import com.ej.snackapp.databinding.FragmentPickSnackBinding
import com.ej.snackapp.dto.UserSnackInfoDto
import com.ej.snackapp.info.ShopDetailInfo
import com.ej.snackapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import kotlin.concurrent.thread

@AndroidEntryPoint
class PickSnackFragment : Fragment() {

    lateinit var pickSnackFragmentBinding : FragmentPickSnackBinding
    val act by lazy{activity as MainActivity}
    private val mainViewModel : MainViewModel by viewModels()

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

        pickSnackFragmentBinding = FragmentPickSnackBinding.inflate(inflater)



        foodpickAdapter = createSnackPickAdapter(act.foodShopDetailInfo)
        drinkPickAdapter = createSnackPickAdapter(act.drinkShopDetailInfo)

        val userPickAdapter = createUserPickAdapter()


        val foodShopSnackList = act.foodShopDetailInfo.snackList
        val drinkShopSnackList = act.drinkShopDetailInfo.snackList


        foodShopSnackList?.observe(viewLifecycleOwner) {
            it.let {
                foodpickAdapter?.submitList(it)
            }
        }
        drinkShopSnackList?.observe(viewLifecycleOwner){
            it.let {
                drinkPickAdapter?.submitList(it)
            }
        }


        mainViewModel.userPickInfo.observe(viewLifecycleOwner){
            userPickAdapter.submitList(it)
        }


        val recyclerView = pickSnackFragmentBinding.pickSnackRecycler
        recyclerView.adapter = userPickAdapter


        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        pickSnackFragmentBinding.snackSwipe.setOnRefreshListener{
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
                    val filterUserSnackInfoDtoList = ArrayList<UserSnackInfoDto>()
                    for (userinfo in mainViewModel.userPickInfo?.value!!) {

                        if (userinfo.name.contains(s.toString())) {
                            Log.d("test","after : ${userinfo.name}")
                            filterUserSnackInfoDtoList.add(userinfo)
                        }
                    }
                    act.filterUserSnackInfoDtoList.postValue(filterUserSnackInfoDtoList)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.fetchUserPickInfo()

    }

    private fun createSnackPickAdapter(shopDetailInfo: ShopDetailInfo) : SnackPickAdapter{
        val funVal : (String) -> Unit = { str ->snackPickAdapterOnClick(str) }
        val snackPickAdapter = SnackPickAdapter(funVal)
        snackPickAdapter.submitList(shopDetailInfo.snackList.value)

        return snackPickAdapter
    }

    private fun createUserPickAdapter(): UserPickAdapter {
//        val funVal: (UserSnackInfo) -> Unit = { userSnackInfo -> userPickAdapterNameOnClick(userSnackInfo) }
        val funFoodBtnVal: (UserSnackInfoDto, Int) -> Unit = { userSnackInfo, position-> userFoodPickAdapterButtonOnClick(userSnackInfo,position) }
        val funDrinkBtnVal: (UserSnackInfoDto, Int) -> Unit = { userSnackInfo, position-> userDrinkPickAdapterButtonOnClick(userSnackInfo,position) }
        val userPickAdapter = UserPickAdapter(funFoodBtnVal, funDrinkBtnVal)

        return userPickAdapter
    }

    private fun snackPickAdapterOnClick(snackName : String){
        selectSnack?.text=snackName
        Log.d("onclick",snackName)
    }

    private fun userPickAdapterNameOnClick(userSnackInfoDto: UserSnackInfoDto){

        Log.d("onclick","name click")
    }

    private fun userFoodPickAdapterButtonOnClick(userSnackInfoDto : UserSnackInfoDto, position: Int) : String{
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
            val currentList = act.filterUserSnackInfoDtoList.value
            val newList = act.filterUserSnackInfoDtoList.value?.clone() as ArrayList<UserSnackInfoDto>
            val currentUser = currentList?.get(position)
            val updateUser = currentUser?.copy()
            updateUser?.food = selectSnack?.text.toString()
            newList.set(position,updateUser!!)
            act.filterUserSnackInfoDtoList.postValue(newList)
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


                    val userSnackInfoDtoList2 = ArrayList<UserSnackInfoDto>()

                    val data = root.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val userSnackData = data.getJSONObject(i)
                        val id = userSnackData.getInt("id")
                        val name = userSnackData.getString("name")
                        val food = userSnackData.getString("food")
                        val foodOption = userSnackData.getString("foodOption")
                        val drink = userSnackData.getString("drink")
                        val drinkOption = userSnackData.getString("drinkOption")
                        val userInfo = UserSnackInfoDto(id,name, food, foodOption, drink, drinkOption)

                        userSnackInfoDtoList2.add(userInfo)
                    }
//                    act.filterUserSnackInfoList.postValue(userSnackInfoList2)
                    mainViewModel.userPickInfo.postValue(userSnackInfoDtoList2)

                }
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
        return "btn!!"
    }


    private fun userDrinkPickAdapterButtonOnClick(userSnackInfoDto : UserSnackInfoDto, position: Int) : String{
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
            val currentList = act.filterUserSnackInfoDtoList.value
            val newList = act.filterUserSnackInfoDtoList.value?.clone() as ArrayList<UserSnackInfoDto>
            val currentUser = currentList?.get(position)
            val updateUser = currentUser?.copy()
            updateUser?.drink = selectSnack?.text.toString()
            newList.set(position,updateUser!!)
            act.filterUserSnackInfoDtoList.postValue(newList)
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


                    val userSnackInfoDtoList2 = ArrayList<UserSnackInfoDto>()

                    val data = root.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val userSnackData = data.getJSONObject(i)
                        val id = userSnackData.getInt("id")
                        val name = userSnackData.getString("name")
                        val food = userSnackData.getString("food")
                        val foodOption = userSnackData.getString("foodOption")
                        val drink = userSnackData.getString("drink")
                        val drinkOption = userSnackData.getString("drinkOption")
                        val userInfo = UserSnackInfoDto(id,name, food, foodOption, drink, drinkOption)

                        userSnackInfoDtoList2.add(userInfo)

                    }
//                    act.filterUserSnackInfoList.postValue(userSnackInfoList2)
                    mainViewModel.userPickInfo.postValue(userSnackInfoDtoList2)

                }
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
        return "btn!!"
    }

}
