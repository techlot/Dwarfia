package com.example.dwarfia
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.dwarfia.database.Dwarf2
import com.example.dwarfia.databinding.FragmentDwarfDetailsBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_dwarf_details.*


class DwarfDetailsFragment : Fragment() {
    val args: DwarfDetailsFragmentArgs by navArgs()

    private lateinit var dbref: DatabaseReference
    private lateinit var user_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        user_id = (activity as MainActivity?)!!.getUserId()
        val binding: FragmentDwarfDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dwarf_details, container, false)
        binding.lifecycleOwner = this
        binding.userId=user_id
        dbref = FirebaseDatabase.getInstance(BuildConfig.API_KEY).getReference("Dwarfs")
        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val dwarf = snapshot.child(args.name).getValue(Dwarf2::class.java)
                    binding.dwarf = dwarf
                    binding.visited = dwarf!!.visited!!.contains(user_id)
                    binding.starred = dwarf.star!!.contains(user_id)
                    binding.hearted = dwarf.heart!!.contains(user_id)
                    binding.thumbed = dwarf.thumb_up!!.contains(user_id)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.dwarf_description_text_view).movementMethod = ScrollingMovementMethod()

        view.findViewById<Button>(R.id.check_button).setOnClickListener{
            dbref.child(args.name).child("visited").get().addOnSuccessListener {
                var res: Map<String, String>? =  it.value as Map<String, String>
                if (res!!.contains(user_id)){
                    dbref.child(args.name).child("visited").child(user_id).removeValue()
                } else {
                    dbref.child(args.name).child("visited").child(user_id).setValue(user_id)
                }
            }
        }

        view.findViewById<Button>(R.id.star_button).setOnClickListener{
            dbref.child(args.name).child("star").get().addOnSuccessListener {
                var res: Map<String, String>? =  it.value as Map<String, String>
                if (res!!.contains(user_id)){
                    dbref.child(args.name).child("star").child(user_id).removeValue()
                } else {
                    dbref.child(args.name).child("star").child(user_id).setValue(user_id)
                }
            }
        }

        view.findViewById<Button>(R.id.heart_button).setOnClickListener{
            dbref.child(args.name).child("heart").get().addOnSuccessListener {
                var res: Map<String, String>? =  it.value as Map<String, String>
                if (res!!.contains(user_id)){
                    dbref.child(args.name).child("heart").child(user_id).removeValue()
                } else {
                    dbref.child(args.name).child("heart").child(user_id).setValue(user_id)
                }
            }
        }

        view.findViewById<Button>(R.id.thumb_button).setOnClickListener{
            dbref.child(args.name).child("thumb_up").get().addOnSuccessListener {
                var res: Map<String, String>? =  it.value as Map<String, String>
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