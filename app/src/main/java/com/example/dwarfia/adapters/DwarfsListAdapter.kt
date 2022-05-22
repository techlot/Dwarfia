package com.example.dwarfia.adapters

import android.graphics.Outline
import android.graphics.drawable.Animatable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.dwarfia.MainFragmentDirections
import com.example.dwarfia.R
import com.example.dwarfia.database.Dwarf
import kotlinx.coroutines.awaitAll

class DwarfsListAdapter: ListAdapter<Dwarf, DwarfsListAdapter.DwarfViewHolder>(DwarfsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DwarfViewHolder {
        return DwarfViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DwarfViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name, current.stars_count, current.hearts_count, current.thumbs_up_count, current.image, current.address, current.description)
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
                    val action = MainFragmentDirections.actionMainFragmentToDwarfDetailsFragment3(name)
                //Navigation.findNavController(itemView.rootView).navigate(action)

                itemView.findNavController().navigate(action)
            }
        }

        fun bind(name: String?, stars: Int?, hearts: Int?, thumbs_up: Int?, image: String?, address: String?, description: String?) {
            name_view.text = name
            stars_view.text = stars.toString()
            hearts_view.text = hearts.toString()
            thumbs_up_view.text = thumbs_up.toString()

            //image_view.setImageURI(image?.toUri())



            image?.let {
                val imgUrl = it.toUri().buildUpon().scheme("https").build()
                Glide.with(image_view.context)
                    .load(imgUrl)
                    .placeholder((R.drawable.loading_animation))
                    .error(R.drawable.dwarf)
                    .transform(CenterCrop(), RoundedCorners(25))
                    .into(image_view)
            }

        }

        companion object {
            fun create(parent: ViewGroup): DwarfViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.dwarf_tile_rec_view, parent, false)
                return DwarfViewHolder(view)
            }
        }
    }

    class DwarfsComparator : DiffUtil.ItemCallback<Dwarf>() {
        override fun areItemsTheSame(oldItem: Dwarf, newItem: Dwarf): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Dwarf, newItem: Dwarf): Boolean {
            return oldItem.name == newItem.name
        }
    }
}