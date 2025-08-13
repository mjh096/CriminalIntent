package com.example.criminalintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for the RecyclerView in CrimeListFragment.
 *
 * This adapter is responsible for creating and binding ViewHolders to display a list of [Crime] objects.
 *
 * @property crimes The list of [Crime] objects to be displayed.
 */
class CrimeListAdapter(
    private val crimes: List<Crime>
) : RecyclerView.Adapter<CrimeListAdapter.CrimeHolder>() {

    class CrimeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.crime_title_text)
        private val dateText: TextView = itemView.findViewById(R.id.crime_date_text)

        fun bind(crime: Crime) {
            titleText.text = crime.title
            dateText.text = crime.date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_crime, parent, false)
        return CrimeHolder(view)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        holder.bind(crimes[position])
    }

    override fun getItemCount(): Int = crimes.size
}