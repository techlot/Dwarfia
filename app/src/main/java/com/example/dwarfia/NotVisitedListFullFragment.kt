package com.example.dwarfia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dwarfia.adapters.DwarfsListAdapter
import com.example.dwarfia.adapters.DwarfsListBigAdapter
import com.example.dwarfia.adapters.DwarfsListBigAdapter2
import com.example.dwarfia.database.Dwarf
import com.example.dwarfia.models.DwarfViewModelFactory
import com.example.dwarfia.models.DwarfsViewModel

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_dwarfs_list_full, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adp = DwarfsListBigAdapter2()


        val gridLayoutManager = GridLayoutManager(context, 2)


        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adp
        recyclerView.layoutManager = gridLayoutManager



        viewModel.not_visisted.observe(viewLifecycleOwner, Observer<List<Dwarf>> { dwarfs ->
            // Update the cached copy of the words in the adapter.
            dwarfs.let { adp.submitList(it) }
        })



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