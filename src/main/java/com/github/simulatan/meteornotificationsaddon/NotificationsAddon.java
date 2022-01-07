package com.github.simulatan.meteornotificationsaddon;

import com.github.simulatan.meteornotificationsaddon.commands.NotificationsCommand;
import com.github.simulatan.meteornotificationsaddon.hud.NotificationsHudElement;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

public class NotificationsAddon extends MeteorAddon {

	public static final Logger LOG = LogManager.getLogger();

	@Override
	public void onInitialize() {
		LOG.info("Initializing Meteor Notifications Addon by SIMULATAN");

		// Required when using @EventHandler
		MeteorClient.EVENT_BUS.registerLambdaFactory("com.github.simulatan.meteornotificationsaddon", (lookupInMethod, clazz) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, clazz, MethodHandles.lookup()));

        Commands.get().add(new NotificationsCommand());

		// HUD
		HUD hud = Systems.get(HUD.class);
		hud.elements.add(new NotificationsHudElement(hud));
	}
}
