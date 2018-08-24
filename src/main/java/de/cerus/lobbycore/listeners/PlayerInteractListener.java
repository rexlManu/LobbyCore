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
import de.cerus.lobbycore.objects.Gadget;
import de.cerus.lobbycore.objects.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (LobbyCore.getInstance().getFileManager().getSettings().contains("hotbar." + player.getInventory().getHeldItemSlot())) {
                player.chat(LobbyCore.getInstance().getFileManager().getSettings().getString("hotbar." + player.getInventory().getHeldItemSlot() + ".command"));
            } else {
                for (Gadget gadget : LobbyCore.getInstance().getGadgetManager().getGadgets()) {
                    if (gadget.getToClick().isSimilar(event.getItem())) gadget.onClick(new User(player));
                }
            }
        }
    }
}
