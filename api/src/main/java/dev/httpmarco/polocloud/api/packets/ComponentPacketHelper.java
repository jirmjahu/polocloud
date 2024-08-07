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

package dev.httpmarco.polocloud.api.packets;

import dev.httpmarco.osgan.networking.packet.PacketBuffer;
import dev.httpmarco.polocloud.api.CloudAPI;
import dev.httpmarco.polocloud.api.cluster.NodeData;
import dev.httpmarco.polocloud.api.groups.CloudGroup;
import dev.httpmarco.polocloud.api.groups.platforms.PlatformVersion;
import dev.httpmarco.polocloud.api.player.CloudPlayer;
import dev.httpmarco.polocloud.api.properties.PropertyPool;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.api.services.ServiceState;
import dev.httpmarco.pololcoud.common.Pair;
import org.jetbrains.annotations.NotNull;

public final class ComponentPacketHelper {

    public static void writeService(CloudService cloudService, PacketBuffer buffer) {
        writeGroup(cloudService.group(), buffer);

        buffer.writeInt(cloudService.orderedId())
                .writeUniqueId(cloudService.id())
                .writeInt(cloudService.port())
                .writeEnum(cloudService.state())
                .writeString(cloudService.hostname())
                .writeInt(cloudService.memory())
                .writeInt(cloudService.maxPlayers())
                .writeString(cloudService.runningNode());

        writeProperties(cloudService.properties(), buffer);
    }

    public static CloudService readService(PacketBuffer buffer) {

        var group = readGroup(buffer);
        var orderedId = buffer.readInt();
        var id = buffer.readUniqueId();
        var port = buffer.readInt();
        var state = buffer.readEnum(ServiceState.class);
        var hostname = buffer.readString();
        var maxMemory = buffer.readInt();
        var maxPlayers = buffer.readInt();
        var node = buffer.readString();

        var service = CloudAPI.instance().serviceProvider().generateService(group, orderedId, id, port, state, hostname, maxMemory, maxPlayers, node);
        service.properties().pool().putAll(readProperties(buffer).pool());
        return service;
    }

    public static void writeGroup(CloudGroup group, PacketBuffer codecBuffer) {
        codecBuffer.writeString(group.name());
        codecBuffer.writeString(group.platform().version());
        codecBuffer.writeBoolean(group.platform().proxy());
        codecBuffer.writeInt(group.minOnlineService());
        codecBuffer.writeInt(group.memory());

        codecBuffer.writeString(group.nodeData().id());
        codecBuffer.writeString(group.nodeData().hostname());
        codecBuffer.writeInt(group.nodeData().port());

        writeProperties(group.properties(), codecBuffer);
    }

    private static void writeProperties(@NotNull PropertyPool properties, @NotNull PacketBuffer codecBuffer) {
        codecBuffer.writeInt(properties.size());
        properties.pool().forEach((id, o) -> writeProperty(id, o, codecBuffer));
    }

    public static void writeProperty(String id, String value, @NotNull PacketBuffer buffer) {
        buffer.writeString(id);
        buffer.writeString(value);
    }

    public static @NotNull Pair<String, String> readProperty(@NotNull PacketBuffer buffer) {
        return new Pair<>(buffer.readString(), buffer.readString());
    }

    public static @NotNull PropertyPool readProperties(@NotNull PacketBuffer buffer) {
        var properties = new PropertyPool();
        var elementSize = buffer.readInt();

        for (int i = 0; i < elementSize; i++) {
            var property = readProperty(buffer);
            properties.pool().put(property.first(), property.second());
        }
        return properties;
    }

    public static @NotNull CloudGroup readGroup(@NotNull PacketBuffer buffer) {

        var name = buffer.readString();
        var platformVersion = buffer.readString();
        var platformProxy = buffer.readBoolean();
        var minOnlineServices = buffer.readInt();
        var memory = buffer.readInt();

        var id = buffer.readString();
        var hostname = buffer.readString();
        var port = buffer.readInt();

        var nodeData = new NodeData(id, hostname, port);

        var group = CloudAPI.instance().groupProvider().fromPacket(name, nodeData, new PlatformVersion(platformVersion, platformProxy), minOnlineServices, memory);

        group.properties().pool().putAll(readProperties(buffer).pool());
        return group;
    }

    public static void writePlayer(@NotNull CloudPlayer cloudPlayer, @NotNull PacketBuffer codecBuffer) {
        codecBuffer.writeUniqueId(cloudPlayer.uniqueId());
        codecBuffer.writeString(cloudPlayer.name());
        codecBuffer.writeString(cloudPlayer.currentServerName());
        codecBuffer.writeString(cloudPlayer.currentProxyName());
    }

    public static CloudPlayer readPlayer(PacketBuffer codecBuffer) {
        return CloudAPI.instance().playerProvider().fromPacket(codecBuffer);
    }
}
