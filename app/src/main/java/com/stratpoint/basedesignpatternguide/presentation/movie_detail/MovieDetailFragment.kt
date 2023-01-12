package com.stratpoint.basedesignpatternguide.presentation.movie_detail

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.stratpoint.basedesignpatternguide.R
import com.stratpoint.basedesignpatternguide.constant.AppConstants
import com.stratpoint.basedesignpatternguide.databinding.FragmentMovieDetailBinding
import com.stratpoint.basedesignpatternguide.domain.Movie
import com.stratpoint.basedesignpatternguide.presentation.base.BaseFragment
import com.stratpoint.basedesignpatternguide.util.ImageUtil
import com.stratpoint.basedesignpatternguide.util.extensions.isVisibleOrInvisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>(
    FragmentMovieDetailBinding::inflate
) {

    private val viewModel by viewModels<MovieDetailViewModel>()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun setUpView() {
        super.setUpView()

        viewBinding.containerToolbar.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { defaultBackPress() }
        }

    }

    override fun observeViewModel() {
        super.observeViewModel()

        observe(viewModel.isLoading) {
            viewBinding.pbLoading.isVisibleOrInvisible(it)
        }

        observe(viewModel.movie) {
            populateView(it)
        }

        observe(viewModel.errorMessage) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

    }

    override fun loadContent() {
        super.loadContent()

        viewModel.getMovie(args.movieId)

    }

    private fun populateView(movie: Movie) {

        ImageUtil.load(
            AppConstants.API_BACKDROP_URL.plus(movie.backdropPath),
            viewBinding.ivBackdrop
        )

        ImageUtil.load(
            AppConstants.API_POSTER_URL.plus(movie.posterPath),
            viewBinding.ivPoster
        )

        viewBinding.containerToolbar.toolbar.title = movie.title
        viewBinding.tvTitle.text = movie.title
        viewBinding.tvOverview.text = movie.overview

    }

}