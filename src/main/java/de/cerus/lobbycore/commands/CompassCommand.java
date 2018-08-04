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

package de.cerus.lobbycore.commands;

import de.cerus.lobbycore.LobbyCore;
import de.cerus.lobbycore.utilities.UtilClass;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CompassCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;

        if (args.length == 0) {
            Inventory inventory = Bukkit.createInventory(null, 5 * 9, "§2§lT§a§leleporter");

            if (LobbyCore.getInstance().getFileManager().getSettings().contains("compass-contents")) {
                for (String string : LobbyCore.getInstance().getFileManager().getSettings().getConfigurationSection("compass-contents").getKeys(false)) {
                    inventory.setItem(Integer.parseInt(string.split(";")[0]), UtilClass.stringBlobToItem(LobbyCore.getInstance().getFileManager().getSettings().getString("compass-content." + string)));
                }
            }

            player.openInventory(inventory);
        }
        return false;
    }
}
