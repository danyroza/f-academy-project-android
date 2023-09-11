package app.futured.academyproject.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import app.futured.academyproject.data.model.local.Review
import app.futured.academyproject.ui.theme.Grid
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun ReviewCard(review: Review, onClick: (Int) -> Unit, modifier: Modifier = Modifier) {

    // FIXME: This should be somewhere else as a common helper function
    fun convertEpochTimeToReadableFormat(epochTime: Long): String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(epochTime),
            ZoneOffset.UTC
        )

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        return localDateTime.format(formatter)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick(review.reviewedPlaceId) }
            .padding(vertical = Grid.d2, horizontal = Grid.d4)
            .fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = Grid.d2, horizontal = Grid.d4),
        ) {
            Column {
                Text(text = convertEpochTimeToReadableFormat(review.timeVisited), style = MaterialTheme.typography.labelSmall)
                Text(
                    text = review.reviewPlaceName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(Grid.d1))
                Row {
                    Text(
                        text = review.description.take(36) + "...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                    )
                }
            }

            val scoreString = review.stars.toString() + "/5"
            Text(text = scoreString, style = MaterialTheme.typography.headlineLarge)
        }
    }
}