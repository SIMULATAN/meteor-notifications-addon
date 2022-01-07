package com.github.simulatan.meteornotificationsaddon.hud;

import com.github.simulatan.meteornotificationsaddon.notifications.Notification;
import com.github.simulatan.meteornotificationsaddon.notifications.NotificationsManager;
import com.github.simulatan.meteornotificationsaddon.utils.DrawUtils;
import meteordevelopment.meteorclient.gui.screens.HudElementScreen;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.hud.modules.HudElement;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.render.AlignmentX;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.client.MinecraftClient;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

public class NotificationsHudElement extends HudElement {

    private static final int MAX_VALID_NOTIFICATIONS = 5;

    private static NotificationsHudElement instance;

    public static NotificationsHudElement getInstance() {
        return instance;
    }

    public NotificationsHudElement(HUD hud) {
        super(hud, "Notifications", "Displays various notifications on your HUD.", true);
        instance = this;
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Integer> timeToDisplay = sgGeneral.add(new IntSetting.Builder()
        .name("time-to-display")
        .description("The time to display the notifications (in milliseconds).")
        .onChanged(c -> {
            c /= 2;
            if (this.animationDuration.get() > c) {
                this.animationDuration.set(c);
            }
            try {
                Field max = IntSetting.class.getDeclaredField("max");
                max.setAccessible(true);
                max.set(this.animationDuration, c);
                Field sliderMax = IntSetting.class.getDeclaredField("sliderMax");
                sliderMax.setAccessible(true);
                sliderMax.set(this.animationDuration, c);
                if (MinecraftClient.getInstance().currentScreen instanceof HudElementScreen e) {
                    e.reload();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        })
        .defaultValue(3000)
        .min(500)
        .sliderRange(500, 5000)
        .build()
    );

    public final Setting<Integer> maxCount = sgGeneral.add(new IntSetting.Builder()
        .name("max-count")
        .description("The maximum amount of notifications to display.")
        .defaultValue(3)
        .min(1)
        .sliderRange(1, MAX_VALID_NOTIFICATIONS)
        .onChanged(c -> {
            if (this.dummyNotificationsDisplayCount.get() > c) {
                this.dummyNotificationsDisplayCount.set(c);
            }
            try {
                Field max = IntSetting.class.getDeclaredField("max");
                max.setAccessible(true);
                max.set(this.dummyNotificationsDisplayCount, c);
                Field sliderMax = IntSetting.class.getDeclaredField("sliderMax");
                sliderMax.setAccessible(true);
                sliderMax.set(this.dummyNotificationsDisplayCount, c);
                if (MinecraftClient.getInstance().currentScreen instanceof HudElementScreen e) {
                    e.reload();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        })
        .build()
    );

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("The display style of the notifications.")
        .defaultValue(Mode.SIMULATAN)
        .build()
    );

    /**
     * Please suggest more modes to me.
     */
    public enum Mode {
        SIMULATAN;
    }

    private final SettingGroup proportions = settings.createGroup("Proportions", false);

    private final Setting<Integer> notificationHeight = proportions.add(new IntSetting.Builder()
        .name("notification-height")
        .description("The height of the notifications.")
        .defaultValue(60)
        .min(40)
        .sliderRange(40, 100)
        .build()
    );

    private final Setting<Integer> notificationPaddingY = proportions.add(new IntSetting.Builder()
        .name("notification-padding-y")
        .description("The padding between notifications on the Y axis.")
        .defaultValue(10)
        .min(0)
        .sliderRange(0, 20)
        .build()
    );

    private final Setting<Integer> innerNotificationPaddingX = proportions.add(new IntSetting.Builder()
        .name("inner-notification-padding-x")
        .description("The padding between the border of the notifications and the text on the X axis in %.")
        .defaultValue(10)
        .min(0)
        .sliderRange(0, 30)
        .build()
    );

    private final Setting<Integer> width = proportions.add(new IntSetting.Builder()
        .name("width")
        .description("The width of the notifications.")
        .defaultValue(250)
        .min(200)
        .sliderRange(200, 500)
        .build()
    );

    private final Setting<Integer> progressBarHeight = sgGeneral.add(new IntSetting.Builder()
        .name("progress-bar-height")
        .description("The height of the progress bar (0 to disable).")
        .defaultValue(5)
        .min(0)
        .sliderRange(0, 10)
        .build()
    );

    private final Setting<VerticalAlign> verticalAlign = sgGeneral.add(new EnumSetting.Builder<VerticalAlign>()
        .name("vertical-align")
        .description("The vertical alignment of the notifications.")
        .defaultValue(VerticalAlign.BOTTOM)
        .build()
    );

    public enum VerticalAlign {
        TOP,
        BOTTOM;
    }

    public final IntSetting dummyNotificationsDisplayCount = (IntSetting) sgGeneral.add(new IntSetting.Builder()
        .name("dummy-notifications-display-count")
        .description("The amount of dummy notifications to display.")
        .defaultValue(Math.max(maxCount.get() - 2, 1))
        .min(0)
        .sliderRange(0, maxCount.get())
        .max(maxCount.get())
        .build()
    );

    public final Setting<Boolean> showChatToggleNotifications = sgGeneral.add(new BoolSetting.Builder()
        .name("show-chat-toggle-notifications")
        .description("Whether to show module toggle notifications in chat.")
        .defaultValue(true)
        .build()
    );

    public final Setting<List<Module>> modules = sgGeneral.add(new ModuleListSetting.Builder()
        .name("Modules to display")
        .description("The modules to display in the notifications.")
        .defaultValue(Modules.get().getList())
        .build()
    );

    private final Setting<AlignmentX> titleAlignment = sgGeneral.add(new EnumSetting.Builder<AlignmentX>()
        .name("title-alignment")
        .description("The horizontal alignment of the notification titles.")
        .defaultValue(AlignmentX.Center)
        .build()
    );

    private final Setting<AlignmentX> descriptionAlignment = sgGeneral.add(new EnumSetting.Builder<AlignmentX>()
        .name("description-alignment")
        .description("The horizontal alignment of the notification descriptions.")
        .defaultValue(AlignmentX.Center)
        .build()
    );

    private final Setting<SettingColor> backgroundColor = sgGeneral.add(new ColorSetting.Builder()
        .name("background-color")
        .description("The background color of the notifications.")
        .defaultValue(new SettingColor(32, 32, 32, 102))
        .build()
    );

    private final Setting<Double> radius = sgGeneral.add(new DoubleSetting.Builder()
        .name("radius")
        .description("How round the rectangles of the notifications are.")
        .defaultValue(10)
        .min(0)
        .max(10)
        .sliderRange(0, 10)
        .build()
    );

    private final Setting<Integer> animationDuration = sgGeneral.add(new IntSetting.Builder()
        .name("animation-duration")
        .description("The duration of the animation in milliseconds.")
        .defaultValue(250)
        .min(0)
        .max(timeToDisplay.get() / 2)
        .sliderRange(0, timeToDisplay.get() / 2)
        .build()
    );

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(
            width.get(),
            (notificationHeight.get() + progressBarHeight.get()) * maxCount.get() + notificationPaddingY.get() * (maxCount.get() - 1)
        );
    }

    @Override
    public void render(HudRenderer renderer) {
        List<Notification> notifications = NotificationsManager.getNotifications(isInEditor());
        if (notifications == null || notifications.isEmpty()) return;

        Integer notificationHeight = this.notificationHeight.get();
        AlignmentX titleAlignment = this.titleAlignment.get();
        AlignmentX descriptionAlignment = this.descriptionAlignment.get();
        Integer notificationPaddingY = this.notificationPaddingY.get();
        Integer innerNotificationPadding = this.innerNotificationPaddingX.get();
        int titlePaddingX = titleAlignment == AlignmentX.Center ? innerNotificationPadding : innerNotificationPadding / 2;
        int descriptionPaddingX = descriptionAlignment == AlignmentX.Center ? innerNotificationPadding : innerNotificationPadding / 2;
        Integer progressBarHeight = this.progressBarHeight.get();
        Integer timeToDisplay = this.timeToDisplay.get();
        VerticalAlign verticalAlign = this.verticalAlign.get();
        Double radius = this.radius.get();
        // float to allow for floating point math
        Float animationDuration = (float) this.animationDuration.get();

        renderer.addPostTask(() -> {
            if (mode.get() == Mode.SIMULATAN) {
                double baseX = box.getX();
                double baseY = box.getY();

                for (int i = 0; i < notifications.size(); i++) {
                    final Notification notification = notifications.get(i);
                    final long startTime = notification.getStartTime() != 0 ? notification.getStartTime() : System.currentTimeMillis() - ((long) (notifications.size() - i - 1)) * timeToDisplay / dummyNotificationsDisplayCount.get();
                    final long notificationTime = System.currentTimeMillis() - notification.getStartTime();

                    final double x = baseX + (notification.getStartTime() == 0 ? 0 : notificationTime <= animationDuration ?
                        (animationDuration - notificationTime) * box.width / animationDuration
                        : notificationTime >= timeToDisplay - animationDuration ?
                        box.width - ((timeToDisplay - notificationTime) * box.width / animationDuration)
                        : 0);
                    final double y = baseY + (notificationHeight + progressBarHeight + notificationPaddingY) * (verticalAlign == VerticalAlign.TOP ? i : maxCount.get() - i - 1);

                    DrawUtils.renderer.begin();

                    // Background
                    if (radius > 0)
                        DrawUtils.drawRoundedQuad(x, y, box.width, notificationHeight + progressBarHeight, radius, new Color(backgroundColor.get()));
                    else
                        DrawUtils.drawQuad(x, y, box.width, notificationHeight + progressBarHeight, new Color(backgroundColor.get()));

                    // Progress bar
                    double progress = (startTime + timeToDisplay - System.currentTimeMillis()) * box.width / timeToDisplay;
                    if (radius > 0)
                        DrawUtils.drawRoundedQuad(x, y + notificationHeight, progress, progressBarHeight, radius, new Color(notification.getColor()), false);
                    else
                        DrawUtils.drawQuad(x, y + notificationHeight, progress, progressBarHeight, new Color(notification.getColor()));

                    DrawUtils.renderer.render(null);

                    final @Nullable String description = notification.getDescription();

                    final double titleHeight = description != null && !description.isEmpty() ? notificationHeight * 0.7D : notificationHeight;

                    // Title scale
                    double scale = Math.min(
                        box.width *
                            (1F - titlePaddingX / 100F)
                            / DrawUtils.getWidth(notification.getTitle()),
                        titleHeight / TextRenderer.get().getHeight()
                    );
                    TextRenderer.get().begin(scale, false, true);

                    // Title
                    float titleX = titleAlignment == AlignmentX.Center ?
                        (float) (x + box.width / 2 - DrawUtils.getWidth(notification.getTitle()) / 2) : titleAlignment == AlignmentX.Left ?
                        (float) (x + titlePaddingX) :
                        (float) (x + box.width - titlePaddingX - DrawUtils.getWidth(notification.getTitle()));
                    DrawUtils.render(notification.getTitle(), titleX, y + (titleHeight - TextRenderer.get().getHeight()) / 2, java.awt.Color.WHITE, false);
                    TextRenderer.get().end();

                    if (description != null && !description.isEmpty()) {
                        // Description scale
                        scale = Math.min(
                            box.width * (1F - descriptionPaddingX / 100F)
                            / DrawUtils.getWidth(description),
                            notificationHeight * 0.25 / TextRenderer.get().getHeight()
                        );
                        TextRenderer.get().begin(scale, false, true);

                        // Description
                        float descriptionX = descriptionAlignment == AlignmentX.Center ?
                                (float) (x + box.width / 2 - DrawUtils.getWidth(description) / 2) : descriptionAlignment == AlignmentX.Left ?
                                (float) (x + descriptionPaddingX) :
                                (float) (x + box.width - descriptionPaddingX - DrawUtils.getWidth(description));
                        DrawUtils.render(description, descriptionX, y + titleHeight + (progressBarHeight - TextRenderer.get().getHeight()) / 2, java.awt.Color.WHITE, false);
                        TextRenderer.get().end();
                    }
                }
            } else {
                throw new NullPointerException("Mode " + mode.get() + " is not supported!");
            }
        });
    }
}
