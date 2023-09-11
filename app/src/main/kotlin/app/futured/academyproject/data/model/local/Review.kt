package app.futured.academyproject.data.model.local

import kotlinx.serialization.Serializable

@Serializable
data class Review (
    val reviewId: Int,
    val reviewedPlaceId: Int,
    // FIXME: This is probably a bad practice. I should be able to obtain the name from the ID of the place.
    val reviewPlaceName: String,
    val stars: Int,
    val description: String,
    val timeVisited: Long,
    var timeCreated: Long? = null,
)