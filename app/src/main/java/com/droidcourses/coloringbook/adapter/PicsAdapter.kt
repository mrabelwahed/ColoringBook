package com.droidcourses.coloringbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.droidcourses.coloringbook.PicsHolder
import com.droidcourses.coloringbook.R
import com.droidcourses.coloringbook.callback.OnItemClick

class PicsAdapter(private val data: List<Int>): RecyclerView.Adapter<PicsAdapter.PicViewHolder>() {
    lateinit var  itemClick: OnItemClick
    fun setClickListener(itemClick: OnItemClick) {
        this.itemClick = itemClick
    }

    inner class PicViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
       fun bind(index: Int) {
           val img = itemView.findViewById<ImageView>(R.id.pic)
           img.setImageResource(PicsHolder.pics[index])
           img.setOnClickListener {
             itemClick.onItemClicked(index)
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicViewHolder {
       val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.item_image, null)
        return PicViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return data.size
    }

    override fun onBindViewHolder(holder: PicViewHolder, position: Int) {
       holder.bind(position)
    }
}