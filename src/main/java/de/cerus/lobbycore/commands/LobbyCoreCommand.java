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
import de.cerus.lobbycore.utilities.ItemBuilder;
import de.cerus.lobbycore.utilities.UtilClass;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LobbyCoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;

        if (args.length == 0) {
            player.sendMessage("§8§m--------------------------------");
            player.sendMessage("§2§lL§a§lobby§2§lC§a§lore");
            player.sendMessage("§e/lc help");
            player.sendMessage("§e/lc about");
            player.sendMessage("§8§m--------------------------------");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("about")) {
                player.sendMessage("§8§m--------------------------------");
                player.sendMessage("§2§lL§a§lobby§2§lC§a§lore");
                player.spigot().sendMessage(new TextComponent("§7Plugin by "), new TextComponent(new ComponentBuilder("§bCerus").event(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://cerus.java-developer.be")).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§8➜ §aWebpage").create())).create()));
                player.spigot().sendMessage(new TextComponent("§7LobbyCore is available at "), new TextComponent(new ComponentBuilder("§6SpigotMC.org").event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://SpigotMC.org")).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§8➜ §6SpigotMC.org").create())).create()), new TextComponent("§7!"));
                player.sendMessage("§8§m--------------------------------");
            } else if (args[0].equalsIgnoreCase("help")) {
                if (!player.hasPermission("lc.admin.seehelp")) {
                    player.sendMessage(MessageManager.getMessage(true, "no-perms", player));
                    return false;
                }

                player.sendMessage("§8§m--------------------------------");
                player.sendMessage("§2§lL§a§lobby§2§lC§a§lore §8| §7Help Page 1");
                player.sendMessage("§e/lc help <1-2>");
                player.sendMessage("§e/compass");
                player.sendMessage("§e/compass additem <slot>");
                player.sendMessage("§e/lc sethotbar");
                player.sendMessage("§e/lc gadgets");
                player.sendMessage("§8§m--------------------------------");
            } else if (args[0].equalsIgnoreCase("sethotbar")) {
                if (!player.hasPermission("lc.admin.sethotbar")) {
                    player.sendMessage(MessageManager.getMessage(true, "no-perms", player));
                    return false;
                }

                LobbyCore.getInstance().getFileManager().getSettings().set("hotbar", null);
                LobbyCore.getInstance().getFileManager().save();

                boolean b = false;
                for (int i = 0; i < 9; i++) {
                    if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() != Material.AIR) {
                        LobbyCore.getInstance().getFileManager().getSettings().set("hotbar." + i + ".item", UtilClass.itemToStringBlob(player.getInventory().getItem(i)));
                        LobbyCore.getInstance().getFileManager().getSettings().set("hotbar." + i + ".command", "/your-command-here");
                        b = true;
                    }
                }
                if (b) LobbyCore.getInstance().getFileManager().save();

                player.sendMessage(MessageManager.getMessage(true, "hotbar-updated-successfully", player));
            } else if (args[0].equalsIgnoreCase("gadgets")) {
                if (!player.hasPermission("lc.admin.gadgets")) {
                    player.sendMessage(MessageManager.getMessage(true, "no-perms", player));
                    return false;
                }

                player.sendMessage(MessageManager.getMessage(true, "gadget-list", player));
                String allGadgets = "§e";
                for (Gadget gadget : LobbyCore.getInstance().getGadgetManager().getGadgets()) {
                    allGadgets += gadget.getName() + " (" + gadget.getId() + ")" + "§8, §e";
                }
                player.sendMessage(allGadgets.equals("§e") ? "§8✖" : allGadgets.substring(0, allGadgets.length() - 6));
            } else if (args[0].equalsIgnoreCase("packets")) {
                Inventory inventory = Bukkit.createInventory(null, 4 * 9, "Packets | 0");
                if (LobbyCore.getInstance().getCorePacketManager().getPacketPagination().totalPages() > 0) {
                    for (ItemStack stack : LobbyCore.getInstance().getCorePacketManager().getPacketPagination().getPage(0)) {
                        inventory.addItem(stack);
                    }
                }
                inventory.setItem(27, new ItemBuilder(Material.ARROW).setDisplayname("§6«").build());
                inventory.setItem(35, new ItemBuilder(Material.ARROW).setDisplayname("§6»").build());
                player.openInventory(inventory);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("help")) {
                if (!player.hasPermission("lc.admin.seehelp")) {
                    player.sendMessage(MessageManager.getMessage(true, "no-perms", player));
                    return false;
                }

                if (args[1].equalsIgnoreCase("1")) {
                    player.chat("/lc help");
                } else if (args[1].equalsIgnoreCase("2")) {
                    player.sendMessage("§8§m--------------------------------");
                    player.sendMessage("§2§lL§a§lobby§2§lC§a§lore §8| §7Help Page 1");
                    player.sendMessage("§e/lc unregistergadget <Gadget>");
                    player.sendMessage("§e/lc packets");
                    player.sendMessage("§8§m--------------------------------");
                }
            } else if (args[0].equalsIgnoreCase("unregistergadget")) {
                if (!player.hasPermission("lc.admin.unregistergadget")) {
                    player.sendMessage(MessageManager.getMessage(true, "no-perms", player));
                    return false;
                }

                Gadget gadget = null;
                for (Gadget gadgets : LobbyCore.getInstance().getGadgetManager().getGadgets()) {
                    if (args[1].equalsIgnoreCase(gadgets.getName())) {
                        gadget = gadgets;
                    }
                }

                if (gadget == null) {
                    player.sendMessage(MessageManager.getMessage(true, "gadget-doesnt-exist", player));
                    return false;
                }

                LobbyCore.getInstance().getGadgetManager().unregisterGadget(gadget);
            }
        }

        return false;
    }
}
