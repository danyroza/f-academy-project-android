package app.futured.academyproject.tools.extensions

import android.content.Intent
import android.net.Uri
import app.futured.academyproject.data.model.local.Place


// TODO 8. Create navigation intent
fun Place.createNavigationIntent(): Intent = Intent(
    Intent.ACTION_VIEW,
    Uri.parse("geo:${latitude},${longitude}?q=${name}")
)