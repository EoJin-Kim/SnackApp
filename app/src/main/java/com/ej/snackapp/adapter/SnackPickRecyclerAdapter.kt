package com.ej.snackapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.MainActivity
import com.ej.snackapp.ServerInfo
import com.ej.snackapp.UserSnackInfo
import com.ej.snackapp.databinding.SnackPickRowBinding
import com.ej.snackapp.fragment.PickSnackFragment

//  RecyclerView의 Adapter 클래스
class SnackPickRecyclerAdapter : RecyclerView.Adapter<SnackPickRecyclerAdapter.ViewHolderClass>(){

    var filterUserSnackInfoList = mutableListOf<UserSnackInfo>()


    lateinit var act : MainActivity


    // 항목 구성을 위해 사용할 ViewHolder 객체가 필요할 떄 호출되는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        // 항목으로 사용할 View 객체를 생성한다.
//        val snackPickRowBinding = SnackPickRowBinding.inflate(layoutInflater)
        val snackPickRowBinding = SnackPickRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

        holder.idx = position

        holder.userId = userInfo.id


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
    inner class ViewHolderClass(snackPickRowBinding: SnackPickRowBinding) : RecyclerView.ViewHolder(snackPickRowBinding.root),
        View.OnClickListener {

        private val mainActivity = MainActivity.getInstance()
        private val pickSnackFragment = PickSnackFragment.getInstance()

        // 항목 View 내부의 View 객체의 주소값을 담는다

        var idx:Int = -1

        var userId:Int = 0

        val snackPickName = snackPickRowBinding.snackPickName
        val snackPickDessertBtn = snackPickRowBinding.snackPickDessertBtn
        val snackPickDrinkBtn = snackPickRowBinding.snackPickDrinkBtn

        init {
            snackPickRowBinding.snackPickDessertBtn.setOnClickListener {
                Log.d("test","${snackPickName.text}")
            }

            snackPickRowBinding.snackPickDrinkBtn.setOnClickListener {

            }

        }
        override fun onClick(v: View?) {
//                textView.text = data1[adapterPosition]
//            pickSnack(adapterPosition)
            pickSnackFragment?.pickSnackFragmentBinding?.nameInput?.setText("흥부")
        }
    }
}