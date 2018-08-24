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

package de.cerus.lobbycore.gadgets;

import de.cerus.lobbycore.managers.MessageManager;
import de.cerus.lobbycore.objects.Gadget;
import de.cerus.lobbycore.objects.User;
import de.cerus.lobbycore.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FlyfeatherGadget extends Gadget {

    public FlyfeatherGadget() {
        setName(MessageManager.getMessage(false, "gadget.flyfeather.name"));
        setLore(MessageManager.getMessage(false, "gadget.flyfeather.lore"));
        setId(0);
        setToClick(new ItemBuilder(Material.FEATHER).build());
        setPrice(0);
    }

    @Override
    public void onClick(User user) {
        Player player = user.getPlayer();

        if (player.getAllowFlight()) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage("§cFly off!");
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("§aFly on!");
        }
    }

}
