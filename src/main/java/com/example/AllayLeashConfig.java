package com.example;

public class AllayLeashConfig {
    // Default leash distance is 10 blocks in vanilla Minecraft
    public static final double NORMAL_LEASH_DISTANCE = 10.0;

    // Increased leash distance when player is flying with elytra
    // This gives more room before the leash breaks
    public static final double ELYTRA_LEASH_DISTANCE = 20.0;

    // Speed multiplier for allay when player is flying with elytra
    // 1.0 = normal speed, 2.0 = double speed, etc.
    public static final double ALLAY_ELYTRA_SPEED_MULTIPLIER = 3.0;

    // Maximum speed the allay can reach (to prevent extreme velocities)
    public static final double ALLAY_MAX_SPEED = 2.0;

    // Distance threshold to start applying speed boost
    // Only boost speed when allay is this far from player
    public static final double SPEED_BOOST_DISTANCE_THRESHOLD = 5.0;
}
