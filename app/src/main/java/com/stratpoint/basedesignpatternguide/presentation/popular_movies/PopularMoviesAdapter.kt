package com.stratpoint.basedesignpatternguide.presentation.popular_movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stratpoint.basedesignpatternguide.constant.AppConstants
import com.stratpoint.basedesignpatternguide.databinding.ItemMovieBinding
import com.stratpoint.basedesignpatternguide.domain.Movie
import com.stratpoint.basedesignpatternguide.presentation.base.BaseRecyclerViewAdapter
import com.stratpoint.basedesignpatternguide.util.ImageUtil
import com.stratpoint.basedesignpatternguide.util.ItemClickListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PopularMoviesAdapter @Inject constructor(): BaseRecyclerViewAdapter<PopularMoviesAdapter.PopularMoviesViewHolder, Movie>() {

    private var itemClickListener:ItemClickListener<Movie>? = null

    fun setOnItemClickListener(listener: ItemClickListener<Movie>) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder =
        PopularMoviesViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        val movie = mainList[holder.adapterPosition]

        holder.tvTitle.text = movie.title

        ImageUtil.load(
            AppConstants.API_POSTER_URL.plus(movie.posterPath),
            holder.ivPoster
        )

        holder.itemView.setOnClickListener { itemClickListener?.onItemClick(movie) }

    }

    override fun getItemCount() = mainList.size

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.overview == newItem.overview &&
                oldItem.releaseDate == newItem.releaseDate &&
                oldItem.ratings == newItem.ratings &&
                oldItem.posterPath == newItem.posterPath &&
                oldItem.backdropPath == newItem.backdropPath
    }

    inner class PopularMoviesViewHolder constructor(itemBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val ivPoster = itemBinding.ivPoster
        val tvTitle = itemBinding.tvTitle
    }

}