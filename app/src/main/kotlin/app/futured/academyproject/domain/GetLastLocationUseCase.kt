package app.futured.academyproject.domain

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import app.futured.arkitekt.crusecases.UseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetLastLocationUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) : UseCase<Unit, Location?>() {
    @SuppressLint("MissingPermission")
    override suspend fun build(args: Unit): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val location: Location = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                bestLocation = location
            }
        }
        return bestLocation
    }
}