package app.futured.academyproject.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.futured.academyproject.data.model.local.Place
import app.futured.academyproject.tools.preview.PlacesDummyData
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun PlaceItem(place: Place) {
    Row(modifier = Modifier.padding(horizontal = 3.dp).fillMaxWidth()) {
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
                .aspectRatio(1f),
        )
        Column(modifier = Modifier.padding(start = 4.dp)) {
            Text(text = place.name, style = MaterialTheme.typography.h5, color = MaterialTheme.colors.onSurface)
            Text(text = place.type, color = MaterialTheme.colors.onSurface)
        }
    }
}

@Preview
@Composable
private fun PlaceItemPreview() {
    PlaceItem(place = PlacesDummyData.places[0])
}