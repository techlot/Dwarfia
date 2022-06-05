package com.example.dwarfia

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dwarfia.adapters.DwarfsListAdapter
import com.example.dwarfia.database.Dwarf2
import com.example.dwarfia.models.DwarfViewModelFactory
import com.example.dwarfia.models.DwarfsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private val viewModel: DwarfsViewModel by activityViewModels {
        DwarfViewModelFactory((activity?.application as DwarfsApplication).repository)
    }

    private lateinit var dbref: DatabaseReference
    private lateinit var dwarfStarList: ArrayList<Dwarf2>
    private lateinit var dwarfNotVisistedList: ArrayList<Dwarf2>
    private lateinit var user_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        user_id = (activity as MainActivity?)!!.getUserId()
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        dwarfStarList = arrayListOf()
        dwarfNotVisistedList = arrayListOf()

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

        dbref = FirebaseDatabase.getInstance("https://dwarfia-7fe62-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Dwarfs")
        dbref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dwarfSnapshot in snapshot.children){
                        val dwarf = dwarfSnapshot.getValue(Dwarf2::class.java)
                        dwarfStarList.add(dwarf!!)
                        if (!dwarf!!.visited!!.contains(user_id)) {
                            dwarfNotVisistedList.add(dwarf!!)
                        }
                    }


                    dwarfStarList.sortWith(compareByDescending { it.star!!.size })
                    adp.submitList(dwarfStarList)


                    adp_2.submitList(dwarfNotVisistedList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        /*viewModel.stars.observe(viewLifecycleOwner, Observer<List<Dwarf>> { dwarfs ->
            // Update the cached copy of the words in the adapter.
            dwarfs.let { adp.submitList(it) }
        })

        viewModel.not_visisted.observe(viewLifecycleOwner, Observer<List<Dwarf>> { dwarfs ->
            // Update the cached copy of the words in the adapter.
            dwarfs.let { adp_2.submitList(it) }
        })*/

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

    private fun getStarsData() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}