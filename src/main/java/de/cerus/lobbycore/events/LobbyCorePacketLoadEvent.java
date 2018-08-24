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

package de.cerus.lobbycore.events;

import de.cerus.lobbycore.objects.CorePacket;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LobbyCorePacketLoadEvent extends Event {
    private CorePacket packet;

    public LobbyCorePacketLoadEvent(CorePacket packet) {
        this.packet = packet;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public CorePacket getPacket() {
        return packet;
    }
}
