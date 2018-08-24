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
import de.cerus.lobbycore.utilities.UtilClass;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandTest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("testitem")) {
                player.sendMessage(UtilClass.itemToStringBlob(player.getItemInHand()));
                player.getInventory().addItem(UtilClass.stringBlobToItem(UtilClass.itemToStringBlob(player.getItemInHand())));
            } else if (args[0].equalsIgnoreCase("setinv")) {
                for (ItemStack stack : player.getInventory().getContents()) {
                    LobbyCore.getInstance().getFileManager().getSettings().set("lobby-inventory." + player.getInventory().first(stack) + ";/command", UtilClass.itemToStringBlob(stack));
                }
                LobbyCore.getInstance().getFileManager().save();
            } else if (args[0].equalsIgnoreCase("iout")) {
                player.sendMessage(player.getItemInHand() + "");
            } else if (args[0].equalsIgnoreCase("fill")) {
                for (int i = 0; i < 4; i++)
                    LobbyCore.getInstance().getCorePacketManager().fillPacketPagination();
            }
        }
        return false;
    }
}
