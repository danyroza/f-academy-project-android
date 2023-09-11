package app.futured.academyproject.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import app.futured.academyproject.R
import app.futured.academyproject.navigation.Destination
import app.futured.academyproject.navigation.NavigationDestinations

@Composable
fun BottomAppBar(
    navigation: NavigationDestinations,
) {
    val navBackStackEntry by navigation.getNavController().currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar(
        content = {
            NavigationBar(
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(Icons.Filled.Home, null)
                    },
                    label = {
                        Text(text = stringResource(id = R.string.home_tab_name))
                    },
                    selected = currentDestination?.route == Destination.Home.route,
                    onClick = {
                        navigation.navigateToHomeScreen()
                    },
                )

                NavigationBarItem(
                    icon = {
                        Icon(Icons.Filled.Receipt, null)
                    },
                    label = {
                        Text(text = stringResource(id = R.string.review_tab_name))
                    },
                    selected = currentDestination?.route == Destination.Reviews.route,
                    onClick = {
                        navigation.navigateToReviewsScreen()
                    },
                )
            }
        },
    )
}