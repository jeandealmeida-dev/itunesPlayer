package com.jeandealmeida_dev.itunesplayer

object NevermindFixture {
    const val ARTIST = "Nirvana"
    const val ALBUM = "Nevermind"
    const val COLLECTION_ID = 200L

    val smellsLikeTeenSpirit = aTrack(
        id = 1L,
        title = "Smells Like Teen Spirit",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 301_000,
    )
    val inBloom = aTrack(
        id = 2L,
        title = "In Bloom",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 254_000,
    )
    val comeAsYouAre = aTrack(
        id = 3L,
        title = "Come as You Are",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 219_000,
    )
    val breed = aTrack(
        id = 4L,
        title = "Breed",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 183_000,
    )
    val lithium = aTrack(
        id = 5L,
        title = "Lithium",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 257_000,
    )
    val polly = aTrack(
        id = 6L,
        title = "Polly",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 177_000,
    )
    val territorialPissings = aTrack(
        id = 7L,
        title = "Territorial Pissings",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 142_000,
    )
    val drainYou = aTrack(
        id = 8L,
        title = "Drain You",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 223_000,
    )
    val loungeAct = aTrack(
        id = 9L,
        title = "Lounge Act",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 156_000,
    )
    val stayAway = aTrack(
        id = 10L,
        title = "Stay Away",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 212_000,
    )
    val onAPlain = aTrack(
        id = 11L,
        title = "On a Plain",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 193_000,
    )
    val somethingInTheWay = aTrack(
        id = 12L,
        title = "Something in the Way",
        artist = ARTIST,
        album = ALBUM,
        collectionId = COLLECTION_ID,
        durationMs = 231_000,
    )

    val allTracks = listOf(
        smellsLikeTeenSpirit, inBloom, comeAsYouAre, breed, lithium, polly,
        territorialPissings, drainYou, loungeAct, stayAway, onAPlain, somethingInTheWay,
    )

    fun toItunesJson(): String {
        val results = allTracks.joinToString(",\n") { t ->
            """
            {
              "trackId": ${t.id},
              "trackName": "${t.title}",
              "artistName": "${t.artist}",
              "collectionName": "${t.album}",
              "collectionId": ${t.collectionId},
              "artworkUrl100": "${t.artworkUrl}",
              "previewUrl": "${t.previewUrl}",
              "trackPrice": ${t.trackPrice},
              "currency": "${t.currency}",
              "trackTimeMillis": ${t.durationMs}
            }
            """.trimIndent()
        }
        return """{"resultCount": ${allTracks.size}, "results": [$results]}"""
    }
}
