package com.shinedev.digitalent.data.story

interface StoryModel {
    val id: String
    val name: String
    val description: String
    val photoUrl: String
    val createdAt: String
    val lat: Double
    val lon: Double
}