/*
 *  Copyright (c) 2018 Cerus
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cerus
 *
 */

package de.cerus.lobbycore.listeners;

import de.cerus.lobbycore.LobbyCore;
import de.cerus.lobbycore.Settings;
import de.cerus.lobbycore.objects.User;
import de.cerus.lobbycore.utilities.UtilClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = User.getUser(player);
        player.getInventory().clear();

        if (!user.hasPlayedBefore()) {
            user.createNew();
            int coins;
            if ((coins = Integer.parseInt(Settings.getValue(Settings.Setting.FJ_COINS).toString())) > 0)
                user.addCoins(coins);
        }
        if ((Boolean) Settings.getValue(Settings.Setting.JOIN_ENABLED))
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', LobbyCore.getInstance().isPapiEnabled() ? PlaceholderAPI.setPlaceholders(player, Settings.getValue(Settings.Setting.JOIN_MESSAGE).toString().replace("{Player}", player.getName())) : Settings.getValue(Settings.Setting.JOIN_MESSAGE).toString().replace("{Player}", player.getName())));
        if ((Boolean) Settings.getValue(Settings.Setting.TAB_ENABLED))
            UtilClass.sendTablist(player, ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, Settings.getValue(Settings.Setting.TAB_HEADER).toString().replace("{new-line}", "\n").replace("{CurrentPlayers}", "" + Bukkit.getOnlinePlayers().size()).replace("{MaxPlayers}", "" + Bukkit.getMaxPlayers()))), ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, Settings.getValue(Settings.Setting.TAB_FOOTER).toString().replace("{new-line}", "\n").replace("{CurrentPlayers}", "" + Bukkit.getOnlinePlayers().size()).replace("{MaxPlayers}", "" + Bukkit.getMaxPlayers()))));
        if (LobbyCore.getInstance().getFileManager().getSettings().contains("hotbar")) {
            Bukkit.getScheduler().runTaskAsynchronously(LobbyCore.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (String key : LobbyCore.getInstance().getFileManager().getSettings().getConfigurationSection("hotbar").getKeys(false)) {
                        player.getInventory().setItem(Integer.parseInt(key), UtilClass.stringBlobToItem(LobbyCore.getInstance().getFileManager().getSettings().getString("hotbar." + key + ".item")));
                    }
                }
            });
        }
    }
}
