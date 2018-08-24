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

import org.bukkit.inventory.ItemStack;

public abstract class Gadget {

    private String name;
    private int id;
    private int price;
    private ItemStack toClick;
    private String lore;

    public Gadget(String name, int id, ItemStack toClick, String lore, int price) {
        this.name = name;
        this.id = id;
        this.toClick = toClick;
        this.lore = lore;
        this.price = price;
    }

    public Gadget() {
        this.name = null;
        this.id = 0;
        this.toClick = null;
        this.price = 0;
    }

    public abstract void onClick(User user);

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemStack getToClick() {
        return toClick;
    }

    public void setToClick(ItemStack toClick) {
        this.toClick = toClick;
    }
}
