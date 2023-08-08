package app.futured.academyproject.domain

import app.futured.academyproject.data.model.local.Place
import app.futured.academyproject.data.store.PlacesStore
import app.futured.arkitekt.crusecases.UseCase
import javax.inject.Inject

class GetPlaceUseCase @Inject constructor(
    private val placesStore : PlacesStore,
) : UseCase<GetPlaceUseCase.Args, Place>() {

    override suspend fun build(args: Args): Place {
        return placesStore.getPlace(args.placeId)
            ?: throw IllegalStateException("Place with id ${args.placeId} does not exist")
    }

    data class Args (
        val placeId: Int
    )
}
