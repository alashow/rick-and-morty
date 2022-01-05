/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow

import org.threeten.bp.Duration

object Config {
    const val BASE_HOST = "rickandmortyapi.com"
    const val BASE_URL = "https://$BASE_HOST/"
    const val API_BASE_URL = "https://$BASE_HOST/"

    const val PLAYSTORE_ID = "tm.alashow.rickmorty"
    const val PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=$PLAYSTORE_ID"

    val API_TIMEOUT = Duration.ofSeconds(40).toMillis()
}
