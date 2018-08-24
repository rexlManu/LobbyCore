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
import de.cerus.lobbycore.managers.MessageManager;
import de.cerus.lobbycore.utilities.UtilClass;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompassCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;

        if (args.length == 0) {
            player.openInventory(UtilClass.getCompass());
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("additem")) {
                if (!player.hasPermission("lc.admin.addcompassitem")) {
                    player.sendMessage(MessageManager.getMessage(true, "no-perms", player));
                    return false;
                }
                if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()).getType() == Material.AIR) {
                    player.sendMessage(MessageManager.getMessage(true, "wrong-material", player));
                    return false;
                }
                if (!args[1].matches("\\d+")) {
                    player.sendMessage(MessageManager.getMessage(true, "only-numbers", player));
                    return false;
                }

                String locString = UtilClass.locationToString(player.getLocation());
                int slot = Integer.parseInt(args[1]);
                LobbyCore.getInstance().getFileManager().getSettings().set("compass-content." + slot + ".location", locString);
                LobbyCore.getInstance().getFileManager().getSettings().set("compass-content." + slot + ".item", UtilClass.itemToStringBlob(player.getInventory().getItem(player.getInventory().getHeldItemSlot())));
                LobbyCore.getInstance().getFileManager().save();

                player.sendMessage(MessageManager.getMessage(true, "compass-updated-successfully", player));
            }
        }
        return false;
    }
}
