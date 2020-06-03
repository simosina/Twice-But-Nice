package it.polito.mad.project.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import it.polito.mad.project.R
import it.polito.mad.project.models.item.Item

class ItemBoughtAdapter (private var itemsBought: MutableList<Item>) : RecyclerView.Adapter<ItemBoughtAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val userItemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_bought, parent, false)
        return ViewHolder(userItemView)
    }

    override fun getItemCount() = itemsBought.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemsBought[position],
            {
                val bundle = bundleOf("ItemId" to itemsBought[position].id)
                holder.itemView.findNavController().navigate(R.id.action_boughtItemsListFragment_to_showItemFragment, bundle)
            },
            {
                val bundle = bundleOf("ItemId" to itemsBought[position].id)
                holder.itemView.findNavController().navigate(R.id.action_boughtItemsListFragment_to_showItemFragment, bundle)
            })
    }

    fun setItems(newItems: MutableList<Item>) {
        val diffs = DiffUtil.calculateDiff(ItemDiffCallback(itemsBought, newItems))
        itemsBought.clear()
        itemsBought.addAll(newItems)
        diffs.dispatchUpdatesTo(this) //animate UI
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val category: TextView = view.findViewById(R.id.item_category)
        private val title: TextView = view.findViewById(R.id.item_title)
        private val price: TextView = view.findViewById(R.id.item_price)
        private val container: CardView = view.findViewById(R.id.item_container)
        private val button: Button = view.findViewById(R.id.item_review_button)

        fun bind(item: Item, callback: (Int) -> Unit, callbackReview: (Int) -> Unit ) {
            val priceStr = "${item.price} €"

            category.text = item.category
            title.text = item.title
            price.text = priceStr

            container.setOnClickListener { callback(adapterPosition) }
            button.setOnClickListener{callbackReview(adapterPosition)}
        }

        fun unbind() {
            container.setOnClickListener(null)
            button.setOnClickListener(null)
        }
    }
}