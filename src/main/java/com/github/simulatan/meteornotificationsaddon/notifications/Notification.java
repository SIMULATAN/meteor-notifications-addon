package com.github.simulatan.meteornotificationsaddon.notifications;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Objects;

public class Notification {

    private static int nextId;

    private static synchronized int getNextId() {
        return nextId++;
    }

    private final int id = getNextId();
    @Nonnull
    private final String title;
    @Nullable
    private final String description;
    @Nonnull
    private final Color color;
    /**
     * Protected to only allow accessing in {@link com.github.simulatan.meteornotificationsaddon.notifications.NotificationsManager}
     *
     * Will get set to a proper value as soon as it becomes visible
     */
    protected long startTime = -1;

    @Nullable
    private final NotificationEvent event;

    public Notification(@Nonnull String title) {
        this(title, (String) null);
    }

    public Notification(@Nonnull String title, @Nullable NotificationEvent event) {
        this(title, null, null, event);
    }

    public Notification(@Nonnull String title, @Nullable String description) {
        this(title, description, null);
    }

    public Notification(@Nonnull String title, @Nullable Color color) {
        this(title, null, color);
    }

    public Notification(@Nonnull String title, @Nullable Color color, @Nullable NotificationEvent event) {
        this(title, null, color, event);
    }

    public Notification(@Nonnull String title, @Nullable String description, @Nullable Color color) {
        this(title, description, color, null);
    }

    public Notification(@Nonnull String title, @Nullable String description, @Nullable Color color, @Nullable NotificationEvent event) {
        Objects.requireNonNull(title);
        this.title = title;
        this.description = description;
        this.color = color != null ? color : Color.ORANGE;
        this.event = event;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nonnull
    public Color getColor() {
        return color;
    }

    public long getStartTime() {
        return startTime;
    }

    @Nullable
    public NotificationEvent getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "Notification{" +
            "title='" + title + '\'' +
            ", description='" + description + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return title.equals(that.title) && Objects.equals(description, that.description) && color.equals(that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, color);
    }
}
