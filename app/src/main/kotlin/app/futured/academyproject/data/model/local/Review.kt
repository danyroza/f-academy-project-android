package app.futured.academyproject.data.model.local

data class Review (
    val reviewId: Int,
    val reviewedPlaceId: Int,
    val stars: Int,
    val description: String,
)