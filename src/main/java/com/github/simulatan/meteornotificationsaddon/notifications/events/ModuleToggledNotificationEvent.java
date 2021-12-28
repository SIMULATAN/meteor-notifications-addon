package com.github.simulatan.meteornotificationsaddon.notifications.events;

import com.github.simulatan.meteornotificationsaddon.notifications.NotificationEvent;
import meteordevelopment.meteorclient.systems.modules.Module;

public class ModuleToggledNotificationEvent extends NotificationEvent {

    private final Module toggledModule;
    private final boolean newState;

    public ModuleToggledNotificationEvent(Module toggledModule) {
        this.toggledModule = toggledModule;
        this.newState = toggledModule.isActive();
    }

    public Module getToggledModule() {
        return toggledModule;
    }

    public boolean getNewState() {
        return newState;
    }
}
