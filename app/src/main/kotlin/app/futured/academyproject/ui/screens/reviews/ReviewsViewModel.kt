package app.futured.academyproject.ui.screens.reviews

import app.futured.academyproject.data.model.local.Review
import app.futured.academyproject.domain.CreateReviewFlowUseCase
import app.futured.academyproject.domain.GetReviewsFlowUseCase
import app.futured.academyproject.tools.arch.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    override val viewState: ReviewsViewState,
    val getReviewsFlowUseCase: GetReviewsFlowUseCase,
    val createReviewFlowUseCase: CreateReviewFlowUseCase,
) : BaseViewModel<ReviewsViewState>(), Reviews.Actions {

    override fun navigateToDetailScreen(placeId: Int) {
        sendEvent(app.futured.academyproject.ui.screens.reviews.NavigateToDetailEvent(placeId))
    }
    override fun loadReviews() {
        getReviewsFlowUseCase.execute {
            onNext {
                viewState.reviews = it.toPersistentList()
            }
            // FIXME: should handle onError here as well
        }
    }
    override fun createNewReview() {
        // TODO: This should've not been hardcoded, but I got no time left :'(

        val randomId = Random.nextInt(10000)

        val descriptions = listOf(
            "Nádherné místo, plné kultury a historie. Skvělý zážitek pro každého milovníka umění.",
            "Prostě úžasné! Toto kulturní místo má opravdu co nabídnout. Doporučuji všem.",
            "Vynikající místo pro rodinný výlet. Zábava a vzdělání v jednom.",
            "Nemohu uvěřit, jak krásné toto místo je. Zdejší kultura a tradice jsou nádherné.",
            "Pokud hledáte inspiraci a krásu, neváhejte a navštivte toto kulturní místo.",
            "Historie na dosah ruky. Fascinující zážitek pro všechny věkové kategorie.",
            "Jsem nadšená z této kulturní atrakce. Zanechala ve mně nezapomenutelný dojem.",
            "Prostě ohromující. Tohle místo stojí za každý kilometr cesty.",
            "Výlet sem byl skvělý nápad. Moc se mi tu líbí.",
            "Kulturní místo s takovým šarmem a autentičností. Musíte sem zajet!",
        )

        val randomIndex = Random.nextInt(descriptions.size)
        val randomDescription = descriptions[randomIndex]

        val names = listOf(
            "BVV", "Bar naproti", "7. nebe", "Fléda", "Kabaret Špalíček", "Kino Art", "Kumst", "Mahenovo divadlo", "Melodka",
        )

        val randomIndex2 = Random.nextInt(names.size)
        val randomPlaceName = names[randomIndex2]

        val placeId = Random.nextInt(1, 11)


        val currentDateTime = LocalDateTime.now()
        val yesterdayDateTime = currentDateTime.minus(1, ChronoUnit.DAYS)

        val randomStars = Random.nextInt(6)

        val newReview = Review(
            randomId,
            placeId,
            randomPlaceName,
            randomStars,
            randomDescription,
            yesterdayDateTime.toEpochSecond(ZoneOffset.UTC),
        )

        createReviewFlowUseCase.execute(CreateReviewFlowUseCase.Args(newReview)) {
            onSuccess {
                // Nothing
            }
        }
    }
}