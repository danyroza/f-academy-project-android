package app.futured.academyproject.ui.screens.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import app.futured.academyproject.R
import app.futured.academyproject.data.model.local.Place
import app.futured.academyproject.navigation.NavigationDestinations
import app.futured.academyproject.tools.arch.EventsEffect
import app.futured.academyproject.tools.arch.onEvent
import app.futured.academyproject.tools.compose.ScreenPreviews
import app.futured.academyproject.tools.extensions.removeDiacritics
import app.futured.academyproject.tools.preview.PlacesProvider
import app.futured.academyproject.ui.components.PlaceCard
import app.futured.academyproject.ui.components.Showcase
import app.futured.academyproject.ui.theme.Grid
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import my.nanihadesuka.compose.LazyColumnScrollbar
import java.text.Collator
import java.text.Normalizer
import java.util.Locale

@Composable
fun HomeScreen(
    navigation: NavigationDestinations,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    with(viewModel) {
        EventsEffect {
            onEvent<NavigateToDetailEvent> {
                navigation.navigateToDetailScreen(placeId = it.placeId)
            }
        }

        RememberLocationPermissionState(
            onGrant = viewModel::onAllowedLocationPermission,
            onDeny = {
                viewModel.loadCulturalPlaces()
            },
        )

        Home.Content(
            viewModel,
            viewState.places,
            viewState.error,
        )
    }
}

object Home {

    interface Actions {

        fun navigateToDetailScreen(placeId: Int) = Unit

        fun tryAgain() = Unit

        fun onAllowedLocationPermission() = Unit

        fun loadCulturalPlaces() = Unit
    }

    object PreviewActions : Actions

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun Content(
        actions: Actions,
        places: PersistentList<Place>,
        error: Throwable?,
        modifier: Modifier = Modifier,
    ) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        var searchQuery by remember { mutableStateOf("") }

        var sortBy by remember { mutableStateOf(SortBy.NAME_ASCENDING) }

        val onSortByChange = { newSortBy: SortBy ->
            sortBy = newSortBy
        }

        val czechCollator = Collator.getInstance(Locale("cs", "CZ"))

