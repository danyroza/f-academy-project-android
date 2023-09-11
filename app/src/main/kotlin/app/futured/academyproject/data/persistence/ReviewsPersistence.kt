package app.futured.academyproject.data.persistence

import app.futured.academyproject.data.model.local.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewsPersistence @Inject constructor(
    private val persistence: Persistence,
) {
    companion object {
        private const val REVIEW_IDS_KEY = "REVIEW_IDS_KEY"
    }

    private val reviewsFlow: MutableStateFlow<List<Review>> = MutableStateFlow(
        persistence.getOrNull(REVIEW_IDS_KEY) ?: emptyList(),
    )

    fun observeReviews(): Flow<List<Review>> = reviewsFlow.asStateFlow()

    fun getReviews(): List<Review> = persistence.getOrNull(REVIEW_IDS_KEY) ?: emptyList()

    fun setReviews(reviews: List<Review>) {
        persistence[REVIEW_IDS_KEY] = reviews
        reviewsFlow.value = reviews
    }
}