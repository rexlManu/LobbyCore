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

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

public class ItemBuilder {

    private ItemStack stack;

    /**
     * @param mat
     */
    public ItemBuilder(Material mat) {
        this.stack = new ItemStack(mat, 1);
    }

    /**
     * @param mat
     * @param subID
     */
    public ItemBuilder(Material mat, int subID) {
        this.stack = new ItemStack(mat, (short) subID);
    }

    /**
     * @param id
     * @param subID
     */
    @SuppressWarnings("deprecation")
    public ItemBuilder(int id, int subID) {
        this.stack = new ItemStack(id, (short) subID);
    }

    /**
     * @param stack
     */
    public ItemBuilder(ItemStack stack) {
        this.stack = stack;
    }

    /**
     * @param amount
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder setAmount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    /**
     * @param subID
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder setSubID(int subID) {
        stack.setDurability((short) subID);
        return this;
    }

    /**
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder addEnchantmentGlowing() {
        stack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        addFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * @param meta
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder setMeta(ItemMeta meta) {
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * @param id
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder setSkullOwnerID(String id) {
        GameProfile GP = new GameProfile(UUID.randomUUID(), "");

        GP.getProperties().removeAll("textures");
        GP.getProperties().put("textures", new Property("textures", id, null));

        SkullMeta im = (SkullMeta) stack.getItemMeta();

        Field profileField = null;

        try {
            profileField = im.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(im, GP);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        stack.setItemMeta(im);
        return this;
    }

    /**
     * @param owner
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder setSkullOwner(String owner) {
        SkullMeta skullMeta = (SkullMeta) stack.getItemMeta();
        skullMeta.setOwner(owner);
        stack.setItemMeta(skullMeta);
        return this;
    }

    /**
     * @param name
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder setDisplayname(String name) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * @param material
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder setMaterial(Material material) {
        stack.setType(material);
        return this;
    }

    /**
     * @param lore
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder setLore(ArrayList<String> lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * @param lore
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder setLore(String lore) {
        ArrayList<String> loreArray = new ArrayList<>();
        loreArray.add(lore);
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(loreArray);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * @param lore
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder setLore(String[] lore) {
        ArrayList<String> loreArray = new ArrayList<>();
        for (int i = 0; i < lore.length; i++) {
            loreArray.add(lore[i]);
        }
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(loreArray);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * @param ench
     * @param level
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder addEchantment(Enchantment ench, int level) {
        stack.addUnsafeEnchantment(ench, level);
        return this;
    }

    /**
     * @param flag
     * @return
     * @returntype ItemBuilder
     */
    public ItemBuilder addFlags(ItemFlag flag) {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(flag);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * @return
     * @returntype ItemStack
     */
    public ItemStack build() {
        return stack;
    }

}
