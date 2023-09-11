package app.futured.academyproject.ui.screens.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.futured.academyproject.R
import app.futured.academyproject.data.model.local.Review
import app.futured.academyproject.navigation.NavigationDestinations
import app.futured.academyproject.ui.components.BottomAppBar
import app.futured.academyproject.ui.components.ReviewCard
import app.futured.academyproject.ui.theme.Grid
import kotlinx.collections.immutable.PersistentList
import my.nanihadesuka.compose.LazyColumnScrollbar

@Composable
fun ReviewsScreen(
    navigation: NavigationDestinations,
    viewModel: ReviewsViewModel = hiltViewModel(),
) {
    with(viewModel) {
        viewModel.loadReviews()

        Reviews.Content(viewModel, viewModel.viewState.reviews, navigation)
    }
}

object Reviews {
    interface Actions {
        fun loadReviews() = Unit

        fun createNewReview() = Unit
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Content(
        actions: Actions,
        reviews: PersistentList<Review>,
        navigation: NavigationDestinations,
        modifier: Modifier = Modifier,
    ) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        val listState = rememberLazyListState()

        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.reviews_title),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Grid.d1),
                    ),
                    scrollBehavior = scrollBehavior,
                )
            },
            content = { innerPadding ->
                when {
                    reviews.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(id = R.string.no_reviews_message),
                                fontSize = 24.sp,
                            )
                        }
                    }

                    reviews.isNotEmpty() -> {
                        LazyColumnScrollbar(
                            listState = listState,
                            thumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            thumbSelectedColor = MaterialTheme.colorScheme.surfaceTint,
                        ) {
                            LazyColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                contentPadding = innerPadding,
                                verticalArrangement = Arrangement.spacedBy(Grid.d1),
                                modifier = Modifier
                                    .fillMaxSize(),
                                state = listState,
                            ) {
                                items(reviews) { review ->
                                    ReviewCard(
                                        review,
                                        onClick = { /* TODO navigate to review detail */},
                                    )
                                }
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { actions.createNewReview() },
                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                ) {
                    Icon(Icons.Filled.Add, "Localized description")
                }
            },
            bottomBar = {
                BottomAppBar(navigation)
            }
        )
    }
}