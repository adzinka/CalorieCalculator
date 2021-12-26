package com.example.caloriecalculator.fragments.rationsByDay

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriecalculator.databinding.DayWithRationItemBinding
import java.text.SimpleDateFormat

class RationsByDayAdapter(): RecyclerView.Adapter<RationsByDayAdapter.RationsByDayHolder>() {

    private var rationsByDayList = mutableListOf<RationByDay>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RationsByDayAdapter.RationsByDayHolder {
        return RationsByDayHolder(DayWithRationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RationsByDayAdapter.RationsByDayHolder, position: Int) {
        holder.bind(rationsByDayList[position])
    }

    override fun getItemCount(): Int {
        return rationsByDayList.size
    }

    inner class RationsByDayHolder(private val binding: DayWithRationItemBinding):
            RecyclerView.ViewHolder(binding.root) {

                @SuppressLint("SimpleDateFormat", "SetTextI18n")
                fun bind(rationByDay: RationByDay) = with(itemView) {

                    val formatter = SimpleDateFormat("dd.MM.yyyy")
                    val date = rationByDay.date
                    val day = formatter.format(date)
                    val calories = rationByDay.calories
                    val proteins = rationByDay.proteins
                    val fats = rationByDay.fats
                    val carbohydrates = rationByDay.carbohydrates

                    binding.date.text = day.toString()
                    binding.calories.text = "$calories ккал"
                    binding.proteins.text = "$proteins белки"
                    binding.fats.text = "$fats жиры"
                    binding.carbohydrates.text = "$carbohydrates углев"

                    binding.dayWithRationItemLayout.setOnClickListener {
                        val action = RationsByDayFragmentDirections.actionRationsByDayFragmentToRationListFragment(rationByDay)
                        findNavController().navigate(action)
                    }
                }
            }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(data: MutableList<RationByDay>) {
        rationsByDayList = data
        notifyDataSetChanged()
    }

}