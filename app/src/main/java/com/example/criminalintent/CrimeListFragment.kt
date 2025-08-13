package com.example.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A fragment that displays a list of crimes.
 *
 * This fragment uses a [RecyclerView] to display the list of crimes.
 * The data for the list is provided by a [CrimeListViewModel].
 */
class CrimeListFragment : Fragment() {

    private val vm: CrimeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_crime_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.crime_recycler_view)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = CrimeListAdapter(vm.crimes) { crime ->
            // Navigate to detail fragment for this crime
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CrimeDetailFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}