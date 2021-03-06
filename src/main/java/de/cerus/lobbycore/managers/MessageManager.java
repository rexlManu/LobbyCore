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

package de.cerus.lobbycore.managers;

import de.cerus.lobbycore.LobbyCore;
import de.cerus.lobbycore.Settings;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class MessageManager {

    private static FileConfiguration configuration;

    public static void init() {
        configuration = YamlConfiguration.loadConfiguration(new File(Settings.getValue(Settings.Setting.LANGUAGE_FILE).toString()));
    }

    public static String getMessage(boolean withPrefix, String message) {
        return ChatColor.translateAlternateColorCodes('&', (withPrefix ? (LobbyCore.getInstance().getFileManager().getSettings().getString("prefix") + (getConfiguration().contains(message) ? getConfiguration().getString(message) : "§cError: Message not found. Input: " + message + ", Output: null")) : (getConfiguration().contains(message) ? getConfiguration().getString(message) : "§cError: Message not found. Input: " + message + ", Output: null")));
    }

    public static String getMessage(boolean withPrefix, String message, Player player) {
        if (LobbyCore.getInstance().isPapiEnabled()) {
            return PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', (withPrefix ? (LobbyCore.getInstance().getFileManager().getSettings().getString("prefix") + (getConfiguration().contains(message) ? getConfiguration().getString(message) : "§cError: Message not found. Input: " + message + ", Output: null")) : (getConfiguration().contains(message) ? getConfiguration().getString(message) : "§cError: Message not found. Input: " + message + ", Output: null"))));
        } else return getMessage(withPrefix, message);
    }

    public static FileConfiguration getConfiguration() {
        return configuration;
    }
}
