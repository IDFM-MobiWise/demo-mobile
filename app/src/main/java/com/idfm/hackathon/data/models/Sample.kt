package com.idfm.hackathon.data.models

import com.idfm.hackathon.utils.toDate
import java.util.Date

data class Sample(
    val items: List<Item>,
    val message: String,
    val timestamp: Date?
) {

    companion object {
        fun fromDto(dto: SampleDto): Sample {
            return Sample(
                items = dto.items,
                message = dto.message,
                timestamp = dto.timestamp.toDate()
            )
        }
    }

}

