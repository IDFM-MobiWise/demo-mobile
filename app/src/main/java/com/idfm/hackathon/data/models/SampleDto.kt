package com.idfm.hackathon.data.models

data class SampleDto(
    val items: List<Item>,
    val message: String,
    val timestamp: String
)

data class Item(
    val id: Int,
    val name: String
)