package com.inter.planner.plan

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inter.planner.databinding.JourneyCustomeViewBinding
import com.inter.planner.entity.JourneyEntity


class EventAdapter(
    private val onItemClickListener: OnItemClickListener,
) : ListAdapter<JourneyEntity, EventAdapter.EventViewHolder>(object :
    DiffUtil.ItemCallback<JourneyEntity>() {
    override fun areItemsTheSame(oldItem: JourneyEntity, newItem: JourneyEntity): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: JourneyEntity, newItem: JourneyEntity): Boolean {
        return oldItem.equals(newItem)

    }
}) {

    interface OnItemClickListener {
        fun onItemClick(item: JourneyEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding =
            JourneyCustomeViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActiveEventViewHolder(binding, parent.context)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == currentList.size - 1) {
            return 1
        } else if (position == 0) {
            return 0
        }
        return 2
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position), position)

    }

    inner class ActiveEventViewHolder(binding: JourneyCustomeViewBinding, context: Context) :
        EventViewHolder(binding, onItemClickListener, context) {
        init {
//            val layoutParams = binding.layoutItemEvent.layoutParams as ViewGroup.MarginLayoutParams

//            if (currentList.size == 1) {
//                layoutParams.leftMargin = dp2px(26F)
//                layoutParams.rightMargin = dp2px(26F)
//            }
//            if (currentList.size > 1) {
//                layoutParams.width = (getScreenWidth(itemView.context) * 0.85).toInt()
//                layoutParams.rightMargin = dp2px(10F)
//            }
//            binding.btnContinue.visibility = View.VISIBLE
            //      binding.descEvent.visibility = View.GONE
        }

        override fun bind(item: JourneyEntity, position: Int) {
            super.bind(item, position)
            //  val layoutParams = binding.layoutItemEvent.layoutParams as ViewGroup.MarginLayoutParams
//            if (position == 0) {
//                layoutParams.leftMargin = dp2px(26F)
//            } else if (position == currentList.size - 1) {
//                layoutParams.leftMargin = 0
//                layoutParams.rightMargin = dp2px(26F)
//            }
//
//            checkStatusDownload(item)
//            downloadImage(item, mapPhotoDetail) { getImageUrlFromId(it, position) }
        }


    }

    open class EventViewHolder(

        val binding: JourneyCustomeViewBinding,
        private val onItemClickListener: OnItemClickListener,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
//            binding.imvEvent.clipToOutline = true
//            bindPhoto("", binding.imvEvent)
        }

        init {

        }

        open fun bind(item: JourneyEntity, position: Int) {
            val totalImage = item.listPlaces.flatMap {
                it.listImage
            }.count()
            binding.tvJourneyName.text = item.title
            binding.tvPlaceNumber.text = item.listPlaces.size.toString()
            binding.tvPlaceTotalImg.text = totalImage.toString()
            var tmp=0
            for (place in item.listPlaces) {
                if(tmp>=5)
                    break

                val imgPath = place.listImage[0].path
                val imageView = ImageView(context)

                bindPhoto(
//                    "https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png",
                    imgPath,
                    imageView
                )
                binding.llPlaces.addView(imageView)
                tmp+=1
            }


//            binding.txtTitleEvent.text = item.title
//            binding.tvDateTime.text = DateTimeUtils.getDateString(item.timestamp)
//            binding.descEvent.text = Util.parseHtmlToStyledTextOrEmpty(item.shortDesc)
//            binding.btnContinue.setOnClickListener { onItemClickListener.onItemClick(item) }
//            binding.btnGetStarted.setOnClickListener { onItemClickListener.onItemClick(item) }

            binding.root.setOnClickListener {
                onItemClickListener.onItemClick(item)
            }
        }


        private fun bindPhoto(url: String, imageView: ImageView) {
            Glide.with(itemView.context)
                .load(url)
//                .placeholder(circularProgressDrawable)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)
        }
    }

}