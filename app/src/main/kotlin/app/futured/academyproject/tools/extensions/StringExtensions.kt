package app.futured.academyproject.tools.extensions

import java.text.Normalizer

fun String.removeDiacritics(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace(Regex("[^\\p{ASCII}]"), "")
}