package com.example.testapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(var spanCount: Int) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if (viewType == 0) MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent,
                false
            )
        ) else MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_header,
                parent,
                false
            )
        )//To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return 100 //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemViewType(position: Int): Int {
        return if ((position + 1) % spanCount == 1) 1 else 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //To change body of created functions use File | Settings | File Templates.
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var myView: View = itemView

        init {
            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "$adapterPosition",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}