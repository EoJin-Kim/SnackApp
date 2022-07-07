package com.ej.snackapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ej.snackapp.R
import com.ej.snackapp.info.ShopDetailInfo
import com.ej.snackapp.databinding.SnackListRowBinding

class SnackRecyclerAdapter(private val textView: TextView, private val onClick: (String) -> Unit) : RecyclerView.Adapter<SnackRecyclerAdapter.ViewHolderClass>() {


    var shopDetailInfo : ShopDetailInfo? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val snackListRowBinding = SnackListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val holder = ViewHolderClass(snackListRowBinding, onClick, textView)
        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        snackListRowBinding.root.layoutParams = layoutParams
        snackListRowBinding.root.setOnClickListener(holder)

        holder.parent = parent

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.snackName.text = shopDetailInfo?.snackList!![position]
    }

    override fun getItemCount(): Int {
        return shopDetailInfo?.snackList!!.size
    }

    //ViewHolder 클래스
    inner class ViewHolderClass(
        snackListRowBinding: SnackListRowBinding,
        onClick: (String) -> Unit,
        private val textView: TextView
    ) : RecyclerView.ViewHolder(snackListRowBinding.root), View.OnClickListener {
        var parent : ViewGroup? = null
        var snackName  = snackListRowBinding.snackName
        var curData : String = ""

        init {
            snackListRowBinding.root.setOnClickListener{onClick(curData)}
        }

        override fun onClick(v: View?) {
            var selectSnack = shopDetailInfo!!.snackList[adapterPosition]
            curData = selectSnack
            val layoutInflater = LayoutInflater.from(parent?.context)
            val view = layoutInflater.inflate(R.layout.alert_dialog,null)
            val selectSnackText = view.findViewById<TextView>(R.id.select_snack)
            textView?.text=selectSnack


            Log.d("alert","${selectSnackText?.text}")

        }
    }

}