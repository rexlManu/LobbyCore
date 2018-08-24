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
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if ((Boolean) Settings.getValue(Settings.Setting.QUIT_ENABLED))
            event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', LobbyCore.getInstance().isPapiEnabled() ? PlaceholderAPI.setPlaceholders(event.getPlayer(), Settings.getValue(Settings.Setting.QUIT_MESSAGE).toString().replace("{Player}", event.getPlayer().getName())) : Settings.getValue(Settings.Setting.QUIT_MESSAGE).toString().replace("{Player}", event.getPlayer().getName())));

    }
}
