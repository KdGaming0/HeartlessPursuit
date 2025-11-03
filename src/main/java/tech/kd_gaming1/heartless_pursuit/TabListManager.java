package tech.kd_gaming1.heartless_pursuit;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import java.util.HashMap;
import java.util.UUID;
import static tech.kd_gaming1.heartless_pursuit.config.HeartlessPursuitConfig.SHOW_TAB_INDICATOR;

public class TabListManager {
    private static final HashMap<UUID, Integer> lastLevelCache = new HashMap<>();

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            updatePlayerTabInfo(player);
            lastLevelCache.put(player.getUuid(), player.experienceLevel);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            lastLevelCache.remove(handler.player.getUuid());
        });
    }

    public static void onXpLevelChange(ServerPlayerEntity player) {
        int currentLevel = player.experienceLevel;
        UUID playerUUID = player.getUuid();

        if (!lastLevelCache.containsKey(playerUUID) || lastLevelCache.get(playerUUID) != currentLevel) {
            lastLevelCache.put(playerUUID, currentLevel);
            updatePlayerTabInfo(player);
        }
    }

    private static void updatePlayerTabInfo(ServerPlayerEntity player) {
        MinecraftServer server = player.getEntityWorld().getServer();

        ServerScoreboard scoreboard = server.getScoreboard();
        String playerName = player.getName().getString();
        String teamName = "xp_" + player.getUuid().toString().substring(0, 8);

        // If disabled, clear any previously set team indicator and exit
        if (!SHOW_TAB_INDICATOR) {
            Team team = scoreboard.getTeam(teamName);
            if (team != null) {
                team.setSuffix(Text.literal(""));
            }
            scoreboard.clearTeam(playerName);
            return;
        }

        // Otherwise update the team suffix with the current XP level
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.addTeam(teamName);
        }

        String xpInfo = " §a[Level: " + player.experienceLevel + "]";
        team.setSuffix(Text.literal(xpInfo));

        scoreboard.clearTeam(playerName);
        scoreboard.addScoreHolderToTeam(playerName, team);
    }
}