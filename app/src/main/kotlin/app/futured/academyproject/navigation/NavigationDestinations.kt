package app.futured.academyproject.navigation

import androidx.navigation.NavController

interface NavigationDestinations {
    fun popBackStack()
    fun navigateToDetailScreen(placeId: Int)
    fun navigateToHomeScreen()
    fun navigateToReviewsScreen()
    fun getNavController(): NavController
}

/**
 * Class that triggers navigation actions on provided [navController].
 */
class NavigationDestinationsImpl(private val navController: NavController) : NavigationDestinations {

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun navigateToDetailScreen(placeId: Int) =
        navController.navigate(Destination.Detail.buildRoute(placeId))

    override fun navigateToHomeScreen() {
        navController.navigate(Destination.Home.route)
    }

    override fun navigateToReviewsScreen() {
        navController.navigate(Destination.Reviews.route)
    }

    override fun getNavController(): NavController {
        return navController;
    }
}
