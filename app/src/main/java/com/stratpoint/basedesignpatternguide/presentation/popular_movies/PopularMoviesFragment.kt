package com.stratpoint.basedesignpatternguide.presentation.popular_movies

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stratpoint.basedesignpatternguide.R
import com.stratpoint.basedesignpatternguide.constant.PaginationLoadingIndicator
import com.stratpoint.basedesignpatternguide.databinding.FragmentPopularMoviesBinding
import com.stratpoint.basedesignpatternguide.domain.Movie
import com.stratpoint.basedesignpatternguide.presentation.base.BaseFragment
import com.stratpoint.basedesignpatternguide.util.ItemClickListener
import com.stratpoint.basedesignpatternguide.util.extensions.goTo
import com.stratpoint.basedesignpatternguide.util.extensions.gone
import com.stratpoint.basedesignpatternguide.util.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PopularMoviesFragment : BaseFragment<FragmentPopularMoviesBinding>(
    FragmentPopularMoviesBinding::inflate
), ItemClickListener<Movie> {

    private val viewModel by viewModels<PopularMoviesViewModel>()

    @Inject
    lateinit var moviesAdapter: PopularMoviesAdapter

    override fun setUpView() {
        super.setUpView()

        moviesAdapter.setOnItemClickListener(this)

        viewBinding.containerToolbar.toolbar.title = getString(R.string.title_popular_movies)

        viewBinding.rvPopularMovies.apply {
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager = gridLayoutManager
            adapter = moviesAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        val totalItemCount = gridLayoutManager.itemCount
                        val visibleItemCount = childCount
                        val firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition()

                        // 20 items per page
                        if (
                            totalItemCount >= 20 &&
                            totalItemCount - visibleItemCount <= firstVisibleItem + 5
                        ) {
                            viewModel.getMovies()
                        }
                    }
                }
            })
        }

        viewBinding.srlPopularMovies.setOnRefreshListener {
            viewModel.getMovies(true)
        }

    }

    override fun collectViewModel() {
        super.collectViewModel()

        collect(viewModel.loadingState) {
            when (it) {
                PaginationLoadingIndicator.NONE -> {
                    viewBinding.srlPopularMovies.isRefreshing = false
                    viewBinding.pbLoadMore.gone()
                }
                PaginationLoadingIndicator.SWIPE_REFRESH -> {
                    viewBinding.srlPopularMovies.isRefreshing = true
                }
                PaginationLoadingIndicator.MORE -> {
                    viewBinding.pbLoadMore.show()
                }
            }
        }

        collect(viewModel.movies) {
            moviesAdapter.submitList(it)
        }

        collect(viewModel.errorMessage) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

    }

    override fun loadContent() {
        super.loadContent()
        viewModel.getMovies(true)
    }

    override fun onItemClick(item: Movie) {
        goTo(PopularMoviesFragmentDirections.actionPopularMoviesFragmentToMovieDetailFragment(item.id))
    }

}