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
import de.cerus.lobbycore.utilities.UtilClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private File settingsFile;
    private FileConfiguration settings;

    private File languageEnFile;
    private FileConfiguration languageEn;

    public FileManager() {
        this.settingsFile = new File("plugins//LobbyCore//settings.yml");
        this.settings = YamlConfiguration.loadConfiguration(this.settingsFile);

        this.languageEnFile = new File("plugins//LobbyCore//languages//EN.yml");
        this.languageEn = YamlConfiguration.loadConfiguration(this.languageEnFile);
    }

    public void init() {
        if (!getSettingsFile().exists()) {
            getSettings().set("prefix", "&2&lL&a&lobby&2§lC&a§lore &8×» &7");
            getSettings().set("language-file", "plugins//LobbyCore//languages//EN.yml");
            getSettings().set("coinsapi.neonn_bukkit", false);
            getSettings().set("join-message.enabled", true);
            getSettings().set("join-message.message", "&8&l[&a&l+&8&l] &e{Player}");
            getSettings().set("quit-message.enabled", true);
            getSettings().set("quit-message.message", "&8&l[&c&l-&8&l] &e{Player}");
            getSettings().set("tablist.enabled", true);
            getSettings().set("tablist.header", "§0{new-line}§8« §2§lL§a§lobby§2§lC§a§lore §8»{new-line}§e{CurrentPlayers} §7/ §e{MaxPlayers}{new-line}§0");
            getSettings().set("tablist.footer", "§0{new-line}§bVery powerful lobby-system{new-line}§6Coins: §7%lobbycore_player_coins%{new-line}§0");
            getSettings().set("first-join.coins", 100);

            save();
        }
        if (!getLanguageEnFile().exists()) {

        }

        if (getSettings().contains("lobby-inventory")) {
            for (String s : LobbyCore.getInstance().getFileManager().getSettings().getConfigurationSection("lobby-inventory").getKeys(false)) {
                if (Integer.parseInt(s.split(";")[0]) != -1)
                    UtilClass.getLobbyInventory().put(UtilClass.stringBlobToItem(LobbyCore.getInstance().getFileManager().getSettings().getString("lobby-inventory." + s)), s);
            }
        }
        if (getSettings().contains("compass-content")) {
            for (String s : LobbyCore.getInstance().getFileManager().getSettings().getConfigurationSection("compass-content").getKeys(false)) {
                if (Integer.parseInt(s) != -1)
                    UtilClass.getCompassContent().put(UtilClass.stringBlobToItem(LobbyCore.getInstance().getFileManager().getSettings().getString("compass-content." + s + ".item")), s + ";" + LobbyCore.getInstance().getFileManager().getSettings().getString("compass-content." + s + ".location"));
            }
        }
        for (ItemStack stack : UtilClass.getCompassContent().keySet()) {
            UtilClass.getCompass().setItem(Integer.parseInt(UtilClass.getCompassContent().get(stack).split(";")[0]), stack);
        }
    }

    public boolean save() {
        try {
            getSettings().save(getSettingsFile());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveLanguageEN() {
        try {
            getLanguageEn().save(getLanguageEnFile());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public FileConfiguration getSettings() {
        return settings;
    }

    public File getLanguageEnFile() {
        return languageEnFile;
    }

    public FileConfiguration getLanguageEn() {
        return languageEn;
    }
}
