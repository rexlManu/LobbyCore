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

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;

        if (args.length == 0) {
            player.sendMessage("§8§m--------------------------------");
            player.sendMessage("§2§lL§a§lobby§2§lC§a§lore");
            player.sendMessage("§e/lc help");
            player.sendMessage("§8§m--------------------------------");
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("about")) {
                player.sendMessage("§8§m--------------------------------");
                player.sendMessage("§2§lL§a§lobby§2§lC§a§lore");
                player.spigot().sendMessage(new TextComponent("§7Plugin by "), new TextComponent(new ComponentBuilder("§bCerus").event(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://cerus.java-developer.be")).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§8➜ §aWebpage").create())).create()));
                player.spigot().sendMessage(new TextComponent("§7LobbyCore is available at "), new TextComponent(new ComponentBuilder("§6SpigotMC.org").event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://SpigotMC.org")).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§8➜ §6SpigotMC.org").create())).create()), new TextComponent("§7!"));
                player.sendMessage("§8§m--------------------------------");
            }
        }

        return false;
    }
}
