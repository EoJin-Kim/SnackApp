package com.ej.snackapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.MainActivity
import com.ej.snackapp.R
import com.ej.snackapp.info.ShopDetailInfo
import com.ej.snackapp.data.UserSnackInfo
import com.ej.snackapp.databinding.SnackPickRowBinding
import com.ej.snackapp.fragment.PickSnackFragment

//  RecyclerView의 Adapter 클래스
class UserPickRecyclerAdapter : RecyclerView.Adapter<UserPickRecyclerAdapter.ViewHolderClass>(){

//    var mainActivity: MainActivity? = null
    var filterUserSnackInfoList = mutableListOf<UserSnackInfo>()
    var foodShopName : String? = null
    var drinkShopName : String? = null
    var foodShopDetailInfo : ShopDetailInfo? = null
    var drinkShopDetailInfo : ShopDetailInfo? = null


    // 항목 구성을 위해 사용할 ViewHolder 객체가 필요할 떄 호출되는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        // 항목으로 사용할 View 객체를 생성한다.
//        val snackPickRowBinding = SnackPickRowBinding.inflate(layoutInflater)
        val snackPickRowBinding = SnackPickRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val holder = ViewHolderClass(snackPickRowBinding)
        holder.context = parent.context
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

//        holder.mainActivity = mainActivity
        holder.foodShopName = foodShopName
        holder.drinkShopName = drinkShopName
        holder.foodShopDetailInfo = foodShopDetailInfo
        holder.drinkShopDetailInfo = drinkShopDetailInfo

        holder.idx = position

        holder.userId = userInfo.id


        holder.snackPickName.text = userInfo.name

        if(userInfo.food=="" || userInfo.food==null){
            holder.pickFoodBtn.text = "간식을 선택해주세요"
        }
        else{
            holder.pickFoodBtn.text = "${userInfo.food} : ${userInfo.foodOption}"
        }

        if(userInfo.food=="" || userInfo.food==null){
            holder.pickDrinkBtn.text = "음료를 선택해주세요"
        }
        else{
            holder.pickDrinkBtn.text = "${userInfo.drink} : ${userInfo.drinkOption}"
        }
    }

    // RecyclerView 항목 개수를 반한
    override fun getItemCount(): Int {
        return filterUserSnackInfoList.size
    }

    //ViewHolder 클래스
    inner class ViewHolderClass(snackPickRowBinding: SnackPickRowBinding) : RecyclerView.ViewHolder(snackPickRowBinding.root), View.OnClickListener {

        var mainActivity: MainActivity? = null
        private var pickSnackFragment : PickSnackFragment? = null
        var context:Context? = null

        // 항목 View 내부의 View 객체의 주소값을 담는다
        var foodShopName : String? = null
        var drinkShopName : String? = null

        var foodShopDetailInfo : ShopDetailInfo? = null
        var drinkShopDetailInfo : ShopDetailInfo? = null

        var idx:Int = -1

        var userId:Int = 0

        val snackPickName = snackPickRowBinding.snackPickName
        val pickFoodBtn = snackPickRowBinding.pickFoodBtn
        val pickDrinkBtn = snackPickRowBinding.pickDrinkBtn

        init {


            pickFoodBtn.setOnClickListener {



                val layoutInflater = LayoutInflater.from(context)
                val view = layoutInflater.inflate(R.layout.alert_dialog,null)
                val alertDialog = AlertDialog.Builder(context!!)
                    .setView(view)
                    .create()

                val snackRecycler = view.findViewById<RecyclerView>(R.id.snack_recycler)
                val shopNameText = view.findViewById<TextView>(R.id.shop_name)
                val selectSnack = view.findViewById<TextView>(R.id.select_snack)
                val confirmButton = view.findViewById<View>(R.id.choice_btn)

                val snackRecyclerAdapter = SnackRecyclerAdapter(textView = selectSnack) { snack ->
                    Log.d("alert", "snack=$snack")
                    selectSnack.text = snack
                }
                snackRecyclerAdapter.shopDetailInfo = foodShopDetailInfo

                snackRecycler.adapter = snackRecyclerAdapter
                snackRecycler.layoutManager = LinearLayoutManager(context)

                shopNameText.text = foodShopName
                confirmButton.setOnClickListener{
                    alertDialog.dismiss()
                }
                alertDialog.show()



                Log.d("test","${snackPickName.text}")
            }

            pickDrinkBtn.setOnClickListener {

            }

        }

        private fun onClickSnack(snack: String){

        }
        override fun onClick(v: View?) {
//                textView.text = data1[adapterPosition]
//            pickSnack(adapterPosition)
            pickSnackFragment?.pickSnackFragmentBinding?.nameInput?.setText("흥부")
        }
    }
}