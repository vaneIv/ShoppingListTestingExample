package com.example.shoppinglisttestingexample.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.shoppinglisttestingexample.R
import com.example.shoppinglisttestingexample.other.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_shopping_item.btn_add_shopping_item
import kotlinx.android.synthetic.main.fragment_add_shopping_item.edit_text_shopping_item_amount
import kotlinx.android.synthetic.main.fragment_add_shopping_item.edit_text_shopping_item_name
import kotlinx.android.synthetic.main.fragment_add_shopping_item.edit_text_shopping_item_price
import kotlinx.android.synthetic.main.fragment_add_shopping_item.image_view_shopping_image
import kotlinx.android.synthetic.main.fragment_shopping.root_layout
import javax.inject.Inject

class AddShoppingItemFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)

        subscribeToObservers()

        btn_add_shopping_item.setOnClickListener {
            viewModel.insertShoppingItem(
                edit_text_shopping_item_name.text.toString(),
                edit_text_shopping_item_amount.text.toString(),
                edit_text_shopping_item_price.text.toString()
            )
        }

        image_view_shopping_image.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setCurrentImage("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun subscribeToObservers() {
        viewModel.curImageUrl.observe(viewLifecycleOwner, Observer {
            glide.load(it).into(image_view_shopping_image)
        })
        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        Snackbar.make(
                            requireActivity().root_layout,
                            "Added Shopping Item",
                            Snackbar.LENGTH_LONG
                        ).show()
                        findNavController().popBackStack()
                    }

                    Status.ERROR -> {
                        Snackbar.make(
                            requireActivity().root_layout,
                            result.message ?: "An unknown error occcured",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    Status.LOADING -> {
                        /* NO-OP */
                    }
                }
            }
        })
    }
}