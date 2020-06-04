package it.polito.mad.project.fragments.advertisements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import it.polito.mad.project.R
import it.polito.mad.project.models.review.Review
import it.polito.mad.project.viewmodels.ItemViewModel
import kotlinx.android.synthetic.main.fragment_item_review.*

class ItemReviewFragment : Fragment() {

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemViewModel = ViewModelProvider(requireActivity()).get(ItemViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)?.supportActionBar?.show()

        itemViewModel.item.data.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                reviewTitle.text = it.title
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val itemId = arguments?.getString("ItemId")
        itemViewModel.loadItem(itemId!!)

        publishReview.setOnClickListener {
            val review = Review(
                description.text.toString(),
                ratingBar.rating
            )
            itemViewModel.setReview(itemViewModel.item.data.value!!,review)

            findNavController().popBackStack()
        }

        super.onViewCreated(view, savedInstanceState)
    }
}