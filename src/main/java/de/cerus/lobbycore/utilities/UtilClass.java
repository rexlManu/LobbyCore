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

package de.cerus.lobbycore.utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class UtilClass {
    private static Map<ItemStack, String> lobbyInventory = new HashMap<>();
    private static Map<ItemStack, String> compassContent = new HashMap<>();
    private static Inventory compass = Bukkit.createInventory(null, 5 * 9, "§2§lT§a§leleporter");

    public static String itemToStringBlob(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", itemStack);
        return config.saveToString();
    }

    public static ItemStack stringBlobToItem(String stringBlob) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringBlob);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("i", null);
    }

    public static void sendTablist(Player player, String headerString, String footerString) {
        try {
            if (getServerVersion().equalsIgnoreCase("v1_12_R1")) {
                Object header = getNmsClass("ChatComponentText").getConstructor(new Class[]{String.class}).newInstance(new Object[]{ChatColor.translateAlternateColorCodes('&', headerString)});
                Object footer = getNmsClass("ChatComponentText").getConstructor(new Class[]{String.class}).newInstance(new Object[]{ChatColor.translateAlternateColorCodes('&', footerString)});

                Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[0]).newInstance(new Object[0]);

                Field fa = ppoplhf.getClass().getDeclaredField("a");
                fa.setAccessible(true);
                fa.set(ppoplhf, header);
                Field fb = ppoplhf.getClass().getDeclaredField("b");
                fb.setAccessible(true);
                fb.set(ppoplhf, footer);

                Object nmsp = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

                pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, new Object[]{ppoplhf});
            } else if ((getServerVersion().equalsIgnoreCase("v1_9_R1")) ||
                    (getServerVersion().equalsIgnoreCase("v1_9_R2")) ||
                    (getServerVersion().equalsIgnoreCase("v1_10_R1")) ||
                    (getServerVersion().equalsIgnoreCase("v1_11_R1")) ||
                    (getServerVersion().equalsIgnoreCase("v1_12_R1"))) {
                Object header = getNmsClass("ChatComponentText").getConstructor(new Class[]{String.class}).newInstance(new Object[]{ChatColor.translateAlternateColorCodes('&', headerString)});
                Object footer = getNmsClass("ChatComponentText").getConstructor(new Class[]{String.class}).newInstance(new Object[]{ChatColor.translateAlternateColorCodes('&', footerString)});

                Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[]{getNmsClass("IChatBaseComponent")}).newInstance(new Object[]{header});

                Field f = ppoplhf.getClass().getDeclaredField("b");
                f.setAccessible(true);
                f.set(ppoplhf, footer);

                Object nmsp = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

                pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, new Object[]{ppoplhf});
            } else if ((getServerVersion().equalsIgnoreCase("v1_8_R2")) ||
                    (getServerVersion().equalsIgnoreCase("v1_8_R3"))) {
                Object header = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{'text': '" + headerString + "'}"});
                Object footer = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{'text': '" + footerString + "'}"});

                Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[]{getNmsClass("IChatBaseComponent")}).newInstance(new Object[]{header});

                Field f = ppoplhf.getClass().getDeclaredField("b");
                f.setAccessible(true);
                f.set(ppoplhf, footer);

                Object nmsp = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

                pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, new Object[]{ppoplhf});
            } else {
                Object header = getNmsClass("ChatSerializer").getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{'text': '" + headerString + "'}"});
                Object footer = getNmsClass("ChatSerializer").getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{'text': '" + footerString + "'}"});

                Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[]{getNmsClass("IChatBaseComponent")}).newInstance(new Object[]{header});

                Field f = ppoplhf.getClass().getDeclaredField("b");
                f.setAccessible(true);
                f.set(ppoplhf, footer);

                Object nmsp = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
                Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);

                pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, new Object[]{ppoplhf});
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static Class<?> getNmsClass(String nmsClassName)
            throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }

    public static Map<ItemStack, String> getLobbyInventory() {
        return lobbyInventory;
    }

    public static Map<ItemStack, String> getCompassContent() {
        return compassContent;
    }

    public static String locationToString(Location location) {
        return new StringBuilder().append(location.getWorld().getName()).append("#").append(location.getX()).append("#").append(location.getY()).append("#").append(location.getZ()).append("#").append(location.getPitch()).append("#").append(location.getYaw()).toString();
    }

    public static Location locationFromString(String string) {
        String[] strings = string.split("#");
        return new Location(Bukkit.getWorld(strings[0]), Double.valueOf(strings[1]), Double.valueOf(strings[2]), Double.valueOf(strings[3]), Float.valueOf(strings[4]), Float.valueOf(strings[5]));
    }

    public static Inventory getCompass() {
        return compass;
    }
}
