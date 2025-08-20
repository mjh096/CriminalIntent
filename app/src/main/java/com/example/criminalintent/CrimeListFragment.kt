package com.example.criminalintent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

/**
 * A fragment that displays a list of crimes.
 *
 * This fragment uses a [RecyclerView] to display the list of crimes.
 * The data for the list is provided by a [CrimeListViewModel].
 */
class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {

    private val vm: CrimeListViewModel by viewModels()
    private lateinit var adapter: CrimeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.crime_recycler_view)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = CrimeListAdapter(emptyList()) { crime ->
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    CrimeDetailFragment.newInstance(crime.id)
                )
                .addToBackStack(null)
                .commit()
        }
        recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.crimes.collect { list ->
                    adapter.update(list)
                }
            }
        }
    }
}