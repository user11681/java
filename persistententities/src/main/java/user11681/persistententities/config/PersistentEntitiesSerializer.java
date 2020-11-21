package user11681.persistententities.config;

import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;

public class PersistentEntitiesSerializer extends Toml4jConfigSerializer<PersistentEntitiesConfiguration> {
    public PersistentEntitiesSerializer(final Config definition, final Class<PersistentEntitiesConfiguration> configClass) {
        super(definition, configClass);
    }

    @Override
    public void serialize(final PersistentEntitiesConfiguration config) throws SerializationException {
        super.serialize(config);

        PersistentEntitiesConfiguration.Data.set(config);
    }
}
