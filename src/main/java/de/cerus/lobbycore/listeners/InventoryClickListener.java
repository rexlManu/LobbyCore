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
import de.cerus.lobbycore.utilities.UtilClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if (event.getInventory().getTitle().equalsIgnoreCase("§2§lT§a§leleporter")) {
            if (UtilClass.getCompassContent().containsKey(event.getCurrentItem())) {
                player.closeInventory();
                player.teleport(UtilClass.locationFromString(UtilClass.getCompassContent().get(event.getCurrentItem()).split(";")[1]));
            }
        } else if (event.getInventory().getTitle().equalsIgnoreCase("§5§lG§d§ladgets")) {
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
        }
    }
}
