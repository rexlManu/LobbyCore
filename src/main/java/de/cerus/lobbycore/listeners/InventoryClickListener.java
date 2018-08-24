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
import de.cerus.lobbycore.managers.MessageManager;
import de.cerus.lobbycore.objects.Gadget;
import de.cerus.lobbycore.objects.User;
import de.cerus.lobbycore.utilities.ItemBuilder;
import de.cerus.lobbycore.utilities.UtilClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if (event.getInventory().getTitle().equalsIgnoreCase("§2§lT§a§leleporter")) {
            if (UtilClass.getCompassContent().containsKey(event.getCurrentItem())) {
                player.closeInventory();
                player.teleport(UtilClass.locationFromString(UtilClass.getCompassContent().get(event.getCurrentItem()).split(";")[1]));
            }
        } else if (event.getInventory().getTitle().startsWith("§5§lG§d§ladgets")) {
            if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals("§6»")) {
                int nextPage = Integer.parseInt(event.getInventory().getTitle().replace("§5§lG§d§ladgets §8| §7", "")) + 1;
                if (nextPage >= LobbyCore.getInstance().getGadgetManager().getGadgetPagination().totalPages()) return;
                Inventory inventory = Bukkit.createInventory(null, 4 * 9, "§5§lG§d§ladgets §8| §7" + nextPage);
                for (ItemStack stack : LobbyCore.getInstance().getGadgetManager().getGadgetPagination().getPage(nextPage)) {
                    inventory.addItem(stack);
                }
                inventory.setItem(27, new ItemBuilder(Material.ARROW).setDisplayname("§6«").build());
                inventory.setItem(35, new ItemBuilder(Material.ARROW).setDisplayname("§6»").build());
                player.openInventory(inventory);
                return;
            } else if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals("§6«")) {
                int nextPage = Integer.parseInt(event.getInventory().getTitle().replace("§5§lG§d§ladgets §8| §7", "")) - 1;
                if (nextPage >= LobbyCore.getInstance().getGadgetManager().getGadgetPagination().totalPages() || nextPage < 0)
                    return;
                Inventory inventory = Bukkit.createInventory(null, 4 * 9, "§5§lG§d§ladgets §8| §7" + nextPage);
                for (ItemStack stack : LobbyCore.getInstance().getGadgetManager().getGadgetPagination().getPage(nextPage)) {
                    inventory.addItem(stack);
                }
                inventory.setItem(27, new ItemBuilder(Material.ARROW).setDisplayname("§6«").build());
                inventory.setItem(35, new ItemBuilder(Material.ARROW).setDisplayname("§6»").build());
                player.openInventory(inventory);
                return;
            }

            User user = User.getUser(player);
            Gadget gadget = null;
            for (Gadget gadgets : LobbyCore.getInstance().getGadgetManager().getGadgets()) {
                if (gadgets.getToClick().isSimilar(event.getCurrentItem())) gadget = gadgets;
            }
            if (gadget == null) return;

            if (!user.hasGadget(gadget)) {
                player.sendMessage(MessageManager.getMessage(true, "gadget-not-bought", player).replace("{Price}", gadget.getPrice() + "").replace("{Gadget}", gadget.getName().toUpperCase() + "-" + gadget.getId()));
            } else {
                if (player.getInventory().contains(gadget.getToClick())) {
                    player.getInventory().remove(gadget.getToClick());
                    return;
                }

                player.closeInventory();
                player.getInventory().addItem(gadget.getToClick());
            }
        } else if (event.getInventory().getTitle().startsWith("Packets | ")) {
            if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals("§6»")) {
                int nextPage = Integer.parseInt(event.getInventory().getTitle().replace("Packets | ", "")) + 1;
                if (nextPage >= LobbyCore.getInstance().getCorePacketManager().getPacketPagination().totalPages())
                    return;
                Inventory inventory = Bukkit.createInventory(null, 4 * 9, "Packets | " + nextPage);
                for (ItemStack stack : LobbyCore.getInstance().getCorePacketManager().getPacketPagination().getPage(nextPage)) {
                    inventory.addItem(stack);
                }
                inventory.setItem(27, new ItemBuilder(Material.ARROW).setDisplayname("§6«").build());
                inventory.setItem(35, new ItemBuilder(Material.ARROW).setDisplayname("§6»").build());
                player.openInventory(inventory);
            } else if (event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals("§6«")) {
                int nextPage = Integer.parseInt(event.getInventory().getTitle().replace("Packets | ", "")) - 1;
                if (nextPage >= LobbyCore.getInstance().getCorePacketManager().getPacketPagination().totalPages() || nextPage < 0)
                    return;
                Inventory inventory = Bukkit.createInventory(null, 4 * 9, "Packets | " + nextPage);
                for (ItemStack stack : LobbyCore.getInstance().getCorePacketManager().getPacketPagination().getPage(nextPage)) {
                    inventory.addItem(stack);
                }
                inventory.setItem(27, new ItemBuilder(Material.ARROW).setDisplayname("§6«").build());
                inventory.setItem(35, new ItemBuilder(Material.ARROW).setDisplayname("§6»").build());
                player.openInventory(inventory);
            }
        }
    }
}
