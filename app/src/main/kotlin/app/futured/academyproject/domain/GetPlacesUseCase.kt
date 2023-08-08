package app.futured.academyproject.domain

import app.futured.academyproject.data.model.local.Place
import app.futured.academyproject.data.store.PlacesStore
import app.futured.arkitekt.crusecases.UseCase
import javax.inject.Inject

class GetPlacesUseCase @Inject constructor(
    private val placesStore : PlacesStore
) : UseCase<Unit, List<Place>>() {

    override suspend fun build(args: Unit): List<Place> {
        return placesStore.getPlaces()
    }
}
