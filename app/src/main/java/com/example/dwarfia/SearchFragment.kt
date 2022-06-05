package com.example.dwarfia

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.ObservableArrayList
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dwarfia.adapters.DwarfListWideAdapter
import com.example.dwarfia.adapters.DwarfsListBigAdapter2
import com.example.dwarfia.database.Dwarf2
import com.example.dwarfia.models.DwarfViewModelFactory
import com.example.dwarfia.models.DwarfsViewModel
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    private val viewModel: DwarfsViewModel by activityViewModels {
        DwarfViewModelFactory((activity?.application as DwarfsApplication).repository)
    }

    private lateinit var dbref: DatabaseReference
    private lateinit var adp: DwarfListWideAdapter
    private lateinit var dwarfAllList: ArrayList<Dwarf2>
    private lateinit var tempAllList: ArrayList<Dwarf2>
    private lateinit var user_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        user_id = (activity as MainActivity?)!!.getUserId()
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adp = DwarfListWideAdapter()
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerview_all)
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = adp
        recyclerView.layoutManager = horizontalLayoutManager

        dwarfAllList = arrayListOf()
        tempAllList = arrayListOf()

        dbref = FirebaseDatabase.getInstance("https://dwarfia-7fe62-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Dwarfs")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dwarfSnapshot in snapshot.children) {
                        val dwarf = dwarfSnapshot.getValue(Dwarf2::class.java)
                        dwarfAllList.add(dwarf!!)
                    }
                    tempAllList.addAll(dwarfAllList)
                    adp.submitList(tempAllList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                tempAllList.clear()
                adp.notifyDataSetChanged()
                val searchText = query!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    dwarfAllList.forEach {
                        if (it.name!!.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            tempAllList.add(it)
                        }
                    }

                    adp.submitList(tempAllList)
                } else {
                    tempAllList.clear()
                    tempAllList.addAll(dwarfAllList)
                    adp.submitList(tempAllList)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempAllList.clear()
                adp.notifyDataSetChanged()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    dwarfAllList.forEach {
                        if (it.name!!.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            tempAllList.add(it)
                        }
                    }

                    adp.submitList(tempAllList)
                } else {
                    tempAllList.clear()
                    tempAllList.addAll(dwarfAllList)
                    adp.submitList(tempAllList)
                }
                return false
            }

        })
    }



}