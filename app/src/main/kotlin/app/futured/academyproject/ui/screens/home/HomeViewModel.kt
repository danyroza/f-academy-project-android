package app.futured.academyproject.ui.screens.home

import androidx.lifecycle.viewModelScope
import app.futured.academyproject.domain.GetPlacesUseCase
import app.futured.academyproject.tools.arch.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    override val viewState: HomeViewState,
    private val getPlacesUseCase: GetPlacesUseCase
) : BaseViewModel<HomeViewState>(), Home.Actions {

    init {
        loadCulturalPlaces()
    }

    private fun loadCulturalPlaces() {
        getPlacesUseCase.execute(){
            onSuccess {
                places ->
                viewState.places = places.toPersistentList()
            }
            onError {
                exception ->
                Timber.tag("LoadCulturalPlaces").e(exception.toString())
            }
        }
    }

    // this is action from UI so it overrides method from Home.Actions interface
    override fun navigateToDetailScreen(placeId: Int) {
        sendEvent(NavigateToDetailEvent(placeId))
    }
}
