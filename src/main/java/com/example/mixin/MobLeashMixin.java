package com.example.mixin;

import com.example.AllayLeashConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobLeashMixin {

    /**
     * Modifies the leash distance check to allow longer leashes when the player is flying with elytra.
     * This intercepts the distance comparison in the tickLeash method.
     */
    @ModifyVariable(
        method = "tickLeash",
        at = @At(value = "STORE"),
        ordinal = 0
    )
    private double modifyLeashDistance(double distance) {
        Mob mob = (Mob) (Object) this;

        // Only modify distance for Allays
        if (!(mob instanceof Allay)) {
            return distance;
        }

        // Check if the mob is leashed to a player
        Entity leashHolder = mob.getLeashHolder();
        if (!(leashHolder instanceof Player player)) {
            return distance;
        }

        // Check if player is flying with elytra
        if (player.isFallFlying()) {
            // Scale down the perceived distance so the leash doesn't break as easily
            // If actual distance is 20 and we multiply by 0.5, it appears as 10 to the vanilla check
            double scaleFactor = AllayLeashConfig.NORMAL_LEASH_DISTANCE / AllayLeashConfig.ELYTRA_LEASH_DISTANCE;
            return distance * scaleFactor;
        }

        return distance;
    }
}
