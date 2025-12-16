package com.example;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
	public static final String MOD_ID = "allay-leash-enhanced";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Allay Leash Enhanced initialized!");
		LOGGER.info("Configuration:");
		LOGGER.info("  Normal leash distance: {}", AllayLeashConfig.NORMAL_LEASH_DISTANCE);
		LOGGER.info("  Elytra leash distance: {}", AllayLeashConfig.ELYTRA_LEASH_DISTANCE);
		LOGGER.info("  Allay speed multiplier: {}", AllayLeashConfig.ALLAY_ELYTRA_SPEED_MULTIPLIER);
		LOGGER.info("  Allay max speed: {}", AllayLeashConfig.ALLAY_MAX_SPEED);
		LOGGER.info("  Speed boost threshold: {}", AllayLeashConfig.SPEED_BOOST_DISTANCE_THRESHOLD);
	}
}