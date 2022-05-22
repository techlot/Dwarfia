package com.example.dwarfia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.dwarfia.database.Dwarf
import com.example.dwarfia.databinding.FragmentDwarfDetailsBinding
import com.example.dwarfia.models.DwarfViewModelFactory
import com.example.dwarfia.models.DwarfsViewModel
import kotlinx.android.synthetic.main.fragment_dwarf_details.*
import kotlinx.coroutines.*
import java.util.*


class DwarfDetailsFragment : Fragment() {
    val args: DwarfDetailsFragmentArgs by navArgs()
    private val viewModel: DwarfsViewModel by activityViewModels {
        DwarfViewModelFactory((activity?.application as DwarfsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        val binding: FragmentDwarfDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dwarf_details, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        binding.dwarf = null
        binding.name = args.name
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.dwarf_description_text_view).movementMethod = ScrollingMovementMethod()
        //val dwarf_name = args.name
        //view.findViewById<TextView>(R.id.dawarf_name_text_view).text = dwarf_name

        /*viewModel.getSingleDwarf(args.name).observe(viewLifecycleOwner, androidx.lifecycle.Observer<Dwarf> {
                dwarf ->
            requireView().findViewById<TextView>(R.id.dwarf_name_txt).text = dwarf.name
        })*/
        viewModel.name = args.name
        var image = ""
        var visited = -1

        /*runBlocking {val tasks = listOf(
            lifecycleScope.async(Dispatchers.IO){
                val dwarf = viewModel.getSingleDwarf(args.name)

                //image = dwarf.image
                //visited = dwarf.visited
                //view.findViewById<TextView>(R.id.dwarf_address_text_view).text = dwarf.address
                //view.findViewById<TextView>(R.id.dwarf_description_text_view).text = dwarf.description
                delay(50)
            }
        )
            tasks.awaitAll()
        }*/



        /*val image_view = view.findViewById<ImageView>(R.id.imageView)
        image.let {
            val imgUrl = it.toUri().buildUpon().scheme("https").build()
            Glide.with(image_view.context)
                .load(imgUrl)
                .placeholder((R.drawable.loading_animation))
                .error(R.drawable.ic_broken_image)
                .transform(CenterCrop())
                .into(image_view)
        }*/

                    //1 -> viewModel.markAsVisited(dwarf_name)


        view.findViewById<Button>(R.id.heart_button).setOnClickListener{
            lifecycleScope.launch {
                //viewModel.markAsHearted(dwarf_name)
            }
        }

        view.findViewById<Button>(R.id.navigate_button).setOnClickListener{
            val address = dwarf_address_text_view.text
            val gmmIntentUri =
                Uri.parse("google.navigation:q=$address,+Wroclaw+Poland")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
}