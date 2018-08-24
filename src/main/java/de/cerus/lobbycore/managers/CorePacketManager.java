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

package de.cerus.lobbycore.managers;

import de.cerus.lobbycore.LobbyCore;
import de.cerus.lobbycore.events.LobbyCorePacketPreLoadEvent;
import de.cerus.lobbycore.events.LobbyCorePacketUnloadEvent;
import de.cerus.lobbycore.exceptions.LobbyCorePacketException;
import de.cerus.lobbycore.objects.CorePacket;
import de.cerus.lobbycore.objects.CorePacketInfo;
import de.cerus.lobbycore.objects.Pagination;
import de.cerus.lobbycore.utilities.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class CorePacketManager {

    private List<CorePacket> corePackets;
    private Map<CorePacket, Listener[]> corePacketListeners;
    private Map<CorePacket, BukkitCommand[]> corePacketCommands;
    private Pagination<ItemStack> packetPagination;

    public CorePacketManager() {
        this.corePackets = new ArrayList<>();
        this.corePacketListeners = new HashMap<>();
        this.corePacketCommands = new HashMap<>();
    }

    public boolean loadCorePacket(File file) {
        boolean brake = false;

        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
        } catch (IOException ex) {
            brake = true;
            throw new LobbyCorePacketException("File '" + file.getName() + "' is not a valid jar file.");
        }

        if (brake) return false;
        Bukkit.getPluginManager().callEvent(new LobbyCorePacketPreLoadEvent(file));

        Class<? extends CorePacket> mainClass = null;

        FileConfiguration packetConfig = null;
        JarEntry jarEntry = jarFile.getJarEntry("packet.yml");
        if (jarEntry == null) {
            brake = true;
            throw new LobbyCorePacketException("packet.yml file could not be found!");
        } else {
            try {
                packetConfig = YamlConfiguration.loadConfiguration(jarFile.getInputStream(jarEntry));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (brake) return false;

        if (!packetConfig.contains("main")) {
            brake = true;
            throw new LobbyCorePacketException("Packet Configuration does not contain main!");
        }

        if (brake) return false;

        if (!packetConfig.contains("name")) {
            brake = true;
            throw new LobbyCorePacketException("Packet Configuration does not contain name!");
        }

        if (brake) return false;

        try {
            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(file));
            JarEntry entry;

            while (true) {
                entry = jarInputStream.getNextJarEntry();

                if (entry == null) {
                    break;
                }

                if (entry.getName().endsWith(".class")) {
                    String name = entry.getName().replaceAll("/", "\\.");
                    String clazzName = name.substring(0, name.lastIndexOf('.'));
                    if (clazzName.equals(packetConfig.getString("main"))) {
                        URLClassLoader child = new URLClassLoader(
                                new URL[]{new URL(
                                        "file:///" + System.getProperty("user.dir") + "/LobbyCorePackets/" + file.getName())},
                                LobbyCore.class.getClassLoader());
                        Class<?> clazz = Class.forName(clazzName, true, child);
                        try {
                            mainClass = clazz.asSubclass(CorePacket.class);
                        } catch (ClassCastException e) {
                            brake = true;
                            throw new LobbyCorePacketException("Given main class '" + clazzName + "' does not extend CorePacket");
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        }

        if (brake) return false;

        if (mainClass == null) {
            brake = true;
            throw new LobbyCorePacketException("Main class could not be found");
        }

        if (brake) return false;

        try {
            Method method = mainClass.getDeclaredMethod("onPacketLoad");
            method.setAccessible(true);
            method.invoke(mainClass.newInstance());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | InstantiationException ignored) {
        }

        getCorePackets().add(new CorePacket(new CorePacketInfo(packetConfig)));
        return true;
    }

    public boolean unloadCorePacket(CorePacket packet) {
        if (getCorePackets().contains(packet)) {
            packet.onPacketUnload();
            if (getCorePacketListeners().containsKey(packet)) {
                for (Listener listener : getCorePacketListeners().get(packet)) {
                    HandlerList.unregisterAll(listener);
                }
            }
            if (getCorePacketCommands().containsKey(packet)) {
                for (BukkitCommand command : getCorePacketCommands().get(packet)) {
                    command.unregister(getCommandMap());
                }
            }
            getCorePackets().remove(packet);
            Bukkit.getPluginManager().callEvent(new LobbyCorePacketUnloadEvent(packet));
        }
        return !getCorePackets().contains(packet);
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = null;

        try {
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }

        return commandMap;
    }

    public void fillPacketPagination() {
        List<ItemStack> list = new ArrayList<>();
        for (CorePacket packet : LobbyCore.getInstance().getCorePacketManager().getCorePackets()) {
            list.add(new ItemBuilder(Material.PAPER).setDisplayname("§a" + packet.getInfo().getConfiguration().getString("name")).setLore(new String[]{
                    packet.getInfo().getConfiguration().contains("author") ? "§eAuthor: §7" + packet.getInfo().getConfiguration().getString("author") : "§eAuthor: §cNo author",
                    packet.getInfo().getConfiguration().contains("version") ? "§eVersion: §7" + packet.getInfo().getConfiguration().getString("version") : "§eVersion: §cNo version"
            }).build());
        }
        this.packetPagination = new Pagination<>(3 * 9, list);
    }

    public List<CorePacket> getCorePackets() {
        return corePackets;
    }

    public Map<CorePacket, Listener[]> getCorePacketListeners() {
        return corePacketListeners;
    }

    public Map<CorePacket, BukkitCommand[]> getCorePacketCommands() {
        return corePacketCommands;
    }

    public Pagination<ItemStack> getPacketPagination() {
        return packetPagination;
    }
}
