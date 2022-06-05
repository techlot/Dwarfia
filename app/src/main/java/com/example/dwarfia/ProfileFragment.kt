package com.example.dwarfia

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dwarfia.adapters.DwarfsListAdapter
import com.example.dwarfia.adapters.DwarfsListAdapter2
import com.example.dwarfia.adapters.DwarfsListBigAdapter2
import com.example.dwarfia.database.Dwarf
import com.example.dwarfia.database.Dwarf2
import com.example.dwarfia.databinding.FragmentDwarfDetailsBinding
import com.example.dwarfia.databinding.FragmentProfileBinding
import com.example.dwarfia.models.DwarfViewModelFactory
import com.example.dwarfia.models.DwarfsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.w3c.dom.Text
import kotlin.random.Random

import kotlin.random.Random.Default.nextInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private val viewModel: DwarfsViewModel by activityViewModels {
        DwarfViewModelFactory((activity?.application as DwarfsApplication).repository)
    }

    private lateinit var dbref: DatabaseReference
    private lateinit var dwarfListVisited: ArrayList<Dwarf2>
    private lateinit var user_id: String
    private lateinit var visited_count: ArrayList<Dwarf2>
    private lateinit var total_count: ArrayList<Dwarf2>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        user_id = (activity as MainActivity?)!!.getUserId()

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adp = DwarfsListAdapter2()
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerview3)
        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adp
        recyclerView.layoutManager = horizontalLayoutManager

        dwarfListVisited = arrayListOf()

        total_count = arrayListOf()
        visited_count = arrayListOf()


        dbref = FirebaseDatabase.getInstance("https://dwarfia-7fe62-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Dwarfs")

        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dwarfSnapshot in snapshot.children){
                        val dwarf = dwarfSnapshot.getValue(Dwarf2::class.java)
                        total_count.add(dwarf!!)
                        if (dwarf!!.visited!!.contains(user_id)) {
                            visited_count.add(dwarf!!)
                        }
                    }
                    discovered_num_text_view.text = visited_count.size.toString()
                    if (visited_count.size == 0) {
                        your_collection_text_view.visibility = View.GONE
                    } else {
                        your_collection_text_view.visibility = View.VISIBLE
                    }
                    total_num_text_view.text = total_count.size.toString()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dwarfSnapshot in snapshot.children){
                        val dwarf = dwarfSnapshot.getValue(Dwarf2::class.java)
                        if (dwarf!!.visited!!.contains(user_id)) {
                            dwarfListVisited.add(dwarf!!)
                        }
                    }


                    dwarfListVisited.sortWith(compareByDescending { it.star!!.size})
                    adp.submitList(dwarfListVisited)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




        val list = listOf<String>("Bigfoot", "Superhero", "Mermaid", "Supervillain", "Unicorn")
        val randomIndex = Random.nextInt(list.size)
        val randomCharacter = list[randomIndex]

        requireView().findViewById<TextView>(R.id.username_text_view).text = randomCharacter

            when (randomCharacter){
            "Bigfoot" -> requireView().findViewById<ImageView>(R.id.pfp_image_view).setImageResource(R.drawable.bigfoot)
            "Superhero" -> requireView().findViewById<ImageView>(R.id.pfp_image_view).setImageResource(R.drawable.character)
            "Mermaid" -> requireView().findViewById<ImageView>(R.id.pfp_image_view).setImageResource(R.drawable.mermaid)
            "Supervillain" -> requireView().findViewById<ImageView>(R.id.pfp_image_view).setImageResource(R.drawable.supervillain)
            else -> requireView().findViewById<ImageView>(R.id.pfp_image_view).setImageResource(R.drawable.unicorn)
        }

        /*viewModel.your_collection.observe(viewLifecycleOwner, Observer<List<Dwarf>> { dwarfs ->
            // Update the cached copy of the words in the adapter.
            dwarfs.let { adp.submitList(it) }
            requireView().findViewById<TextView>(R.id.discovered_num_text_view).text = dwarfs.size.toString()
        })*/

/*        viewModel.stars.observe(viewLifecycleOwner, Observer<List<Dwarf>> { dwarfs ->
            // Update the cached copy of the words in the adapter.
            requireView().findViewById<TextView>(R.id.total_num_text_view).text = dwarfs.size.toString()
        })*/

        //requireActivity().findViewById<ImageView>(R.id.arrow_wroclaw_stars).setOnClickListener {
        //        view -> view.findNavController().navigate(R.id.action_mainFragment_to_dwarfsListFull)
        //}

        //requireActivity().findViewById<ImageView>(R.id.arrow_not_visited).setOnClickListener {
        //        view -> view.findNavController().navigate(R.id.action_mainFragment_to_notVisitedListFullFragment)
        //}

        requireActivity().findViewById<Button>(R.id.back_btn).setOnClickListener {
                view -> view.findNavController().navigate(R.id.action_profileFragment_to_mainFragment)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(context, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}