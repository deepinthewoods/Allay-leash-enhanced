package com.example.mixin;

import com.example.AllayLeashConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AllayEntity.class)
public abstract class AllaySpeedBoostMixin {

    /**
     * Boosts the allay's speed when the player it's leashed to is flying with elytra.
     * This helps the allay keep up with the fast-moving player.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void boostSpeedDuringElytraFlight(CallbackInfo ci) {
        AllayEntity allay = (AllayEntity) (Object) this;

        // Check if the allay is leashed
        Entity leashHolder = allay.getHoldingEntity();
        if (!(leashHolder instanceof PlayerEntity player)) {
            return;
        }

        // Check if player is flying with elytra
        if (!player.isFallFlying()) {
            return;
        }

        // Calculate distance to player
        double distance = allay.distanceTo(player);

        // Only apply boost if allay is far enough away
        if (distance < AllayLeashConfig.SPEED_BOOST_DISTANCE_THRESHOLD) {
            return;
        }

        // Calculate direction vector from allay to player
        Vec3d toPlayer = player.getPos().subtract(allay.getPos()).normalize();

        // Get current velocity
        Vec3d currentVelocity = allay.getVelocity();

        // Calculate boosted velocity towards player
        Vec3d boostVelocity = toPlayer.multiply(AllayLeashConfig.ALLAY_ELYTRA_SPEED_MULTIPLIER);

        // Combine current velocity with boost (weighted average for smoother movement)
        Vec3d newVelocity = currentVelocity.multiply(0.5).add(boostVelocity.multiply(0.5));

        // Cap the speed to prevent extreme velocities
        double speed = newVelocity.length();
        if (speed > AllayLeashConfig.ALLAY_MAX_SPEED) {
            newVelocity = newVelocity.normalize().multiply(AllayLeashConfig.ALLAY_MAX_SPEED);
        }

        // Apply the new velocity
        allay.setVelocity(newVelocity);
    }
}
