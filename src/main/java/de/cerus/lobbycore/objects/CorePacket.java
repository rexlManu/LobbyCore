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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class CorePacket {

    private CorePacketInfo info;

    public CorePacket() {
        this.info = null;
    }

    public CorePacket(CorePacketInfo info) {
        this.info = info;
    }

    public void onPacketLoad() {
    }

    public void onPacketUnload() {
    }

    public void registerBukkitCommand(BukkitCommand command) {
        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(getInfo() == null ? "lobbycore_?" : "lobbycore_" + getInfo().getConfiguration().getString("name"), command);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }

    public void registerBukkitListener(Listener listener) {
        LobbyCore.getInstance().getServer().getPluginManager().registerEvents(listener, LobbyCore.getInstance());
        final List<Listener> listenerList = Arrays.asList(LobbyCore.getInstance().getCorePacketManager().getCorePacketListeners().get(this));
        listenerList.add(listener);
        LobbyCore.getInstance().getCorePacketManager().getCorePacketListeners().put(this, listenerList.toArray(new Listener[listenerList.size()]));
    }

    public CorePacketInfo getInfo() {
        return info;
    }
}
