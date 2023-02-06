package com.example.dwarfia
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dwarfia.adapters.DwarfsListBigAdapter2
import com.example.dwarfia.database.Dwarf2
import com.google.firebase.database.*

class NotVisitedListFullFragment : Fragment() {
    private lateinit var dbref: DatabaseReference
    private lateinit var dwarf_not_visisted_list: ArrayList<Dwarf2>
    private lateinit var user_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user_id = (activity as MainActivity?)!!.getUserId()
        return inflater.inflate(R.layout.fragment_dwarfs_list_full, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adp = DwarfsListBigAdapter2()
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerview_not_visited)
        recyclerView.adapter = adp
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        dwarf_not_visisted_list = arrayListOf()
        dbref = FirebaseDatabase.getInstance(BuildConfig.API_KEY).getReference("Dwarfs")
        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dwarfSnapshot in snapshot.children){
                        val dwarf = dwarfSnapshot.getValue(Dwarf2::class.java)
                        if (!dwarf!!.visited!!.contains(user_id)) {
                            dwarf_not_visisted_list.add(dwarf)
                        }
                    }


                    dwarf_not_visisted_list.sortWith(compareByDescending { it.star!!.size })
                    adp.submitList(dwarf_not_visisted_list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}