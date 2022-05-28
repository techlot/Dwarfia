package com.example.dwarfia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dwarfia.adapters.DwarfsListBigAdapter2
import com.example.dwarfia.database.Dwarf2
import com.example.dwarfia.models.DwarfViewModelFactory
import com.example.dwarfia.models.DwarfsViewModel
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DwarfsListFull.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotVisitedListFullFragment : Fragment() {
    private val viewModel: DwarfsViewModel by activityViewModels {
        DwarfViewModelFactory((activity?.application as DwarfsApplication).repository)
    }

    private lateinit var dbref: DatabaseReference
    private lateinit var dwarfNotVisistedList: ArrayList<Dwarf2>
    private lateinit var user_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        user_id = (activity as MainActivity?)!!.getUserId()
        return inflater.inflate(R.layout.fragment_dwarfs_list_full, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adp = DwarfsListBigAdapter2()
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerview_not_visited)
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adp
        recyclerView.layoutManager = horizontalLayoutManager

        dwarfNotVisistedList = arrayListOf()
        dbref = FirebaseDatabase.getInstance("https://dwarfia-7fe62-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Dwarfs")
        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dwarfSnapshot in snapshot.children){
                        val dwarf = dwarfSnapshot.getValue(Dwarf2::class.java)
                        if (!dwarf!!.visited!!.contains(user_id)) {
                            dwarfNotVisistedList.add(dwarf!!)
                        }
                    }


                    dwarfNotVisistedList.sortWith(compareByDescending { it.star!!.size })
                    adp.submitList(dwarfNotVisistedList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        /*viewModel.stars.observe(viewLifecycleOwner, Observer<List<Dwarf>> { dwarfs ->
            // Update the cached copy of the words in the adapter.
            dwarfs.let { adp.submitList(it) }
        })*/




    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DwarfsListFull().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}