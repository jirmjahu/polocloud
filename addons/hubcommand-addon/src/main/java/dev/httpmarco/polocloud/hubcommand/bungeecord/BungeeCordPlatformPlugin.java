/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.hubcommand.bungeecord;

import dev.httpmarco.polocloud.hubcommand.bungeecord.command.HubCommand;
import dev.httpmarco.polocloud.hubcommand.configuration.ConfigManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeCordPlatformPlugin extends Plugin {

    @Override
    public void onEnable() {
        var configManager = new ConfigManager();
        var messagesConfiguration = configManager.getMessagesConfiguration();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand(messagesConfiguration));
    }
}
