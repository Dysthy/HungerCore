package com.dysthy.hungerCore.util;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class HcChat {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();
    private static final TextColor SEP = TextColor.color(0x6B7280);
    private HcChat() {}
    public static Component brand() {
        return MINI_MESSAGE.deserialize("<gradient:#FBBF24:#EA580C>Hunger</gradient><gold><bold>Craft</bold></gold>");
    }
    public static Component prefixed(Component body) {
        return Component.empty()
            .append(brand())
            .append(Component.space())
            .append(Component.text("»", SEP))
            .append(Component.space())
            .append(body);
    }
    public static void tell(Audience audience, Component body) {
        audience.sendMessage(prefixed(body));
    }
    public static void send(Audience audience, Component body) {
        audience.sendMessage(body);
    }
    public static Component legacy(String sectionMarked) {
        return LEGACY.deserialize(sectionMarked);
    }
    
    public static void tellLegacy(Audience audience, String sectionMarkedLine) {
        tell(audience, legacy(sectionMarkedLine));
    }
    public static void rawLegacy(Audience audience, String sectionMarkedLine) {
        audience.sendMessage(legacy(sectionMarkedLine));
    }
    public static void success(Audience audience, String plain) {
        tell(audience, Component.text(plain, NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
    }
    public static void error(Audience audience, String plain) {
        tell(audience, Component.text(plain, NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
    }
    public static void warn(Audience audience, String plain) {
        tell(audience, Component.text(plain, NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
    }
    public static void info(Audience audience, String plain) {
        tell(audience, Component.text(plain, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
    }
    public static void accent(Audience audience, String plain) {
        tell(audience, Component.text(plain, NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
    }
    
    public static void header(Audience audience, String title) {
        audience.sendMessage(
            Component.empty()
                .append(Component.text("── ", SEP))
                .append(Component.text(title, TextColor.color(0xFB923C), TextDecoration.BOLD))
                .append(Component.text(" ──", SEP))
        );
    }
    
    public static void hint(Audience audience, String plain) {
        tell(audience, Component.text(plain, NamedTextColor.DARK_AQUA).decoration(TextDecoration.ITALIC, false));
    }
    
    public static void bullet(Audience audience, String plain) {
        audience.sendMessage(
            Component.text("  ⧫ ", TextColor.color(0x78716C))
                .append(Component.text(plain, NamedTextColor.GRAY))
                .decoration(TextDecoration.ITALIC, false)
        );
    }
}
