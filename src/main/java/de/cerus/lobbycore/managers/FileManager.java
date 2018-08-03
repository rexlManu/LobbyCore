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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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

    public void init(){
        if(!getSettingsFile().exists()){
            getSettings().set("prefix", "&2§lL§a§lobby§2§lC§a§lore §8×» §7");
            getSettings().set("language-file", "plugins//LobbyCore//languages//EN.yml");
            getSettings().set("coinsapi.neonn_bukkit", true);
        }
        if(!getLanguageEnFile().exists()){

        }
    }

    public boolean save(){
        try {
            getSettings().save(getSettingsFile());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveLanguageEN(){
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
