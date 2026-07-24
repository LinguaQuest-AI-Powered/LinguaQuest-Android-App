package com.iti.linguaquest.features.roleplay.domain.model

object BossScenarioProvider {
    val scenarios: List<BossScenario> = listOf(
        BossScenario(
            id = "scenario_market_01",
            worldId = "world_cairo_market",
            bossName = "Haga Sherry",
            roleDescription = "A friendly but firm old fruit vendor in a bustling Cairo market.",
            taskObjective = "Buy two apples and a bunch of bananas for less than 50 pounds."
        ),
        BossScenario(
            id = "scenario_cafe_01",
            worldId = "world_cairo_downtown",
            bossName = "Noura",
            roleDescription = "A busy barista at a popular downtown cafe who speaks very quickly.",
            taskObjective = "Order a large iced latte with oat milk without sugar."
        )
    )
}
