package com.ej.snackapp.fragment

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.MainActivity
import com.ej.snackapp.ServerInfo
import com.ej.snackapp.UserSnackInfo
import com.ej.snackapp.databinding.FragmentPickSnackBinding
import com.ej.snackapp.databinding.SnackPickRowBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.concurrent.thread


class PickSnackFragment : Fragment() {


    lateinit var pickSnackFragmentBinding : FragmentPickSnackBinding


    val nameList = arrayOf(
        "홍길동1","홍길동2","홍길동3","홍길동4","홍길동5","홍길동6","홍길동7","홍길동8"
    )
    var filterUserSnackInfoList = ArrayList<UserSnackInfo>()

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


        getUserSnackList()

        val snackPickRecyclerAdapter = SnackPickRecyclerAdapter()
        pickSnackFragmentBinding.pickSnackRecycler.adapter = snackPickRecyclerAdapter

        pickSnackFragmentBinding.pickSnackRecycler.layoutManager = LinearLayoutManager(requireContext())
//        pickSnackFragmentBinding.nameInput.setAdapter()

        pickSnackFragmentBinding.snackSwipe.setOnRefreshListener{
            getUserSnackList()
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
                    for (userinfo in act.UserSnackInfoList) {

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



    //  RecyclerView의 Adapter 클래스
    inner class SnackPickRecyclerAdapter : RecyclerView.Adapter<SnackPickRecyclerAdapter.ViewHolderClass>(){

        // 항목 구성을 위해 사용할 ViewHolder 객체가 필요할 떄 호출되는 메서드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            // 항목으로 사용할 View 객체를 생성한다.
            val snackPickRowBinding = SnackPickRowBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(snackPickRowBinding)
            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            snackPickRowBinding.root.layoutParams = layoutParams
            snackPickRowBinding.root.setOnClickListener(holder)

            return holder
        }

        // ViewHolder를 통해 항목을 구성할 떄 항목 내의 View 객체에 데이터를 셋팅한다.
        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

            val userInfo = filterUserSnackInfoList[position]

            holder.snackPickName.text = userInfo.name

            if(userInfo.food=="" || userInfo.food==null){
                holder.snackPickDessertBtn.text = "간식을 선택해주세요"
            }
            else{
                holder.snackPickDessertBtn.text = "${userInfo.food} : ${userInfo.foodOption}"
            }

            if(userInfo.food=="" || userInfo.food==null){
                holder.snackPickDrinkBtn.text = "음료를 선택해주세요"
            }
            else{
                holder.snackPickDrinkBtn.text = "${userInfo.drink} : ${userInfo.drinkOption}"
            }
        }

        // RecyclerView 항목 개수를 반한
        override fun getItemCount(): Int {
            return filterUserSnackInfoList.size
        }

        //ViewHolder 클래스
        inner class ViewHolderClass(snackPickRowBinding: SnackPickRowBinding) : RecyclerView.ViewHolder(snackPickRowBinding.root),View.OnClickListener {
            // 항목 View 내부의 View 객체의 주소값을 담는다
            val snackPickName = snackPickRowBinding.snackPickName
            val snackPickDessertBtn = snackPickRowBinding.snackPickDessertBtn
            val snackPickDrinkBtn = snackPickRowBinding.snackPickDrinkBtn

            override fun onClick(v: View?) {
//                textView.text = data1[adapterPosition]
                pickSnack(adapterPosition)
            }
        }
    }

    fun getUserSnackList(){
        filterUserSnackInfoList.clear()


        thread {
            val client = OkHttpClient()

            val act = activity as MainActivity

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

                    act.UserSnackInfoList.add(userInfo)
                    filterUserSnackInfoList.add(userInfo)

                }
                act?.runOnUiThread {
                    pickSnackFragmentBinding.pickSnackRecycler.adapter?.notifyDataSetChanged()
                }

            }
        }
    }
    fun pickSnack(idx:Int){

    }
}
