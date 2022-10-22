package com.example.take

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.take.databinding.ItemMedicamentBinding
import com.example.take.model.Medicament
import com.example.take.model.TimeRecylerAdapter

interface MedicamentActionListener {
    fun onMedicamentDelete(medicament: Medicament)
    fun onMedicamentDetails(medicament: Medicament)
}

class MedicineDiffCallback(
    private val oldList: List<Medicament>,
    private val newList: List<Medicament>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMedicament = oldList[oldItemPosition]
        val newMedicament = newList[newItemPosition]
        return oldMedicament.id == newMedicament.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMedicament = oldList[oldItemPosition]
        val newMedicament = newList[newItemPosition]
        return oldMedicament == newMedicament //сравнение происходит по содержимому, в dataclass уже переопределен Equals
    }

}

class MedicamentsAdapter(
    private val actionListener: MedicamentActionListener
): RecyclerView.Adapter<MedicamentsAdapter.MedicamentsViewHolder>(),  View.OnClickListener{

    var medicaments: List<Medicament> = emptyList()
        set(newValue) {
            val diffCallback = MedicineDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    private lateinit var ourContext: Context

    class MedicamentsViewHolder(
        val binding: ItemMedicamentBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMedicamentBinding.inflate(inflater, parent, false)
        ourContext = parent.context
        binding.root.setOnClickListener(this)
        binding.moreImageViewBtn.setOnClickListener(this)

        return MedicamentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MedicamentsViewHolder, position: Int) {
        val medicament = medicaments[position]
        val timeRecylerAdapter = TimeRecylerAdapter(medicament.time)
        with(holder.binding) {
            holder.itemView.tag = medicament
            moreImageViewBtn.tag = medicament
            nameMedicamentTextView.text = medicament.name
            //timeMedicamentTextView.text = medicament.time
            val layoutManager = LinearLayoutManager(ourContext, LinearLayoutManager.HORIZONTAL,false)
            timeRecyclerView.layoutManager = layoutManager
            timeRecyclerView.adapter = timeRecylerAdapter
        }
    }

    override fun getItemCount(): Int {
        return medicaments.size
    }

    override fun onClick(p0: View) {
        val medicament = p0.tag as Medicament
        when(p0.id) {
            R.id.moreImageViewBtn -> {
                //Пользователь нажал на кнопку more
                showPopupMenu(p0)
            }
            else -> {
                //Пользователь нажал на сам элемент списка
                actionListener.onMedicamentDetails(medicament)
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val medicament = view.tag as Medicament

        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "remove")

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                ID_REMOVE -> {
                    actionListener.onMedicamentDelete(medicament)
                }
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    companion object {
        private const val ID_REMOVE = 1
    }
}