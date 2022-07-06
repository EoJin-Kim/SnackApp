package com.ej.snackapp.fragment

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ej.snackapp.*
import com.ej.snackapp.adapter.UserPickRecyclerAdapter
import com.ej.snackapp.databinding.FragmentPickSnackBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.concurrent.thread


class PickSnackFragment : Fragment() {

    lateinit var pickSnackFragmentBinding : FragmentPickSnackBinding

//    val nameList = arrayOf(
//        "홍길동1","홍길동2","홍길동3","홍길동4","홍길동5","홍길동6","홍길동7","홍길동8"
//    )
    var filterUserSnackInfoList = mutableListOf<UserSnackInfo>()



    init{
        instance = this
    }

    companion object{
        private var instance:PickSnackFragment? = null
        fun getInstance(): PickSnackFragment? {
            return instance
        }
    }

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


        val userPickRecyclerAdapter = UserPickRecyclerAdapter()
        userPickRecyclerAdapter.filterUserSnackInfoList = filterUserSnackInfoList
        userPickRecyclerAdapter.drinkShopDetailInfoList = act.drinkShopDetailInfoList
        userPickRecyclerAdapter.foodShopDetailInfoList = act.foodShopDetailInfoList

        pickSnackFragmentBinding.pickSnackRecycler.adapter = userPickRecyclerAdapter

        pickSnackFragmentBinding.pickSnackRecycler.layoutManager = LinearLayoutManager(requireContext())
//        pickSnackFragmentBinding.nameInput.setAdapter()

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

    override fun onStart() {
        super.onStart()


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
