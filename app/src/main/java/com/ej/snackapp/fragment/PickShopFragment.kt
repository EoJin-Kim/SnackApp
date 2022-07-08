package com.ej.snackapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.MainActivity
import com.ej.snackapp.R
import com.ej.snackapp.adapter.ShopPickAdapter
import com.ej.snackapp.adapter.SnackPickAdapter
import com.ej.snackapp.adapter.UserPickAdapter
import com.ej.snackapp.data.UserSnackInfo
import com.ej.snackapp.databinding.FragmentPickShopBinding
import com.ej.snackapp.info.ServerInfo
import com.ej.snackapp.info.ShopInfo
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


class PickShopFragment : Fragment() {

    lateinit var pickShopFragmentBinding : FragmentPickShopBinding

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
//        apiInit()


        val foodShopInfoList = act.foodShopInfoList
        foodShopInfoList.add(ShopInfo(11,"df","df"))
        foodShopInfoList.add(ShopInfo(11,"df","df"))
        foodShopInfoList.add(ShopInfo(11,"df","df"))
        foodShopInfoList.add(ShopInfo(11,"df","df"))
        foodShopInfoList.add(ShopInfo(11,"df","df"))
        foodShopInfoList.add(ShopInfo(11,"df","df"))
        val drinkShopInfoList = act.drinkShopInfoList

        val foodShopPickAdapter = createShopPickAdapter()





        pickShopFragmentBinding.foodShopBtn.setOnClickListener {

            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.shop_pick_dialog,null)
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(view)
                .create()
            val shopRecycler = view.findViewById<RecyclerView>(R.id.shop_recycler)

            shopRecycler.adapter = foodShopPickAdapter
            shopRecycler.layoutManager = LinearLayoutManager(requireContext())
            foodShopPickAdapter.submitList(foodShopInfoList)

            alertDialog.show()



        }

        pickShopFragmentBinding.drinkShopBtn.setOnClickListener {

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
        // shop 선택 api 보내고
        // 받은 데이터를 view에 셋팅
    }

}