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

package de.cerus.lobbycore.objects;

import de.NeonnBukkit.CoinsAPI.API.CoinsAPI;
import de.cerus.lobbycore.Settings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class User {

    private Player player;
    private File dataFile;
    private FileConfiguration data;
    public User(Player player) {
        this.player = player;
        this.dataFile = new File("plugins//LobbyCore//playerdata//" + player.getUniqueId() + ".yml");
        this.data = YamlConfiguration.loadConfiguration(this.dataFile);
    }

    public static User getUser(Player player) {
        return new User(player);
    }

    public void save() {
        try {
            getData().save(getDataFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPlayedBefore() {
        return getDataFile().exists();
    }

    public void createNew() {
        if (getDataFile().exists()) throw new SecurityException("Playerdata does already exist!");
        getData().set("coins", 0);
        getData().set("firstJoined", System.currentTimeMillis());
    }

    public long getCoins() {
        return (Boolean.valueOf(Settings.getValue(Settings.Setting.COINSAPI_NEONN_BUKKIT).toString()) ? ((long) CoinsAPI.getCoins(getUniqueId().toString())) : (getData().contains("coins") ? getData().getLong("coins") : 0L));
    }

    public void addCoins(int amount) {
        if (Boolean.valueOf(Settings.getValue(Settings.Setting.COINSAPI_NEONN_BUKKIT).toString())) {
            CoinsAPI.addCoins(getUniqueId().toString(), amount);
        } else {
            getData().set("coins", getCoins() + amount);
            save();
        }
    }

    public void removeCoins(int amount) {
        if (Boolean.valueOf(Settings.getValue(Settings.Setting.COINSAPI_NEONN_BUKKIT).toString())) {
            CoinsAPI.removeCoins(getUniqueId().toString(), amount);
        } else {
            getData().set("coins", getCoins() - amount);
            save();
        }
    }

    public boolean hasCoins(User user, long amount) {
        return getCoins() >= amount;
    }

    public UUID getUniqueId() {
        return getPlayer().getUniqueId();
    }

    public Player getPlayer() {
        return player;
    }

    public File getDataFile() {
        return dataFile;
    }

    public FileConfiguration getData() {
        return data;
    }
}
