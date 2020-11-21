package net.fabricmc.loader.metadata;

import it.unimi.dsi.fastutil.objects.Object2ReferenceArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.List;
import java.util.Map;
import net.fabricmc.loader.ModContainer;
import net.gudenau.lib.unsafe.Unsafe;
import user11681.projectfabrok.synthesis.FabrokTransformer;
import user11681.reflect.Accessor;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class MetadataGenerator {
    public static void addEntrypoint(final String modID, final String entrypoint, final String adapter, final String target) {
        try {
            final LoaderModMetadata modMetadata = ((ModContainer) FabrokTransformer.fabric.getModContainer(modID).get()).getInfo();

            List<EntrypointMetadata> entrypoints = modMetadata.getEntrypoints(entrypoint);

            if (entrypoints == null) {
                entrypoints = ReferenceArrayList.wrap(new EntrypointMetadata[1], 0);

                Map<String, List<EntrypointMetadata>> entrypointMap = Accessor.getObject(modMetadata, "entrypoints");

                if (!(entrypointMap instanceof Object2ReferenceOpenHashMap)) {
                    entrypointMap = new Object2ReferenceArrayMap<>(entrypointMap);

                    Accessor.putObject(modMetadata, "entrypoints", entrypointMap);
                }

                entrypointMap.put(entrypoint, entrypoints);
            } else {
                for (final EntrypointMetadata entrypointMetadata : entrypoints) {
                    if (entrypointMetadata.getValue().equals(target)) {
                        return;
                    }
                }
            }

            final EntrypointMetadata entrypointMetadata = new V1ModMetadata.EntrypointMetadataImpl(adapter, target);

            entrypoints.add(entrypointMetadata);
        } catch (final Throwable throwable) {
            throw Unsafe.throwException(throwable);
        }
    }
}
