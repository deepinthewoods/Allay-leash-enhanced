package com.example.mixin;

import com.example.AllayLeashConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Allay.class)
public abstract class AllaySpeedBoostMixin {

    /**
     * Boosts the allay's speed when the player it's leashed to is flying with elytra or riding in a minecart.
     * This helps the allay keep up with the fast-moving player.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void boostSpeedDuringElytraFlight(CallbackInfo ci) {
        Allay allay = (Allay) (Object) this;

        // Check if the allay is leashed
        Entity leashHolder = allay.getLeashHolder();
        if (!(leashHolder instanceof Player player)) {
            return;
        }

        // Check if player is flying with elytra or riding in a minecart
        boolean isInMinecart = player.getVehicle() instanceof AbstractMinecart;
        if (!player.isFallFlying() && !isInMinecart) {
            return;
        }

        // Calculate distance to player
        double distance = allay.distanceTo(player);

        // Only apply boost if allay is far enough away
        if (distance < AllayLeashConfig.SPEED_BOOST_DISTANCE_THRESHOLD) {
            return;
        }

        // Calculate direction vector from allay to player
        Vec3 toPlayer = player.position().subtract(allay.position()).normalize();

        // Get current velocity
        Vec3 currentVelocity = allay.getDeltaMovement();

        // Calculate boosted velocity towards player
        Vec3 boostVelocity = toPlayer.scale(AllayLeashConfig.ALLAY_ELYTRA_SPEED_MULTIPLIER);

        // Combine current velocity with boost (weighted average for smoother movement)
        Vec3 newVelocity = currentVelocity.scale(0.5).add(boostVelocity.scale(0.5));

        // Cap the speed to prevent extreme velocities
        double speed = newVelocity.length();
        if (speed > AllayLeashConfig.ALLAY_MAX_SPEED) {
            newVelocity = newVelocity.normalize().scale(AllayLeashConfig.ALLAY_MAX_SPEED);
        }

        // Apply the new velocity
        allay.setDeltaMovement(newVelocity);
    }
}
