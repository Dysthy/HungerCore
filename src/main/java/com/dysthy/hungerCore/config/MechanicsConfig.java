package com.dysthy.hungerCore.config;
import com.dysthy.hungerCore.commands.subcommands.BountyCommand;
import com.dysthy.hungerCore.util.HcChat;
import com.dysthy.hungerCore.utils.IFileConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
public class MechanicsConfig {
    private static final MechanicsConfig INSTANCE = new MechanicsConfig();
    private IFileConfiguration config;
    public static MechanicsConfig getInstance() {
        return INSTANCE;
    }
    public void init(Plugin plugin) {
        try {
            this.config = new IFileConfiguration("mechanics", plugin);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error al cargar mechanics.yml", e);
        }
    }
    public void reload() {
        if (this.config != null) {
            this.config.reload();
        }
    }
    public void onTributeKill(Player killer, Player victim) {
        if (killer == null || victim == null) {
            return;
        }
        this.applyLifestealReward(killer);
        this.applyBountyReward(killer, victim);
        this.sendKillMessage(killer, victim);
        this.runKillCommands(killer, victim);
    }
    public boolean isLifestealEnabled() {
        if (this.config == null) {
            return false;
        }
        return this.config.getBoolean("lifesteal.enabled", false);
    }
    public double getLifestealHeartsPerKill() {
        if (this.config == null) {
            return 0.0;
        }
        return this.config.getDouble("lifesteal.hearts-per-kill", 1.0);
    }
    public double getLifestealMaxTotalHealth() {
        if (this.config == null) {
            return 20.0;
        }
        return this.config.getDouble("lifesteal.max-total-health", 40.0);
    }
    public void applyLifestealReward(Player killer) {
        if (killer == null || !this.isLifestealEnabled()) {
            return;
        }
        if (killer.isOp()) {
            return;
        }
        double hearts = this.getLifestealHeartsPerKill();
        if (hearts <= 0.0) {
            return;
        }
        double bonusHp = hearts * 2.0;
        AttributeInstance maxHealthAttr = killer.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthAttr == null) {
            return;
        }
        double cap = Math.max(2.0, this.getLifestealMaxTotalHealth());
        double currentBase = maxHealthAttr.getBaseValue();
        double newBase = Math.min(currentBase + bonusHp, cap);
        if (newBase <= currentBase) {
            return;
        }
        maxHealthAttr.setBaseValue(newBase);
        double newHealth = Math.min(killer.getHealth() + bonusHp, newBase);
        killer.setHealth(newHealth);
    }
    public void applyBountyReward(Player killer, Player victim) {
        if (killer == null || victim == null || this.config == null) {
            return;
        }
        if (!this.config.getBoolean("bounty-rewards.enabled", false)) {
            return;
        }
        if (BountyCommand.tributeWanted == null || !victim.getName().equalsIgnoreCase(BountyCommand.tributeWanted)) {
            return;
        }
        if (killer.isOp()) {
            return;
        }
        boolean heal = this.config.getBoolean("bounty-rewards.heal", true);
        boolean feed = this.config.getBoolean("bounty-rewards.feed", true);
        double bonusHearts = this.config.getDouble("bounty-rewards.bonus-hearts", 2.0);
        if (heal) {
            AttributeInstance maxHealthAttr = killer.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealthAttr != null) {
                killer.setHealth(maxHealthAttr.getBaseValue());
            }
        }
        if (feed) {
            killer.setFoodLevel(20);
            killer.setSaturation(20.0f);
        }
        if (bonusHearts > 0.0) {
            double bonusHp = bonusHearts * 2.0;
            AttributeInstance maxHealthAttr = killer.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealthAttr != null) {
                double cap = Math.max(2.0, this.getLifestealMaxTotalHealth());
                double currentBase = maxHealthAttr.getBaseValue();
                double newBase = Math.min(currentBase + bonusHp, cap);
                if (newBase > currentBase) {
                    maxHealthAttr.setBaseValue(newBase);
                    killer.setHealth(Math.min(killer.getHealth() + bonusHp, newBase));
                }
            }
        }
        HcChat.accent(killer, "Recompensa de bounty · " + victim.getName() + " eliminado");
        if (heal) {
            HcChat.success(killer, "Vida restaurada al maximo");
        }
        if (feed) {
            HcChat.success(killer, "Hambre saciada");
        }
        if (bonusHearts > 0.0) {
            HcChat.success(killer, "+" + bonusHearts + " corazones permanentes");
        }
    }
    private void sendKillMessage(Player killer, Player victim) {
        if (this.config == null) {
            return;
        }
        if (!this.config.getBoolean("on-tribute-kill.message.enabled", false)) {
            return;
        }
        String text = this.config.getString("on-tribute-kill.message.text", "");
        if (text == null || text.isBlank()) {
            return;
        }
        String audienceRaw = this.config.getString("on-tribute-kill.message.audience", "broadcast");
        boolean usePrefix = this.config.getBoolean("on-tribute-kill.message.use-prefix", true);
        String line = ChatColor.translateAlternateColorCodes('&', this.interpolate(killer, victim, text));
        String audience = audienceRaw != null ? audienceRaw.trim().toLowerCase(Locale.ROOT) : "broadcast";
        if ("killer".equals(audience)) {
            if (usePrefix) {
                HcChat.tellLegacy(killer, line);
            } else {
                HcChat.rawLegacy(killer, line);
            }
            return;
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (usePrefix) {
                HcChat.tellLegacy(online, line);
            } else {
                HcChat.rawLegacy(online, line);
            }
        }
    }
    private void runKillCommands(Player killer, Player victim) {
        if (this.config == null) {
            return;
        }
        List<String> raw = this.config.getStringList("on-tribute-kill.commands");
        if (raw == null || raw.isEmpty()) {
            return;
        }
        for (String entry : raw) {
            if (entry == null || entry.isBlank()) {
                continue;
            }
            String trimmed = entry.trim();
            String commandLine = trimmed;
            boolean asKiller = false;
            if (trimmed.regionMatches(true, 0, "killer:", 0, 7)) {
                asKiller = true;
                commandLine = trimmed.substring(7).trim();
            } else if (trimmed.regionMatches(true, 0, "console:", 0, 8)) {
                commandLine = trimmed.substring(8).trim();
            }
            if (commandLine.isEmpty()) {
                continue;
            }
            String parsed = this.interpolate(killer, victim, commandLine);
            String withoutSlash = parsed.startsWith("/") ? parsed.substring(1) : parsed;
            try {
                if (asKiller) {
                    if (killer.isOnline()) {
                        Bukkit.dispatchCommand(killer, withoutSlash);
                    }
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), withoutSlash);
                }
            } catch (CommandException ex) {
                Bukkit.getLogger().log(Level.WARNING, "[HungerCore] Comando on-tribute-kill fallo: " + withoutSlash, ex);
            } catch (Throwable t) {
                Bukkit.getLogger().log(Level.WARNING, "[HungerCore] Error ejecutando comando on-tribute-kill: " + withoutSlash, t);
            }
        }
    }
    public String interpolate(Player killer, Player victim, String template) {
        if (template == null) {
            return "";
        }
        String out = template;
        if (killer != null) {
            out = out.replace("{killer}", killer.getName());
            out = out.replace("{killer_uuid}", killer.getUniqueId().toString());
        } else {
            out = out.replace("{killer}", "");
            out = out.replace("{killer_uuid}", "");
        }
        if (victim != null) {
            out = out.replace("{victim}", victim.getName());
            out = out.replace("{victim_uuid}", victim.getUniqueId().toString());
            if (victim.getWorld() != null) {
                out = out.replace("{world}", victim.getWorld().getName());
            } else {
                out = out.replace("{world}", "");
            }
        } else {
            out = out.replace("{victim}", "");
            out = out.replace("{victim_uuid}", "");
            out = out.replace("{world}", "");
        }
        return out;
    }
}
