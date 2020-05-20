package it.polito.mad.project.fragments.advertisements

import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import it.polito.mad.project.R
import kotlinx.android.synthetic.main.fragment_on_sale_list.*
import kotlinx.android.synthetic.main.modal_filter.view.*

class OnSaleListFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var itemViewModel: ItemViewModel
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemViewModel = ViewModelProvider(activity?:this).get(ItemViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_on_sale_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
        itemViewModel.loader.observe(viewLifecycleOwner, Observer {
            if (itemViewModel.isNotLoading()) {
                // loader ended
                itemViewModel.adapterOnSale.setItems(itemViewModel.itemsOnSale)
                if(itemViewModel.itemsOnSale.size == 0) {
                    emptyListLayout.visibility = View.VISIBLE
                    itemRecyclerView.visibility = GONE
                } else {
                    emptyListLayout.visibility = GONE
                    itemRecyclerView.visibility = View.VISIBLE
                }
                loadingLayout.visibility = GONE
            } else {
                loadingLayout.visibility = View.VISIBLE
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemRecyclerView.setHasFixedSize(true)
        itemRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        itemRecyclerView.adapter = itemViewModel.adapterOnSale
        itemViewModel.loadItemsOnSale()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        var itemMenu: MenuItem = menu!!.findItem(R.id.menu_search)

        if(itemMenu != null){
            searchView = itemMenu.actionView as SearchView

            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.hint = "Type any kind of info"

            searchView.setOnQueryTextListener(this)
        } else {
            //Toast.makeText(activity, "WRONG", Toast.LENGTH_LONG).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.search_filter -> {
                openModal()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openModal(){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.modal_filter, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
            .setTitle("Choose the filter")
        val alertDialog = builder.show()

        dialogView.filterBtn.setOnClickListener{
            alertDialog.dismiss()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Toast.makeText(requireActivity(), newText, Toast.LENGTH_SHORT).show()
        itemViewModel.adapterOnSale.filter.filter(newText)
        return true
    }
}