package com.glc.smartcar.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute {
    @Serializable
    data object AuthGraph

    @Serializable
    data object Login

    @Serializable
    data object MainGraph

    @Serializable
    data object History

    @Serializable
    data object NewEvaluation

    @Serializable
    data object Profile
}
