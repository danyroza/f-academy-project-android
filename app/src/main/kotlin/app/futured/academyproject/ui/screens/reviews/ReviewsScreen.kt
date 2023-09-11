package app.futured.academyproject.ui.screens.reviews

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import app.futured.academyproject.data.model.local.Review
import app.futured.academyproject.navigation.NavigationDestinations
import kotlinx.collections.immutable.PersistentList

@Composable
fun ReviewsScreen(
    navigation: NavigationDestinations,
    viewModel: ReviewsViewModel = hiltViewModel(),
) {
    Reviews.Content(viewModel, viewModel.viewState.reviews)
}

object Reviews {
    interface Actions {
        fun loadReviews()
    }

    @Composable
    fun Content(
        actions: Actions,
        reviews: PersistentList<Review>,
        modifier: Modifier = Modifier,
    ) {
        // TODO: UI
    }
}