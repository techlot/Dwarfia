package com.example.dwarfia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dwarfia.adapters.DwarfsListAdapter
import com.example.dwarfia.database.Dwarf
import com.example.dwarfia.models.DwarfViewModelFactory
import com.example.dwarfia.models.DwarfsViewModel

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        
        val adp = DwarfsListAdapter()
        val adp_2 = DwarfsListAdapter()

        val horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val horizontalLayoutManager_2 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adp
        recyclerView.layoutManager = horizontalLayoutManager

        val recyclerView_2 = requireView().findViewById<RecyclerView>(R.id.recyclerview2)
        recyclerView_2.adapter = adp_2
        recyclerView_2.layoutManager = horizontalLayoutManager_2

        viewModel.stars.observe(viewLifecycleOwner, Observer<List<Dwarf>> { dwarfs ->
            // Update the cached copy of the words in the adapter.
            dwarfs.let { adp.submitList(it) }
        })

        viewModel.not_visisted.observe(viewLifecycleOwner, Observer<List<Dwarf>> { dwarfs ->
            // Update the cached copy of the words in the adapter.
            dwarfs.let { adp_2.submitList(it) }
        })

        requireActivity().findViewById<ImageView>(R.id.arrow_wroclaw_stars).setOnClickListener {
            view -> view.findNavController().navigate(R.id.action_mainFragment_to_dwarfsListFull)
        }

        requireActivity().findViewById<ImageView>(R.id.arrow_not_visited).setOnClickListener {
                view -> view.findNavController().navigate(R.id.action_mainFragment_to_notVisitedListFullFragment)
        }

        requireActivity().findViewById<Button>(R.id.my_collection_btn).setOnClickListener {
                view -> view.findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
        }

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