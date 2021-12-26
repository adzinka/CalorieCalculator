package com.example.caloriecalculator.fragments.rationList

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.util.Log
import android.view.Gravity
import androidx.navigation.findNavController
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriecalculator.R
import com.example.caloriecalculator.databinding.RationItemBinding
import com.example.caloriecalculator.model.Ration
import com.example.caloriecalculator.relations.RationWithProduct
import com.example.caloriecalculator.viewmodel.RationViewModel
import java.lang.Exception

class RationListAdapter(val c: Context, private var rationDao: RationViewModel): RecyclerView.Adapter<RationListAdapter.RationViewHolder>() {

    private var rationWithProductList = mutableListOf<RationWithProduct>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RationViewHolder {
        return RationViewHolder(RationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RationListAdapter.RationViewHolder, position: Int) {
        holder.bind(rationWithProductList[position])
    }

    override fun getItemCount(): Int {
        return rationWithProductList.size
    }

    inner class RationViewHolder(private val binding: RationItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "SimpleDateFormat", "DiscouragedPrivateApi",
            "NotifyDataSetChanged"
        )
        fun bind(rationWithProduct: RationWithProduct) = with(itemView) {

            val weight = rationWithProduct.ration.weight
            val currentProteins = rationWithProduct.ration_proteins
            val currentFats = rationWithProduct.ration_fats
            val currentCarbohydrates = rationWithProduct.ration_carbohydrates
            val currentCalories = rationWithProduct.ration_calories

            binding.nameTV.text = rationWithProduct.product.name
            binding.weightTV.text = "${weight.toInt()}гр"
            binding.caloriesTV.text = "$currentCalories ккал"
            binding.proteinsFatsCarboh.text = "Б/Ж/У $currentProteins/$currentFats/$currentCarbohydrates"

            val popupMenu = PopupMenu(this.context, binding.rationItemLayout, Gravity.END)
            popupMenu.inflate(R.menu.popupmenu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        val action = RationListFragmentDirections.actionRationListFragmentToUpdateRationFragment(rationWithProduct)
                        findNavController().navigate(action)
                    }
                    R.id.delete -> {
                        AlertDialog.Builder(c, R.style.AlertDialogCustom)
                            .setTitle("Удалить ${rationWithProduct.product.name}?")
                            .setMessage("Вы уверены, что хотите удалить ${rationWithProduct.product.name}?")
                            .setPositiveButton("Да") { _, _ ->

                                if (rationWithProductList.size == 1) {
                                    rationDao.deleteRationById(rationWithProduct.ration_id)
                                    findNavController().navigate(R.id.action_rationListFragment_to_homeFragment)
                                } else {
                                    rationWithProductList.removeAt(adapterPosition)
                                    rationDao.deleteRationById(rationWithProduct.ration_id)
                                    notifyDataSetChanged()
                                }
                                Toast.makeText(c,"Удалено", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("Нет") {_, _ -> }
                            .create().show()
                    }
                }
                true
            }

            binding.rationItemLayout.setOnLongClickListener {
                try {
                    val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                    fieldMPopup.isAccessible = true
                    val mPopup = fieldMPopup.get(popupMenu)
                    mPopup.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPopup, true)
                } catch (e: Exception) {
                    Log.e("Main", "Error showing menu icons.", e)
                } finally {
                    popupMenu.show()
                }
                return@setOnLongClickListener true
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(data: MutableList<RationWithProduct>) {
        rationWithProductList = data
        notifyDataSetChanged()
    }
}