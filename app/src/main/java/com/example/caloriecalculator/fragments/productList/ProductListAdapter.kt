package com.example.caloriecalculator.fragments.productList

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.marginBottom
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriecalculator.R
import com.example.caloriecalculator.databinding.ProductItemBinding
import com.example.caloriecalculator.model.Product
import com.example.caloriecalculator.viewmodel.ProductViewModel
import java.lang.Exception

class ProductListAdapter(val c: Context, private val onDeleteCallback: (Product) -> Unit): RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    private var productList = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        if (!productList[position].isVisible) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        } else {
            holder.itemView.visibility = View.VISIBLE

            val factor = holder.itemView.context.resources.displayMetrics.density
            val params = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (60 * factor).toInt()
            )
            params.setMargins(0, 10, 0, 10)
            holder.itemView.layoutParams = params
        }
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(private val binding: ProductItemBinding):
        RecyclerView.ViewHolder(binding.root) {

            @SuppressLint("SetTextI18n", "DiscouragedPrivateApi", "NotifyDataSetChanged")
            fun bind(product: Product) = with(itemView) {

                binding.productName.text = product.name
                binding.calories.text = "${product.calories} ккал"

                binding.productItemLayout.setOnClickListener {

                    val action = ProductListFragmentDirections
                        .actionProductListFragmentToAddRationFragment(product)
                        findNavController().navigate(action)
                    }

                val popupMenu = PopupMenu(this.context, binding.productItemLayout, Gravity.END)
                popupMenu.inflate(R.menu.popupmenu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit -> {
                            val action = ProductListFragmentDirections.actionProductListFragmentToUpdateProductFragment(product)
                            findNavController().navigate(action)
                        }
                        R.id.delete -> {

                            AlertDialog.Builder(c, R.style.AlertDialogCustom)
                                .setTitle("Удалить ${product.name}?")
                                .setMessage("Вы уверены, что хотите удалить ${product.name}?")
                                .setPositiveButton("Да") { _, _ ->
                                    onDeleteCallback(product)
                                    notifyDataSetChanged()
                                    Toast.makeText(c,"Удалено",Toast.LENGTH_SHORT).show()
                                }
                                .setNegativeButton("Нет") {_, _ -> }
                                .create().show()

                        }
                    }
                    true
                }

                binding.productItemLayout.setOnLongClickListener {
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

    fun setListData(data: MutableList<Product>) {
        productList = data
        notifyDataSetChanged()
    }
}