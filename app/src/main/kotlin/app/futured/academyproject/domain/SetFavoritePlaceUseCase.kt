package app.futured.academyproject.domain

import app.futured.academyproject.data.persistence.PlacesPersistence
import app.futured.arkitekt.crusecases.UseCase
import javax.inject.Inject

class SetFavouritePlaceUseCase @Inject constructor(
    private val placesPersistence: PlacesPersistence
) : UseCase<SetFavouritePlaceUseCase.Args,Unit>() {

    override suspend fun build(args: Args): Unit {
        placesPersistence.toggleFavourite(args.placeId)
    }

    data class Args(val placeId: Int)
}
