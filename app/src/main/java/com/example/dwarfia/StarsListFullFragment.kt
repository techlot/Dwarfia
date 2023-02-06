package com.example.dwarfia
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dwarfia.adapters.DwarfsListBigAdapter
import com.example.dwarfia.database.Dwarf2
import com.google.firebase.database.*

class DwarfsListFull : Fragment() {
    private lateinit var dbref: DatabaseReference
    private lateinit var dwarfStarList: ArrayList<Dwarf2>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dwarfs_list_full, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dwarfStarList = arrayListOf()
        val adp = DwarfsListBigAdapter()
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerview_not_visited)
        recyclerView.adapter = adp
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        dbref = FirebaseDatabase.getInstance(BuildConfig.API_KEY).getReference("Dwarfs")
        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dwarfSnapshot in snapshot.children){
                        val dwarf = dwarfSnapshot.getValue(Dwarf2::class.java)
                        dwarfStarList.add(dwarf!!)
                    }

                    dwarfStarList.sortWith(compareByDescending { it.star!!.size })
                    adp.submitList(dwarfStarList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}