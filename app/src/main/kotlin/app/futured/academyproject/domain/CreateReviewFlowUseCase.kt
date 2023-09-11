package app.futured.academyproject.domain

import app.futured.academyproject.data.model.local.Review
import app.futured.academyproject.data.persistence.ReviewsPersistence
import app.futured.arkitekt.crusecases.UseCase
import javax.inject.Inject

class CreateReviewFlowUseCase @Inject constructor(
    private val persistence: ReviewsPersistence,
) : UseCase<CreateReviewFlowUseCase.Args, Unit>() {

    override suspend fun build(args: Args): Unit =
        persistence.addReview(args.review)

    data class Args(val review: Review)
}

