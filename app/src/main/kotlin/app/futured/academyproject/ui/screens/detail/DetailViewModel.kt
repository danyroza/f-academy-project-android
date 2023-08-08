package app.futured.academyproject.ui.screens.detail

import android.util.Log
import app.futured.academyproject.domain.GetPlaceUseCase
import app.futured.academyproject.tools.arch.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.logging.LogManager
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    override val viewState: DetailViewState,
    private val getPlaceUseCase: GetPlaceUseCase,
) : BaseViewModel<DetailViewState>(), Detail.Actions {

    init {
        loadPlace()
    }

    private fun loadPlace() {
        getPlaceUseCase.execute(GetPlaceUseCase.Args(viewState.placeId)){
            onSuccess {
                place ->
                viewState.place = place
            }
            onError {
                exception ->
                Timber.tag("LoadPlace").e(exception.toString())
            }
        }
    }

    override fun navigateBack() {
        sendEvent(NavigateBackEvent)
    }
}
