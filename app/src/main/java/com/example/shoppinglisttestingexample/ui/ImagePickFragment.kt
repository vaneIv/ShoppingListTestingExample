package com.example.shoppinglisttestingexample.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppinglisttestingexample.R
import com.example.shoppinglisttestingexample.adapters.ImageAdapter
import com.example.shoppinglisttestingexample.other.Constants.GRID_SPAN_COUNT
import com.example.shoppinglisttestingexample.other.Constants.SEARCH_TIME_DELAY
import com.example.shoppinglisttestingexample.other.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_image_pick.edit_text_search
import kotlinx.android.synthetic.main.fragment_image_pick.progress_bar
import kotlinx.android.synthetic.main.fragment_image_pick.recycler_view_images
import kotlinx.android.synthetic.main.fragment_shopping.root_layout
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter
) : Fragment(R.layout.fragment_image_pick) {

    lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        setupRecyclerView()
        subscribeToObservers()

        var job: Job? = null
        edit_text_search.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchForImage(editable.toString())
                    }
                }
            }
        }

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurrentImage(it)
        }
    }

    private fun subscribeToObservers() {
        viewModel.images.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val urls =
                            result.data?.results?.map { imageResult -> imageResult.urls.toString() }
                        imageAdapter.images = urls ?: listOf()
                        progress_bar.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        Snackbar.make(
                            requireActivity().root_layout,
                            result.message ?: "An unknown error occurred.",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    Status.LOADING -> {
                        progress_bar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun setupRecyclerView() {
        recycler_view_images.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }
}