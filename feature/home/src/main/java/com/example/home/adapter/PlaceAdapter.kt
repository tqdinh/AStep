package com.example.home.adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.home.databinding.CustomImageBinding
import com.example.home.databinding.CustomPlaceBinding
import com.inter.entity.planner.PlaceEntity
import java.text.SimpleDateFormat
import java.util.Date


class PlaceAdapter(val onItemSelectOptionListener: OnItemSelectOptionListener) : BaseAdapter() {

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


        val customImageBinding: CustomImageBinding =
            CustomImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

//        val imageView = ImageView(parent.context)
        val imageView = customImageBinding.ivPhoto
        imageView.layoutParams = ConstraintLayout.LayoutParams(
            (heightPixels * 0.2).toInt(), (heightPixels * 0.2).toInt()
        )


        listPlaces?.get(position)?.listImage?.forEach {
            Glide.with(customImageBinding.root.context)
                .load(it.path)
                .apply(RequestOptions().centerCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
            view.llImages.addView(customImageBinding.root)
        }



        view.tvPlaceOrder.text = "" + position

        listPlaces?.get(position)?.apply {

            val date = Date(this.timestamp)
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val formattedDateTime: String = sdf.format(date)
            view.tvPlaceTime.text = formattedDateTime
        }
        view.llDeletePlace.setOnClickListener {
            onItemSelectOptionListener.onDeletePlace(position)
        }


        return view.root
    }

    interface OnItemSelectOptionListener {
        fun onDeletePlace(position: Int)
        fun onNavigatePlace(position: Int)
        fun onSharePlace(position: Int)

        fun onSelectPlace(position: Int)
    }

}