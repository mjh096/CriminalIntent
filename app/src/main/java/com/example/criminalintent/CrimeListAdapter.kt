package com.example.criminalintent

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


/**
 * A RecyclerView.Adapter to display a list of [Crime] objects.
 *
 * @property crimes The list of [Crime] objects to display.
 * @property onCrimeClicked A lambda function to be invoked when a crime item is clicked.
 *                           It takes the clicked [Crime] object as a parameter.
 */
class CrimeListAdapter(
    private var crimes: List<Crime>,
    private val onCrimeClicked: (Crime) -> Unit
) : RecyclerView.Adapter<CrimeListAdapter.CrimeHolder>() {

    /**
     * Updates the list of crimes and notifies the adapter of the data change.
     *
     * @param list The new list of [Crime] objects to display.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun update(list: List<Crime>) {
        crimes = list
        notifyDataSetChanged()
    }

    class CrimeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.crime_title_text)
        private val dateText: TextView  = itemView.findViewById(R.id.crime_date_text)
        private val solvedIcon: ImageView = itemView.findViewById(R.id.crime_solved_icon)

        fun bind(crime: Crime, onClick: () -> Unit) {
            titleText.text = crime.title
            dateText.text  = crime.date.toString()
            solvedIcon.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
            itemView.setOnClickListener { onClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_crime, parent, false)
        return CrimeHolder(view)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime) { onCrimeClicked(crime) }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = crimes.size
}
