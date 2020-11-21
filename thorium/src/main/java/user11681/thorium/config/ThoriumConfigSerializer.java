package user11681.thorium.config;

import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;

public class ThoriumConfigSerializer extends Toml4jConfigSerializer<ThoriumConfiguration> {
    public ThoriumConfigSerializer(final Config definition, final Class<ThoriumConfiguration> configClass) {
        super(definition, configClass);
    }

    @Override
    public void serialize(final ThoriumConfiguration configuration) throws SerializationException {
        super.serialize(configuration);

        ThoriumConfiguration.Data.set(configuration);
    }
}
