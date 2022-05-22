package com.example.dwarfia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dwarfia.adapters.DwarfsListAdapter
import com.example.dwarfia.adapters.DwarfsListAdapter2
import com.example.dwarfia.database.Dwarf
import com.example.dwarfia.models.DwarfViewModelFactory
import com.example.dwarfia.models.DwarfsViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adp = DwarfsListAdapter2()

        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerview3)
        recyclerView.adapter = adp
        recyclerView.layoutManager = horizontalLayoutManager

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

        viewModel.your_collection.observe(viewLifecycleOwner, Observer<List<Dwarf>> { dwarfs ->
            // Update the cached copy of the words in the adapter.
            dwarfs.let { adp.submitList(it) }
            requireView().findViewById<TextView>(R.id.discovered_num_text_view).text = dwarfs.size.toString()
        })

        viewModel.stars.observe(viewLifecycleOwner, Observer<List<Dwarf>> { dwarfs ->
            // Update the cached copy of the words in the adapter.
            requireView().findViewById<TextView>(R.id.total_num_text_view).text = dwarfs.size.toString()
        })

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