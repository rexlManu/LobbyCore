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
import de.cerus.lobbycore.objects.Gadget;
import de.cerus.lobbycore.objects.User;
import de.cerus.lobbycore.utilities.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GadgetsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;

        if (args.length == 2 && args[0].equalsIgnoreCase("buy")) {
            Gadget gadget = null;
            User user = User.getUser(player);

            for (Gadget gadgets : LobbyCore.getInstance().getGadgetManager().getGadgets()) {
                if (args[1].equalsIgnoreCase(gadgets.getName() + "-" + gadgets.getId())) {
                    gadget = gadgets;
                }
            }

            if (gadget == null) {
                player.sendMessage(MessageManager.getMessage(true, "gadget-doesnt-exist", player));
                return false;
            }

            if (user.hasGadget(gadget)) {
                player.sendMessage(MessageManager.getMessage(true, "gadget-already-bought", player));
                return false;
            }

            if (!user.hasCoins(gadget.getPrice())) {
                player.sendMessage(MessageManager.getMessage(true, "not-enough-coins", player));
                return false;
            }

            user.removeCoins(gadget.getPrice());
            user.addGadget(gadget);

            player.sendMessage(MessageManager.getMessage(true, "gadget-bought-successfully", player));

            return false;
        }

        Inventory inventory = Bukkit.createInventory(null, 2 * 9, "§5§lG§d§ladgets §8| §70");

        if (LobbyCore.getInstance().getGadgetManager().getGadgetPagination().totalPages() > 0) {
            for (ItemStack stack : LobbyCore.getInstance().getGadgetManager().getGadgetPagination().getPage(0)) {
                inventory.addItem(stack);
            }
        }
        inventory.setItem(9, new ItemBuilder(Material.ARROW).setDisplayname("§6«").build());
        inventory.setItem(17, new ItemBuilder(Material.ARROW).setDisplayname("§6»").build());
        player.openInventory(inventory);

        return false;
    }
}
