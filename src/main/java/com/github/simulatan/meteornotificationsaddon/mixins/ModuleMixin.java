package com.github.simulatan.meteornotificationsaddon.mixins;

import com.github.simulatan.meteornotificationsaddon.hud.NotificationsHudElement;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Module.class)
public class ModuleMixin {
    @Redirect(method = "sendToggledMsg", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/utils/player/ChatUtils;sendMsg(ILnet/minecraft/util/Formatting;Ljava/lang/String;[Ljava/lang/Object;)V"))
    private void sendToggledMsg(int i, Formatting formatting, String s, Object[] objects) {
        if (NotificationsHudElement.getInstance().showChatToggleNotifications.get()) {
            if (i == 0) {
                objects = new Object[]{s};
            }
            ChatUtils.sendMsg(i, formatting, s, objects);
        }
    }
}
