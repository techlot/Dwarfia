package com.example.dwarfia.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.dwarfia.DwarfsListFullDirections
import com.example.dwarfia.R
import com.example.dwarfia.database.Dwarf2

class DwarfsListBigAdapter: ListAdapter<Dwarf2, DwarfsListBigAdapter.DwarfViewHolder>(DwarfsComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DwarfViewHolder {
        return DwarfViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DwarfViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name, current.longitude, current.latitude, current.address, current.description, current.img_link, current.star, current.heart, current.thumb_up, current.visited)
    }

    class DwarfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name_view: TextView = itemView.findViewById(R.id.dwarf_name_txt)
        private val stars_view: TextView = itemView.findViewById(R.id.stars_count_view)
        private val hearts_view: TextView = itemView.findViewById(R.id.hearts_count_view)
        private val thumbs_up_view: TextView = itemView.findViewById(R.id.thumbs_count_view)
        private val image_view: ImageView = itemView.findViewById(R.id.dwarf_image_view)

        init {
            itemView.setOnClickListener{
                val name_text_view : TextView = itemView.findViewById(R.id.dwarf_name_txt)
                val name = name_text_view.text.toString()
                val action = DwarfsListFullDirections.actionDwarfsListFullToDwarfDetailsFragment(name)
                itemView.findNavController().navigate(action)
            }
        }

        fun bind(name: String?, longitude: String?, latitude: String?, address: String?, description: String?, img_link: String?, star: Map<String, String>?, heart: Map<String, String>?, thumb_up: Map<String, String>?, visited: Map<String, String>?) {
            name_view.text = name
            stars_view.text = star!!.size.toString()
            hearts_view.text = heart!!.size.toString()
            thumbs_up_view.text = thumb_up!!.size.toString()

            img_link?.let {
                val imgUrl = it.toUri().buildUpon().scheme("https").build()
                Glide.with(image_view.context)
                    .load(imgUrl)
                    .placeholder((R.drawable.loading_animation))
                    .error(R.drawable.dwarf_outlined)
                    .transform(CenterCrop(), RoundedCorners(25))
                    .into(image_view)
            }

        }

        companion object {
            fun create(parent: ViewGroup): DwarfViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.dwarf_tile_rec_view_big, parent, false)
                return DwarfViewHolder(view)
            }
        }
    }

    class DwarfsComparator : DiffUtil.ItemCallback<Dwarf2>() {
        override fun areItemsTheSame(oldItem: Dwarf2, newItem: Dwarf2): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Dwarf2, newItem: Dwarf2): Boolean {
            return oldItem.name == newItem.name
        }
    }
}