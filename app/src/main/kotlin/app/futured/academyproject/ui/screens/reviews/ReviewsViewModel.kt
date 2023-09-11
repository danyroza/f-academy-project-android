package app.futured.academyproject.ui.screens.reviews

import app.futured.academyproject.tools.arch.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    override val viewState: ReviewsViewState,
) : BaseViewModel<ReviewsViewState>(), Reviews.Actions {
    override fun loadReviews() {
        // TODO
    }
}