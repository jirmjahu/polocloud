package dev.httpmarco.polocloud.node.groups;

import com.google.gson.*;
import dev.httpmarco.polocloud.api.groups.ClusterGroup;
import dev.httpmarco.polocloud.api.groups.FallbackClusterGroup;
import dev.httpmarco.polocloud.api.platforms.PlatformType;
import dev.httpmarco.polocloud.api.properties.PropertiesPool;
import dev.httpmarco.polocloud.node.platforms.PlatformVersion;
import dev.httpmarco.polocloud.node.platforms.PlatformVersionTypeAdapter;
import dev.httpmarco.polocloud.node.properties.PropertiesPoolSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public final class ClusterGroupTypeAdapter implements JsonDeserializer<ClusterGroup>, JsonSerializer<FallbackClusterGroup> {

    private static final Gson SILENT_GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().registerTypeAdapter(PlatformVersion.class, new PlatformVersionTypeAdapter()).registerTypeAdapter(PropertiesPool.class, new PropertiesPoolSerializer()).create();

    @Override
    public ClusterGroup deserialize(JsonElement json, Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
        var object = (JsonObject) json;
        var group = (ClusterGroup) context.deserialize(object, ClusterGroupImpl.class);

        if (object.has("fallback") && object.get("fallback").getAsBoolean()) {
            return new ClusterGroupFallbackImpl(group);
        }
        return group;
    }

    @Override
    public @Nullable JsonElement serialize(FallbackClusterGroup src, Type typeOfSrc, @NotNull JsonSerializationContext context) {
        var group = (JsonObject) SILENT_GSON.toJsonTree(src);

        if (src.platform().type() == PlatformType.SERVER) {
            group.addProperty("fallback", true);
        }

        return group;
    }
}
