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

import de.cerus.lobbycore.objects.Gadget;
import de.cerus.lobbycore.objects.Pagination;
import de.cerus.lobbycore.utilities.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GadgetManager {

    private List<Gadget> gadgets;
    private Pagination<ItemStack> gadgetPagination;

    public GadgetManager() {
        this.gadgets = new ArrayList<>();
    }

    public Gadget getGadgetById(int id) {
        for (Gadget g : getGadgets()) {
            if (g.getId() == id) return g;
        }
        return null;
    }

    public List<Gadget> getGadgets() {
        return gadgets;
    }

    public void registerGadget(Gadget gadget) {
        gadget.setToClick(new ItemBuilder(gadget.getToClick()).setAmount(1).setDisplayname(gadget.getName()).setLore(gadget.getLore()).build());

        getGadgets().add(gadget);
    }

    public void unregisterGadget(Gadget gadget) {
        if (getGadgets().contains(gadget)) {
            getGadgets().remove(gadget);
            return;
        }

        gadget.setToClick(new ItemBuilder(gadget.getToClick()).setAmount(1).setDisplayname(gadget.getName()).setLore(gadget.getLore()).build());
        getGadgets().remove(gadget);
    }

    public void fillGadgetPagination() {
        List<ItemStack> list = new ArrayList<>();
        for (Gadget gadget : getGadgets()) {
            list.add(new ItemBuilder(gadget.getToClick()).setDisplayname("§e" + gadget.getName()).setLore("§7" + gadget.getLore()).build());
        }
        this.gadgetPagination = new Pagination<>(1 * 9, list);
    }

    public Pagination<ItemStack> getGadgetPagination() {
        return gadgetPagination;
    }
}
