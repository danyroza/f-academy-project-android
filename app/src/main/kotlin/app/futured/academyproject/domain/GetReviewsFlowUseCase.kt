package app.futured.academyproject.domain

import app.futured.academyproject.data.model.local.Review
import app.futured.academyproject.data.persistence.ReviewsPersistence
import app.futured.arkitekt.crusecases.FlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewsFlowUseCase @Inject constructor(
    private val reviewsPersistence: ReviewsPersistence
) : FlowUseCase<Unit, List<Review>>() {
    override fun build(args: Unit): Flow<List<Review>> =
        reviewsPersistence.observeReviews()
}