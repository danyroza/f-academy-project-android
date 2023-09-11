package app.futured.academyproject.ui.screens.reviews

import app.futured.arkitekt.core.event.Event


sealed class ReviewsEvents : Event<ReviewsViewState>()

data class NavigateToDetailEvent(val placeId: Int) : ReviewsEvents()