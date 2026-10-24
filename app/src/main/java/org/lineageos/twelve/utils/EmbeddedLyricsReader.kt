/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.twelve.utils

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.extractor.metadata.id3.TextInformationFrame
import androidx.media3.inspector.MetadataRetriever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(UnstableApi::class)
object EmbeddedLyricsReader {
    suspend fun read(
        context: Context,
        uri: Uri,
    ): String? = withContext(Dispatchers.IO) {
        val retriever = MetadataRetriever.Builder(
            context,
            MediaItem.fromUri(uri),
        ).build()

        try {
            val trackGroups = retriever.retrieveTrackGroups().get()

            for (groupIndex in 0 until trackGroups.length) {
                val group = trackGroups.get(groupIndex)

                for (formatIndex in 0 until group.length) {
                    val metadata = group.getFormat(formatIndex).metadata ?: continue

                    for (entryIndex in 0 until metadata.length()) {
                        val entry = metadata.get(entryIndex)

                        if (
                            entry is TextInformationFrame &&
                            entry.id == "USLT"
                        ) {
                            return@withContext entry.values
                                .firstOrNull()
                                ?.takeIf { it.isNotBlank() }
                        }
                    }
                }
            }

            null
        } finally {
            retriever.close()
        }
    }
}
