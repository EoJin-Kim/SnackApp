package com.ej.snackapp.fragment

import android.app.Activity
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.*
import com.ej.snackapp.adapter.SnackRecyclerAdapter
import com.ej.snackapp.adapter.UserPickAdapter
import com.ej.snackapp.adapter.UserPickRecyclerAdapter
import com.ej.snackapp.databinding.FragmentPickSnackBinding
import com.ej.snackapp.info.ServerInfo
import com.ej.snackapp.data.UserSnackInfo
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.concurrent.thread


class PickSnackFragment : Fragment() {

    lateinit var pickSnackFragmentBinding : FragmentPickSnackBinding

    var filterUserSnackInfoList = mutableListOf<UserSnackInfo>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val act = activity as MainActivity
        pickSnackFragmentBinding = FragmentPickSnackBinding.inflate(inflater)

        getUserSnackList(true)


//        val userPickRecyclerAdapter = UserPickRecyclerAdapter()
//
//        userPickRecyclerAdapter.filterUserSnackInfoList = filterUserSnackInfoList
//        userPickRecyclerAdapter.foodShopName = act.nowFoodType
//        userPickRecyclerAdapter.drinkShopName = act.nowDrinkType
//        userPickRecyclerAdapter.drinkShopDetailInfo = act.drinkShopDetailInfo
//        userPickRecyclerAdapter.foodShopDetailInfo = act.foodShopDetailInfo
//
//        pickSnackFragmentBinding.pickSnackRecycler.adapter = userPickRecyclerAdapter
//
//        pickSnackFragmentBinding.pickSnackRecycler.layoutManager = LinearLayoutManager(requireContext())

        val funVal : (UserSnackInfo) -> Unit = { userSnackInfo -> adapterOnClick(userSnackInfo) }
        val funBtnVal : () -> String = {  -> adapterButtonOnClick() }
        val userPickAdapter = UserPickAdapter(funVal,funBtnVal)
        val recyclerView = pickSnackFragmentBinding.pickSnackRecycler
        recyclerView.adapter = userPickAdapter

        userPickAdapter.submitList(act.userSnackInfoList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        pickSnackFragmentBinding.snackSwipe.setOnRefreshListener{
            getUserSnackList(true)
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
                    filterUserSnackInfoList.clear()
                    for (userinfo in act.userSnackInfoList) {

                        if (userinfo.name.contains(s.toString())) {
                            Log.d("test","after : ${userinfo.name}")
                            filterUserSnackInfoList.add(userinfo)
                        }
                    }
                    act?.runOnUiThread {
                        pickSnackFragmentBinding.pickSnackRecycler.adapter?.notifyDataSetChanged()
                    }

                }

            }
        }
        pickSnackFragmentBinding.nameInput.addTextChangedListener(textChangeListener)

        return pickSnackFragmentBinding.root

    }

    private fun adapterOnClick(userSnackInfo: UserSnackInfo){

        Log.d("onclick","name click")
    }

    private fun adapterButtonOnClick() : String{
        val act = activity as MainActivity
        Log.d("onclick","btn click")

        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.alert_dialog,null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        val snackRecycler = view.findViewById<RecyclerView>(R.id.snack_recycler)
        val shopNameText = view.findViewById<TextView>(R.id.shop_name)
        val selectSnack = view.findViewById<TextView>(R.id.select_snack)
        val confirmButton = view.findViewById<View>(R.id.choice_btn)


//        snackRecycler.adapter = snackRecyclerAdapter
        snackRecycler.layoutManager = LinearLayoutManager(context)

        shopNameText.text = act.nowFoodType
        confirmButton.setOnClickListener{
            alertDialog.dismiss()
        }
        alertDialog.show()
        return "btn!!"
    }




    fun getUserSnackList(filter:Boolean){
        val act = activity as MainActivity
        act.userSnackInfoList.clear()
        filterUserSnackInfoList.clear()


        thread {

            val client = OkHttpClient()

            val url = "${ServerInfo.SERVER_URL}/api/snack/pick"

            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if(response.isSuccessful){
                val resultText = response.body?.string()!!.trim()
                Log.d("test",resultText.toString())
                val root = JSONObject(resultText)



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

                    act.userSnackInfoList.add(userInfo)
                    filterUserSnackInfoList.add(userInfo)

                }
                act?.runOnUiThread {
                    pickSnackFragmentBinding.pickSnackRecycler.adapter?.notifyDataSetChanged()
                }

            }
        }
    }


}
