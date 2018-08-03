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

package de.cerus.lobbycore.utilities;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Updater {
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

    private String resourceId;
    private Plugin plugin;

    private String currentVersion;
    private String newestVersion;
    private boolean upToDate;

    public Updater(String resourceId, Plugin plugin) {
        this.resourceId = resourceId;
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
        this.newestVersion = "?";
        this.upToDate = true;
    }

    public void checkForNewUpdate() {
        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
        consoleCommandSender.sendMessage("§e[UPDATER] §rLooking for new updates..");

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            String spigotMcVersion;

            try {
                HttpsURLConnection connection = (HttpsURLConnection) new java.net.URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openConnection();
                connection.setRequestMethod("GET");
                spigotMcVersion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            } catch (IOException e) {
                consoleCommandSender.sendMessage("§e[UPDATER] §rUpdate checker failed: " + e.getMessage());
                this.newestVersion = "Error";
                return;
            }

            if (currentVersion.equalsIgnoreCase(spigotMcVersion)) {
                consoleCommandSender.sendMessage("§e[UPDATER] §rYou are using the newest version!");
            } else
                consoleCommandSender.sendMessage("§e[UPDATER] §rA new update was found! You're on version " + currentVersion + ", and the newest version is " + spigotMcVersion + ".");
            this.newestVersion = spigotMcVersion;
            this.upToDate = currentVersion.equals(newestVersion);
        });
    }

    public String getNewestVersion() {
        return newestVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public boolean isUpToDate() {
        return upToDate;
    }

}
