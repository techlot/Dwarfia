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
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.dwarfia.adapters.DwarfsListAdapter
import com.example.dwarfia.database.Dwarf
import com.example.dwarfia.database.Dwarf2
import com.example.dwarfia.databinding.FragmentDwarfDetailsBinding
import com.example.dwarfia.models.DwarfViewModelFactory
import com.example.dwarfia.models.DwarfsViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.fragment_dwarf_details.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DwarfDetailsFragment : Fragment() {
    val args: DwarfDetailsFragmentArgs by navArgs()
    private val viewModel: DwarfsViewModel by activityViewModels {
        DwarfViewModelFactory((activity?.application as DwarfsApplication).repository)
    }

    private lateinit var dbref: DatabaseReference
    private lateinit var user_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        user_id = (activity as MainActivity?)!!.getUserId()
        val binding: FragmentDwarfDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dwarf_details, container, false)
        binding.lifecycleOwner = this
        binding.userId=user_id
        dbref = FirebaseDatabase.getInstance("https://dwarfia-7fe62-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Dwarfs")
        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dwarf = snapshot.child(args.name).getValue(Dwarf2::class.java)
                    binding.dwarf = dwarf
                    binding.visited = dwarf!!.visited!!.contains(user_id)
                    binding.starred = dwarf!!.star!!.contains(user_id)
                    binding.hearted = dwarf!!.heart!!.contains(user_id)
                    binding.thumbed = dwarf!!.thumb_up!!.contains(user_id)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


       /* runBlocking {val tasks = listOf(
            lifecycleScope.async(Dispatchers.IO){
                binding.dwarf = viewModel.getSingleDwarf(args.name)

                //image = dwarf.image
                //visited = dwarf.visited
                //view.findViewById<TextView>(R.id.dwarf_address_text_view).text = dwarf.address
                //view.findViewById<TextView>(R.id.dwarf_description_text_view).text = dwarf.description
                delay(50)
            }
        )
            tasks.awaitAll()
        }*/

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

        runBlocking {val tasks = listOf(
            lifecycleScope.async(Dispatchers.IO){
                val dwarf = viewModel.getSingleDwarf(args.name)

                //image = dwarf.image
                visited = dwarf.visited
                //view.findViewById<TextView>(R.id.dwarf_address_text_view).text = dwarf.address
                //view.findViewById<TextView>(R.id.dwarf_description_text_view).text = dwarf.description
                delay(50)
            }
        )
            tasks.awaitAll()
        }



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

        view.findViewById<Button>(R.id.check_button).setOnClickListener{
            dbref.child(args.name).child("visited").get().addOnSuccessListener {
                var res: Map<String, String>? =  it.getValue() as Map<String, String>
                if (res!!.contains(user_id)){
                    // remove
                    dbref.child(args.name).child("visited").child(user_id).removeValue()

                } else {
                    dbref.child(args.name).child("visited").child(user_id).setValue(user_id)
                }
            }
        }


        view.findViewById<Button>(R.id.star_button).setOnClickListener{
            dbref.child(args.name).child("star").get().addOnSuccessListener {
                var res: Map<String, String>? =  it.getValue() as Map<String, String>
                if (res!!.contains(user_id)){
                    dbref.child(args.name).child("star").child(user_id).removeValue()
                } else {
                    dbref.child(args.name).child("star").child(user_id).setValue(user_id)
                }
            }
        }

        view.findViewById<Button>(R.id.heart_button).setOnClickListener{
            dbref.child(args.name).child("heart").get().addOnSuccessListener {
                var res: Map<String, String>? =  it.getValue() as Map<String, String>
                if (res!!.contains(user_id)){
                    dbref.child(args.name).child("heart").child(user_id).removeValue()
                } else {
                    dbref.child(args.name).child("heart").child(user_id).setValue(user_id)
                }
            }
        }

        view.findViewById<Button>(R.id.thumb_button).setOnClickListener{
            dbref.child(args.name).child("thumb_up").get().addOnSuccessListener {
                var res: Map<String, String>? =  it.getValue() as Map<String, String>
                if (res!!.contains(user_id)){
                    dbref.child(args.name).child("thumb_up").child(user_id).removeValue()
                } else {
                    dbref.child(args.name).child("thumb_up").child(user_id).setValue(user_id)
                }
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