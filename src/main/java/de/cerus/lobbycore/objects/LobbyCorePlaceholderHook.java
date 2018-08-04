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

package de.cerus.lobbycore.objects;

import de.cerus.lobbycore.LobbyCore;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LobbyCorePlaceholderHook extends EZPlaceholderHook {
    public LobbyCorePlaceholderHook(Plugin plugin) {
        super(plugin, "lobbycore");
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        if (s.equalsIgnoreCase("version")) {
            return LobbyCore.getInstance().getDescription().getVersion();
        }
        if (s.equalsIgnoreCase("latest_version")) {
            return LobbyCore.getInstance().getUpdater().getNewestVersion();
        }
        if (s.equalsIgnoreCase("uptodate")) {
            return LobbyCore.getInstance().getUpdater().isUpToDate() + "";
        }

        if (player == null) {
            return "";
        }

        if (s.equalsIgnoreCase("player_coins")) {
            return User.getUser(player).getCoins() + "";
        }

        return "?";
    }
}
