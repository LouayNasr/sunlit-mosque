package com.example.sunlitmosques.models

import kotlinx.serialization.Serializable

@Serializable
data class Mosque(
    val accountNumber: String,
    val minName: String,
    val totalOutstanding: Double,
    val monthlyConsumption: Map<String, Double>
)
