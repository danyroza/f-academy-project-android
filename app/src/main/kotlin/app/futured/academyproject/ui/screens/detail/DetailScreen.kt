package app.futured.academyproject.ui.screens.detail

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.futured.academyproject.R
import app.futured.academyproject.data.model.local.Place
import app.futured.academyproject.navigation.NavigationDestinations
import app.futured.academyproject.tools.arch.EventsEffect
import app.futured.academyproject.tools.arch.onEvent
import app.futured.academyproject.tools.compose.ScreenPreviews
import app.futured.academyproject.tools.extensions.createCallIntent
import app.futured.academyproject.tools.extensions.createEmailIntent
import app.futured.academyproject.tools.extensions.createNavigationIntent
import app.futured.academyproject.tools.extensions.createWebsiteIntent
import app.futured.academyproject.ui.components.Showcase
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun DetailScreen(
    navigation: NavigationDestinations,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    with(viewModel) {
        EventsEffect {
            onEvent<NavigateBackEvent> {
                navigation.popBackStack()
            }
        }

        Detail.Content(
            this,
            viewState.place,
        )
    }
}

object Detail {

    interface Actions {
        fun navigateBack()
        fun onFavorite()
    }

    object PreviewActions : Actions {
        override fun navigateBack() {}
        override fun onFavorite() {}
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Content(
        actions: Actions,
        place: Place?,
        modifier: Modifier = Modifier,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.detail_title)) },
                    actions = {
                        var isFavourite = place?.isFavourite
                        var (icon, color) = if (isFavourite == true) {
                            Icons.Filled.Favorite to MaterialTheme.colorScheme.error
                        } else {
                            Icons.Filled.FavoriteBorder to MaterialTheme.colorScheme.onSurface
                        }

                        IconButton(onClick = actions::onFavorite) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = color,
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { actions.navigateBack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    },
                )
            },
            modifier = modifier,
        ) { contentPadding ->
            place?.let {
                val scrollState = rememberScrollState()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize()
                        .verticalScroll(state = scrollState),
                ) {
                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(place.image1Url)
                                .crossfade(true)
                                .build(),
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(16.dp)
                            .clip(shape = RoundedCornerShape(16.dp)),
                    )
                    Text(text = place.type, style = MaterialTheme.typography.labelLarge)
                    Divider(modifier = Modifier.padding(16.dp))
                    if (place.note != null) {
                        Text(text = place.note, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp), style = MaterialTheme.typography.labelMedium)
                    }
                    if (place.longitude != null && place.latitude != null && place.street != null) {
                        val streetComplete = place.street + " " + place.streetNumber
                        InfoRow(text = streetComplete, intent = place.createNavigationIntent(), icon = Icons.Filled.Navigation)
                    }
                    if (place.phone != null && place.phone != "0") {
                        InfoRow(text = place.phone, intent = place.createCallIntent(), icon = Icons.Filled.Phone)
                    }
                    if (place.webUrl != null) {
                        InfoRow(text = place.webUrl, intent = place.createWebsiteIntent(), icon = Icons.Filled.Language)
                    }
                    if (place.email != null) {
                        InfoRow(text = place.email, intent = place.createEmailIntent(), icon = Icons.Filled.MailOutline)
                    }
                    val inOperationIconColor = if (place.inOperation == true) Icons.Filled.Check to MaterialTheme.colorScheme.primary
                        else Icons.Outlined.Cancel to MaterialTheme.colorScheme.error

                    val brnoPassIconColor = if (place.brnoPass == true) Icons.Filled.Check to MaterialTheme.colorScheme.primary
                        else Icons.Outlined.Cancel to MaterialTheme.colorScheme.error

                    Row(modifier = Modifier.padding(vertical = 8.dp), Arrangement.SpaceEvenly) {
                        Column (modifier = Modifier.padding(horizontal = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(inOperationIconColor.first, contentDescription = "", tint = inOperationIconColor.second)
                            Text(modifier = Modifier.padding(8.dp), text = "V provozu", style = MaterialTheme.typography.labelMedium)
                        }
                        Column (modifier = Modifier.padding(horizontal = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(brnoPassIconColor.first, contentDescription = "", tint = brnoPassIconColor.second)
                            Text(modifier = Modifier.padding(8.dp), text = "BrnoPass", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    text: String,
    intent: Intent,
    icon: ImageVector,
) {
    var context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SelectionContainer {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        IconButton(
            onClick = {
                context.startActivity(intent)
            },
        ) {
            Icon(
                imageVector = icon,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
            )
        }
    }
}


@ScreenPreviews
@Composable
fun DetailContentPreview() {
    Showcase {
        Detail.Content(
            Detail.PreviewActions,
            place = null,
        )
    }
}
