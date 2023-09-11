package app.futured.academyproject.data.model.local

import java.util.Date

data class Review (
    val reviewId: Int,
    val reviewedPlaceId: Int,
    val stars: Int,
    val description: String,
    val timeCreated: Date,
    val timeVisited: Date,
)