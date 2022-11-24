package com.github.simulatan.meteornotificationsaddon.mixins;

import com.github.simulatan.meteornotificationsaddon.notifications.Notification;
import com.github.simulatan.meteornotificationsaddon.notifications.NotificationsManager;
import com.github.simulatan.meteornotificationsaddon.notifications.events.ModuleToggledNotificationEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Modules.class)
public class ModulesMixin {

	@Inject(method = "addActive", at = @At(value = "INVOKE", target = "Lmeteordevelopment/orbit/IEventBus;post(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
	private void addActive(Module module, CallbackInfo ci) {
		NotificationsManager.add(new Notification(Utils.nameToTitle(module.name) + " &aON", Color.GREEN, new ModuleToggledNotificationEvent(module)));
	}

	@Inject(method = "removeActive", at = @At(value = "INVOKE", target = "Lmeteordevelopment/orbit/IEventBus;post(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
	private void removeActive(Module module, CallbackInfo ci) {
		NotificationsManager.add(new Notification(Utils.nameToTitle(module.name) + " &4OFF", Color.RED, new ModuleToggledNotificationEvent(module)));
	}
}
