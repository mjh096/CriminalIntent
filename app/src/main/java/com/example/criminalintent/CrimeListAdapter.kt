package com.example.criminalintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClicked: (Crime) -> Unit // ← add this parameter
) : RecyclerView.Adapter<CrimeListAdapter.CrimeHolder>() {

    class CrimeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.crime_title_text)
        private val dateText: TextView = itemView.findViewById(R.id.crime_date_text)
        private val solvedIcon: ImageView = itemView.findViewById(R.id.crime_solved_icon)

        fun bind(crime: Crime, onCrimeClicked: (Crime) -> Unit) {
            titleText.text = crime.title
            dateText.text = crime.date.toString()

            solvedIcon.visibility = if (crime.isSolved) View.VISIBLE else View.GONE

            itemView.setOnClickListener { onCrimeClicked(crime) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_crime, parent, false)
        return CrimeHolder(view)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        holder.bind(crimes[position], onCrimeClicked) // ← now passes the constructor param
    }

    override fun getItemCount(): Int = crimes.size
}
