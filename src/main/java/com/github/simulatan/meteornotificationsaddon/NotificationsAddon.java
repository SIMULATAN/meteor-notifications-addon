package com.github.simulatan.meteornotificationsaddon;

import com.github.simulatan.meteornotificationsaddon.commands.NotificationsCommand;
import com.github.simulatan.meteornotificationsaddon.hud.NotificationsHudElement;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import org.slf4j.Logger;

public class NotificationsAddon extends MeteorAddon {

	public static final Logger LOG = LogUtils.getLogger();
	public static final HudGroup HUD_GROUP = new HudGroup("Notifications");

	@Override
	public void onInitialize() {
		LOG.info("Initializing Meteor Notifications Addon by SIMULATAN");

		Commands.get().add(new NotificationsCommand());

		// HUD
		Hud.get().register(NotificationsHudElement.INFO);
	}

	@Override
	public String getPackage() {
		return this.getClass().getPackageName();
	}
}