        val listState = rememberLazyListState()

        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                HomeTopAppBar(scrollBehavior, onSortByChange)
            },
            content = { innerPadding ->
                when {
                    error != null -> {
                        Error(onTryAgain = actions::tryAgain)
                    }

                    places.isEmpty() -> {
                        Loading()
                    }

                    places.isNotEmpty() -> {
                        LazyColumnScrollbar(
                            listState = listState,
                            thumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            thumbSelectedColor = MaterialTheme.colorScheme.surfaceTint
                        ) {
                            LazyColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                contentPadding = innerPadding,
                                verticalArrangement = Arrangement.spacedBy(Grid.d1),
                                modifier = Modifier
                                    .fillMaxSize(),
                                state = listState,
                            ) {
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        // Search box
                                        TextField(
                                            value = searchQuery,
                                            singleLine = true,
                                            onValueChange = { newValue ->
                                                searchQuery = newValue
                                            },
                                            placeholder = {
                                                Text("Vyhledejte místo")
                                            },
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .then(if (searchQuery.isEmpty()) Modifier.fillMaxWidth() else Modifier),
                                        )

                                        if (searchQuery.isNotEmpty()) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .clickable {
                                                        searchQuery = ""
                                                    }
                                                    .padding(end = 16.dp),
                                            )
                                        }
                                    }
                                }

                                items(
                                    places
                                        // FIXME: This line is complex and ugly, refactor it
                                        .filter { it.name.removeDiacritics().contains(searchQuery.trim().removeDiacritics(), true) }
                                        .let { filteredPlaces ->
                                            when (sortBy) {
                                                SortBy.NAME_ASCENDING -> filteredPlaces.sortedWith(compareBy { czechCollator.getCollationKey(it.name) })
                                                SortBy.NAME_DESCENDING -> filteredPlaces.sortedWith(compareByDescending { czechCollator.getCollationKey(it.name) })
                                                SortBy.CLOSEST -> filteredPlaces.sortedBy { it.distance }
                                            }
                                        },
                                ) { place ->
                                    PlaceCard(
                                        place = place,
                                        onClick = actions::navigateToDetailScreen,
                                    )
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                            )
                        }
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = null,
                            )
                        }
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                Icons.Filled.Mic,
                                contentDescription = null,
                            )
                        }
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                Icons.Filled.Image,
                                contentDescription = null,
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { /* do something */ },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                        ) {
                            Icon(Icons.Filled.Add, "Localized description")
                        }
                    },
                )
            },
        )
    }

    private fun closestPlaces(filteredPlaces: List<Place>): List<Place> {
        return filteredPlaces
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun HomeTopAppBar(
        scrollBehavior: TopAppBarScrollBehavior,
        onSortByChange: (SortBy) -> Unit,
    ) {
        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.app_map_name),
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
            actions = {
                SortingMenu(onSortByChange)
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Grid.d1),
            ),
            scrollBehavior = scrollBehavior,
        )
    }

    @Composable
    private fun SortingMenu(
        onSortByChange: (SortBy) -> Unit,
    ) {
        var isDropdownMenuOpen by remember { mutableStateOf(false) }

        val dropdownItems: List<SortItem> = listOf(
            SortItem("Name ascending", SortBy.NAME_ASCENDING),
            SortItem("Name descending", SortBy.NAME_DESCENDING),
            SortItem("Closest to you", SortBy.CLOSEST),
        )

        Box(
            Modifier
                .wrapContentSize(Alignment.TopEnd),
        ) {
            IconButton(
                onClick = {
                    isDropdownMenuOpen = true
                },
            ) {
                Icon(
                    Icons.Filled.Sort,
                    contentDescription = "More Menu",
                )
            }
        }


        if (isDropdownMenuOpen) {
            DropdownMenu(
                expanded = isDropdownMenuOpen,
                onDismissRequest = { isDropdownMenuOpen = false },
            ) {
                // Populate the menu items from the list
                dropdownItems.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            isDropdownMenuOpen = false
                            onSortByChange(item.sortOption)
                        },
                    )
                }
            }
        }
    }

    enum class SortBy {
        NAME_ASCENDING,
        NAME_DESCENDING,
        CLOSEST
    }

    private data class SortItem(
        val name: String,
        val sortOption: SortBy,
    )

    @Composable
    private fun Loading() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    private fun Error(
        onTryAgain: () -> Unit,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Grid.d1),
            ) {
                Text(
                    text = "Yups, Error Happened!",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Not our proudest moment. Can you try it again?",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
                Button(onClick = onTryAgain) {
                    Text(
                        text = "Try again",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun RememberLocationPermissionState(
    onGrant: () -> Unit,
    onDeny: () -> Unit,
) {
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        when {
            permissions.getOrDefault(ACCESS_FINE_LOCATION, false) ||
                permissions.getOrDefault(ACCESS_COARSE_LOCATION, false) -> {
                onGrant()
            }

            else -> {
                onDeny()
            }
        }
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        val permissions = arrayOf(
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION,
        )

        if (
            permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it,
                ) == PackageManager.PERMISSION_GRANTED
            }
        ) {
            onGrant()
        } else {
            launcherMultiplePermissions.launch(permissions)
        }
    }
}

@ScreenPreviews
@Composable
private fun HomeContentPreview(@PreviewParameter(PlacesProvider::class) places: PersistentList<Place>) {
    Showcase {
        Home.Content(
            Home.PreviewActions,
            places,
            error = null,
        )
    }
}

@ScreenPreviews
@Composable
private fun HomeContentWithErrorPreview() {
    Showcase {
        Home.Content(
            Home.PreviewActions,
            places = persistentListOf(),
            error = IllegalStateException("Test"),
        )
    }
}

@ScreenPreviews
@Composable
private fun HomeContentWithLoadingPreview() {
    Showcase {
        Home.Content(
            Home.PreviewActions,
            places = persistentListOf(),
            error = null,
        )
    }
}

