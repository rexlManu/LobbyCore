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

package de.cerus.lobbycore;

public class Settings {

    public static Object getValue(Setting setting) {
        return LobbyCore.getInstance().getFileManager().getSettings().get(setting.getConfigPath());
    }

    public enum Setting {
        COINSAPI_NEONN_BUKKIT("coinsapi.neonn_bukkit"),
        LANGUAGE_FILE("language-file"),
        JOIN_ENABLED("join-message.enabled"),
        QUIT_ENABLED("quit-message.enabled"),
        JOIN_MESSAGE("join-message.message"),
        QUIT_MESSAGE("quit-message.message"),
        LOBBY_INVENTORY("lobby-inventory"),
        TAB_ENABLED("tablist.enabled"),
        TAB_HEADER("tablist.header"),
        TAB_FOOTER("tablist.footer"),
        FJ_COINS("first-join.coins");

        private String configPath;

        Setting(String configPath) {
            this.configPath = configPath;
        }

        public String getConfigPath() {
            return configPath;
        }
    }
}
