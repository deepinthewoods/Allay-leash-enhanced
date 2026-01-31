package com.example.mixin;

import com.example.AllayLeashConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin that modifies Allay leash behavior to prevent leash breaking when players
 * are flying with elytra or riding in minecarts.
 * Targets the Allay class since that's the only mob we want to modify.
 */
@Mixin(Allay.class)
public abstract class MobLeashMixin implements Leashable {

    private boolean isLeashHolderFastMoving() {
        Entity leashHolder = this.getLeashHolder();
        if (leashHolder instanceof Player player) {
            boolean isInMinecart = player.getVehicle() instanceof AbstractMinecart;
            return player.isFallFlying() || isInMinecart;
        }
        return false;
    }

    @Override
    public double leashSnapDistance() {
        if (isLeashHolderFastMoving()) {
            return AllayLeashConfig.ELYTRA_LEASH_DISTANCE;
        }
        return Leashable.super.leashSnapDistance();
    }

    @Override
    public double leashElasticDistance() {
        if (isLeashHolderFastMoving()) {
            return AllayLeashConfig.ELYTRA_LEASH_DISTANCE * 0.8;
        }
        return Leashable.super.leashElasticDistance();
    }
}
