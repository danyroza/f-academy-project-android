package app.futured.academyproject.tools.extensions

import android.content.Intent
import android.net.Uri
import app.futured.academyproject.data.model.local.Place

fun Place.createNavigationIntent(): Intent = Intent(
    Intent.ACTION_VIEW,
    Uri.parse("geo:${latitude},${longitude}?q=${name}"),
)

fun Place.createCallIntent(): Intent = Intent(
    Intent.ACTION_DIAL,
    Uri.parse("tel:$phone"),
)

fun Place.createWebsiteIntent(): Intent {
    if (webUrl != null && webUrl.startsWith("https:")) {
        return Intent(
            Intent.ACTION_VIEW,
            Uri.parse(webUrl),
        )
    } else {
        return Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://$webUrl"),
        )
    }
}

fun Place.createEmailIntent(): Intent {
    val subject = "Dotaz"
    val message = "Vyplňte email"

    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:$email")
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, message)

    return intent
}