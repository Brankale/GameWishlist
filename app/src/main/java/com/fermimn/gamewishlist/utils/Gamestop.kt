package com.fermimn.gamewishlist.utils

import com.fermimn.gamewishlist.models.Game
import com.fermimn.gamewishlist.models.GamePreview
import com.fermimn.gamewishlist.models.Price
import com.fermimn.gamewishlist.models.Promo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URLEncoder

class Gamestop {

    companion object {

        private val TAG: String = Gamestop::class.java.simpleName

        private const val WEBSITE_URL = "https://www.gamestop.it"
        private const val SEARCH_URL = "$WEBSITE_URL/SearchResult/QuickSearch?q="
        private const val PAGE_URL = "$WEBSITE_URL/Platform/Games/"

        fun getGamePageUrl(gameId: Int) : String = "$PAGE_URL/$gameId"
        private fun getSearchPageUrl(gameTitle: String) : String = "$SEARCH_URL/${URLEncoder.encode(gameTitle, "UTF-8")}"

        fun searchGame(title: String) : ArrayList<GamePreview>? {
            val url: String = getSearchPageUrl(title)
            val html: Document = Jsoup.connect(url).get()
            return getGamesFromSearchPage(html)
        }

        private fun getGamesFromSearchPage(html: Element) : ArrayList<GamePreview>? {
            try {
                val productsList: Elements = html.getElementsByClass("prodList")
                for (productList in productsList) {
                    val gameElements: Elements = productList.getElementsByClass("singleProduct")
                    if (gameElements.isNotEmpty()) {
                        val games = ArrayList<GamePreview>()
                        for (gameElement in gameElements) {
                            val game: GamePreview = getGameFromElementSingleProduct(gameElement)
                            games.add(game)
                        }
                        return games
                    }
                }
                return null
            } catch (ex: Exception) {
                throw GameInitException(ex.message)
            }
        }

        private fun getGameFromElementSingleProduct(singleProduct: Element) : GamePreview {
            val id: Int = singleProduct.getElementsByClass("prodImg")[0].attr("href").split("/")[3].toInt()
            val gamePreview = GamePreview(id)

            with (gamePreview) {
                title = singleProduct.getElementsByTag("h3")[0].text()
                publisher = singleProduct.getElementsByTag("h4")[0].getElementsByTag("strong").text()
                platform = singleProduct.getElementsByTag("h4")[0].textNodes()[0].text().trim()
                cover = singleProduct.getElementsByClass("prodImg")[0].getElementsByTag("img")[0].attr("data-llsrc")
                initPricesFromElementSingleProduct(singleProduct, gamePreview)
            }

            return gamePreview
        }

        private fun initPricesFromElementSingleProduct(singleProduct: Element, gamePreview: GamePreview) {

            var categoryPrices = getPricesByCategory("buyNew", singleProduct)
            categoryPrices?.let {
                gamePreview.newPrice = it.first
                gamePreview.addOldNewPrices(it.second)
                gamePreview.newAvailable = it.third
            }

            categoryPrices = getPricesByCategory("buyUsed", singleProduct)
            categoryPrices?.let {
                gamePreview.usedPrice = it.first
                gamePreview.addOldUsedPrices(it.second)
                gamePreview.usedAvailable = it.third
            }

            categoryPrices = getPricesByCategory("buyPresell", singleProduct)
            categoryPrices?.let {
                gamePreview.preorderPrice = it.first
                gamePreview.addOldPreorderPrices(it.second)
                gamePreview.preorderAvailable = it.third
            }

            categoryPrices = getPricesByCategory("buyDLC", singleProduct)
            categoryPrices?.let {
                gamePreview.digitalPrice = it.first
                gamePreview.addOldDigitalPrices(it.second)
                gamePreview.digitalAvailable = it.third
            }
        }

        private fun getPricesByCategory(category: String, element: Element) : Triple<Float, ArrayList<Float>?, Boolean>? {
            val e: Elements = element.getElementsByClass(category)
            if (e.isNotEmpty()) {
                // <em> tag is present only if there are multiple prices
                val em: Elements = e[0].getElementsByTag("em")

                // if you can buy the product -> class "megaButton buyTier3 cartAddNoRadio"  (new, used prices)
                // if you can't buy the product -> class "megaButton buyTier3 buyDisabled"  (new, used prices)
                // if you can buy the product -> class "megaButton cartAddNoRadio"  (preorder prices)
                // if you can't buy the product -> class "megaButton buyDisabled"  (preorder prices)
                val available = e[0].getElementsByClass("megaButton buyTier3 cartAddNoRadio").size == 1 ||
                                e[0].getElementsByClass("megaButton cartAddNoRadio").size == 1

                return if (em.isEmpty()) {
                    // if there's just one price
                    val price: Float = stringToPrice(e[0].text())
                    Triple(price, null, available)
                } else {
                    // if more than one price is present
                    val price: Float =stringToPrice(em[0].text())
                    val oldPrices = ArrayList<Float>()
                    for (i in 1 until em.size) {
                        oldPrices.add(stringToPrice(em[i].text()))
                    }
                    Triple(price, oldPrices, available)
                }
            }
            return null
        }

        fun getGameById(gameId: Int) : Game {
            val html: Document = Jsoup.connect( getGamePageUrl(gameId) ).get()
            return getGameFromGamePage(gameId, html)
        }

        private fun getGameFromGamePage(gameId: Int, html: Element) : Game {
            try {
                val game = Game(gameId)
                initGameMainInfo(game, html)
                initGameOptionalInfo(game, html)
                initGamePrices(game, html)
                initGamePegi(game, html)
                initGameCover(game, html)
                initGameGallery(game, html)
                initGameDescription(game, html)
                initGamePromos(game, html)
                return game
            } catch (ex: Exception) {                   // catch Exception because HTML can change
                throw GameInitException(ex.message)
            }
        }

        private fun getElementByClass(html: Element, className: String) : Element? {
            val elements: Elements = html.getElementsByClass(className)
            return if (elements.isNotEmpty()) elements[0] else null
        }

        private fun initGameMainInfo(game: Game, html: Element) {
            val prodTitle: Element? = getElementByClass(html, "prodTitle")
            prodTitle?.let {
                with (game) {
                    title = it.getElementsByTag("h1").text()
                    platform = it.getElementsByTag("p")[0].getElementsByTag("span").text()
                    publisher = it.getElementsByTag("strong").text()
                }
            }
        }

        private fun initGameOptionalInfo(game: Game, html: Element) {
            val addedDetInfo: Element? = getElementByClass(html, "addedDetInfo")

            addedDetInfo?.let {
                for (element in it.getElementsByTag("p")) {
                    val labels: Elements = element.getElementsByTag("label")
                    val spans: Elements = element.getElementsByTag("span")

                    if (labels.isNotEmpty() && spans.isNotEmpty()) {
                        val category: String = labels.first().text()
                        val info: Element = spans.first()

                        when (category) {
                            // use replace to make dates comparable
                            "Rilascio" -> game.releaseDate = info.text().replace(".", "/")
                            "Sito Ufficiale" -> game.website = info.getElementsByTag("a").attr("href")
                            "Giocatori" -> game.players = info.text()
                            "Genere" -> {
                                val genres: List<String> = info.text().split("/")
                                for (genre in genres) {
                                    game.addGenre(genre)
                                }
                            }
                        }
                    }
                }

                val validForPromoClass = getElementByClass(it, "ProdottoNonValido")
                game.validForPromo = validForPromoClass?.text() == "Prodotto VALIDO per le promozioni"
            }
        }

        private fun initGamePrices(game: Game, html: Element) {
            val buySection: Element? = getElementByClass(html, "buySection")

            buySection?.let {

                for (svd in it.getElementsByClass("singleVariantDetails")) {
                    val radio: Element = svd.getElementsByTag("input")[0]
                    val svt: Element = svd.getElementsByClass("singleVariantText")[0]

                    when (svt.getElementsByClass("variantName")[0].text()) {
                        "Nuovo" -> {
                            game.newAvailable = radio.attr("data-int") != "0"
                            game.newPrice = getPriceFromSingleVariantText(svt)
                            game.addOldNewPrices(getOldPricesFromSingleVariantText(svt))
                        }
                        "Usato" -> {
                            game.usedAvailable = radio.attr("data-int") != "0"
                            game.usedPrice = getPriceFromSingleVariantText(svt)
                            game.addOldUsedPrices(getOldPricesFromSingleVariantText(svt))
                        }
                        "Prenotazione" -> {
                            game.preorderAvailable = true
                            game.preorderPrice = getPriceFromSingleVariantText(svt)

                            // TODO: need test cases
                            // Init of oldPreorder can be wrong due to too few test cases
                            // Leave UNCOMMENTED, if the app crashes here it can be fixed
                            game.addOldPreorderPrices(getOldPricesFromSingleVariantText(svt))
                        }
                        "Digitale" -> {
                            game.digitalAvailable = radio.attr("data-int") != "0"
                            game.digitalPrice = getPriceFromSingleVariantText(svt)

                            // TODO: need test cases
                            // for old digital prices you should retrieve data in this way
                            // if there are two old prices the behaviour is unknown
                            svt.getElementsByClass("pricetext2").remove()
                            svt.getElementsByClass("detailsLink").remove()
                            val priceStr = svt.text().replace(Regex("[^0-9.,]"),"")
                            if (priceStr.isNotEmpty()) {
                                game.addOldDigitalPrice(stringToPrice(priceStr))
                            }
                        }
                    }
                }
            }
        }

        private fun getPriceFromSingleVariantText(svt: Element) : Float {
            val price: String = svt.getElementsByClass("prodPriceCont")[0].text()
            return stringToPrice(price)
        }

        private fun getOldPricesFromSingleVariantText(svt: Element) : ArrayList<Float> {
            val oldPrices = ArrayList<Float>()
            for (olderPrice in svt.getElementsByClass("olderPrice")) {
                oldPrices.add( stringToPrice(olderPrice.text()) )
            }
            return oldPrices
        }

        private fun stringToPrice(priceStr: String) : Float {
            var price: String = priceStr

            // remove all the characters except for numbers, ',' and '.'
            price = price.replace(Regex("[^0-9.,]"),"")
            // to handle prices over 999,99€ like 1.249,99€
            price = price.replace(".", "")
            // to convert the price in a string that can be parsed
            price = price.replace(',', '.')

            return price.toFloat()
        }

        private fun initGamePegi(game: Game, html: Element) {
            val ageBlock = getElementByClass(html, "ageBlock")
            ageBlock?.let {
                for (element in it.allElements) {
                    when (element.attr("class")) {
                        "pegi18" -> game.addPegi("pegi18")
                        "pegi16" -> game.addPegi("pegi16")
                        "pegi12" -> game.addPegi("pegi12")
                        "pegi7" -> game.addPegi("pegi7")
                        "pegi3" -> game.addPegi("pegi3")
                        "ageDescr BadLanguage" -> game.addPegi("bad-language")
                        "ageDescr violence" -> game.addPegi("violence")
                        "ageDescr online" -> game.addPegi("online")
                        "ageDescr sex" -> game.addPegi("sex")
                        "ageDescr fear" -> game.addPegi("fear")
                        "ageDescr drugs" -> game.addPegi("drugs")
                        "ageDescr discrimination" -> game.addPegi("discrimination")
                        "ageDescr gambling" -> game.addPegi("gambling")
                    }
                }
            }
        }

        private fun initGameCover(game: Game, html: Element) {
            val prodImgMax = getElementByClass(html, "prodImg max")
            game.cover = prodImgMax?.attr("href")
        }

        private fun initGameGallery(game: Game, html: Element) {
            val mediaImages: Element? = getElementByClass(html, "mediaImages")
            mediaImages?.let {
                for (element in it.getElementsByTag("a")) {
                    game.addImage( element.attr("href") )
                }
            }
        }

        private fun initGameDescription(game: Game, html: Element) {
            val prodDesc: Element? = html.getElementById("prodDesc")
            prodDesc?.let {
                // remove unnecessary elements
                it.getElementsByClass("prodToTop").remove()
                it.getElementsByClass("prodSecHead").remove()
                it.getElementsByTag("img").remove()
                game.description = it.outerHtml()
            }
        }

        private fun initGamePromos(game: Game, html: Element) {
            val bonusBlock: Element? = html.getElementById("bonusBlock")
            bonusBlock?.let {
                for (psp in it.getElementsByClass("prodSinglePromo")) {
                    val h4: Elements = psp.getElementsByTag("h4")
                    val p: Elements = psp.getElementsByTag("p")

                    val promo = Promo(h4.text())
                    promo.text = p[0].text()
                    if (p.size > 2) {
                        promo.findMore = p[0].text()
                        promo.findMoreUrl = "$WEBSITE_URL${p[1].getElementsByTag("a").attr("href")}"
                    }
                    game.addPromo(promo)
                }
            }
        }

    }

    private class GameInitException(message: String?) : RuntimeException(message)

}