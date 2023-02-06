package com.example.dwarfia
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dwarfia.adapters.DwarfsListAdapter
import com.example.dwarfia.database.Dwarf2
import com.google.firebase.database.*

class MainFragment : Fragment() {
    private lateinit var dbref: DatabaseReference
    private lateinit var dwarf_star_list: ArrayList<Dwarf2>
    private lateinit var dwarf_not_visisted_list: ArrayList<Dwarf2>
    private lateinit var user_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        user_id = (activity as MainActivity?)!!.getUserId()
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dwarf_star_list = arrayListOf()
        dwarf_not_visisted_list = arrayListOf()

        val adp = DwarfsListAdapter()
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerview_not_visited)
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adp
        recyclerView.layoutManager = horizontalLayoutManager

        val adp_2 = DwarfsListAdapter()
        val horizontalLayoutManager_2 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val recyclerView_2 = requireView().findViewById<RecyclerView>(R.id.recyclerview2)
        recyclerView_2.adapter = adp_2
        recyclerView_2.layoutManager = horizontalLayoutManager_2
        dbref = FirebaseDatabase.getInstance(BuildConfig.API_KEY).getReference("Dwarfs")
        dbref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dwarfSnapshot in snapshot.children){
                        val dwarf = dwarfSnapshot.getValue(Dwarf2::class.java)
                        dwarf_star_list.add(dwarf!!)
                        if (!dwarf.visited!!.contains(user_id)) {
                            dwarf_not_visisted_list.add(dwarf)
                        }
                    }

                    dwarf_star_list.sortWith(compareByDescending { it.star!!.size })
                    adp.submitList(dwarf_star_list)


                    adp_2.submitList(dwarf_not_visisted_list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        requireActivity().findViewById<ImageView>(R.id.arrow_wroclaw_stars).setOnClickListener {
            view -> view.findNavController().navigate(R.id.action_mainFragment_to_dwarfsListFull)
        }

        requireActivity().findViewById<Button>(R.id.search_button).setOnClickListener {
                view -> view.findNavController().navigate(R.id.action_mainFragment_to_searchFragment)
        }

        requireActivity().findViewById<ImageView>(R.id.arrow_not_visited).setOnClickListener {
                view -> view.findNavController().navigate(R.id.action_mainFragment_to_notVisitedListFullFragment)
        }

        requireActivity().findViewById<Button>(R.id.my_collection_btn).setOnClickListener {
                view -> view.findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}