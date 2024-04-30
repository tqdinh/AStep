package com.example.home.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.home.databinding.CustomPlaceBinding
import com.inter.entity.planner.PlaceEntity


class PlaceAdapter() : BaseAdapter() {

    var listPlaces: MutableList<PlaceEntity> = mutableListOf()

    fun submitNewList(newlist: MutableList<PlaceEntity>) {
        listPlaces = newlist
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return listPlaces.size
    }

    override fun getItem(position: Int): Any {
        return listPlaces.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            CustomPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val windowManager = parent.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthPixels = displayMetrics.widthPixels
        val heightPixels = displayMetrics.heightPixels


        val layoutParams = ViewGroup.LayoutParams(widthPixels, (heightPixels * 0.3).toInt())
        view.root.layoutParams = layoutParams

        val imageView = ImageView(parent.context)
        imageView.layoutParams = ConstraintLayout.LayoutParams(
            (heightPixels * 0.2).toInt(), (heightPixels * 0.2).toInt()
        )


        listPlaces?.get(position)?.listImage?.forEach {
            Glide.with(view.root.context)
                .load(it.path)
                .apply(RequestOptions().centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
            view.llImages.addView(imageView)
        }



        view.tvPlaceOrder.text = "" + position



        return view.root
    }

}