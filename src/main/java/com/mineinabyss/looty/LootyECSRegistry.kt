package com.mineinabyss.looty

import com.mineinabyss.geary.ecs.components.with
import com.mineinabyss.geary.minecraft.dsl.attachToGeary
import com.mineinabyss.geary.minecraft.events.Events
import com.mineinabyss.looty.config.LootyTypes
import com.mineinabyss.looty.ecs.actions.Explode
import com.mineinabyss.looty.ecs.actions.Meteor
import com.mineinabyss.looty.ecs.components.ChildItemCache
import com.mineinabyss.looty.ecs.components.PotionComponent
import com.mineinabyss.looty.ecs.components.Screaming
import com.mineinabyss.looty.ecs.components.inventory.SlotType
import com.mineinabyss.looty.ecs.systems.CooldownDisplaySystem
import com.mineinabyss.looty.ecs.systems.ItemTrackerSystem
import com.mineinabyss.looty.ecs.systems.PotionEffectSystem
import com.mineinabyss.looty.ecs.systems.ScreamingSystem
import org.bukkit.entity.Player

fun Looty.attachToGeary() {
    attachToGeary(types = LootyTypes) {
        systems(
            ItemTrackerSystem,
            ScreamingSystem,
            PotionEffectSystem,
            CooldownDisplaySystem,
        )

        actions {
            action(Explode.serializer())
            action(Meteor.serializer())
        }

        components {
            component(Screaming.serializer())
            component(PotionComponent.serializer())
            component(Events.serializer())

            component(SlotType.Held.serializer())
            component(SlotType.Offhand.serializer())
            component(SlotType.Hotbar.serializer())
        }

        bukkitEntityAccess {
            onEntityRegister<Player> { player ->
                add(ChildItemCache(player))
            }

            onEntityUnregister<Player> { gearyPlayer, player ->
                gearyPlayer.with<ChildItemCache> {
                    it.reevaluate(player.inventory)
                    it.clear()
                }
            }
        }
    }
}
