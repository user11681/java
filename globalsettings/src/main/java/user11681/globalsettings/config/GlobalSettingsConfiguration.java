package user11681.globalsettings.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.IOUtils;
import user11681.globalsettings.asm.mixin.GameOptionsAccess;

public class GlobalSettingsConfiguration {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final File configConfig;
    private static final File optionConfig;

    public static final GlobalSettingsConfiguration instance = new GlobalSettingsConfiguration();

    private Path configPath;
    private File optionFile;
    private boolean useGlobalConfig;
    private boolean useGlobalOptions;

    public GlobalSettingsConfiguration() {
        try {
            final Field configDir = net.fabricmc.loader.FabricLoader.class.getDeclaredField("configDir");
            configDir.setAccessible(true);

            final File configConfigFile = FabricLoader.getInstance().getConfigDir().resolve("globalsettings.json").toFile();

            if (configConfigFile.exists()) {
                try {
                    final JsonObject json = (JsonObject) new JsonParser().parse(new String(IOUtils.toByteArray(configConfigFile.toURI())));

                    this.useGlobalConfig = json.get("useGlobalConfig").getAsBoolean();
                    this.useGlobalOptions = json.get("useGlobalOptions").getAsBoolean();
                } catch (final Throwable throwable) {
                    this.write();
                }
            }

            final String home = System.getProperty("user.home");

            if (optionConfig.exists()) {
                this.optionFile = new File(new String(IOUtils.toByteArray(optionConfig.toURI())));
            } else {
                this.setOptionPath(new File(home, ".config/minecraft/globalsettings/options.txt"));
            }

            this.ensureFileExists();
        } catch (final Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public File getOptionFile() {
        return this.optionFile;
    }

    public Path getConfigPath() {
        return this.configPath;
    }

    public void setOptionPath(final String path) {
        setOptionPath(new File(path));
    }

    public void setOptionPath(final File path) {
        this.optionFile = path;

        write();

        ((GameOptionsAccess) MinecraftClient.getInstance().options).setOptionFile(this.optionFile);
    }

    public void setConfigPath(final String path) {
        setConfigPath(FileSystems.getDefault().getPath(path));
    }

    public void setConfigPath(final Path path) {
        this.configPath = path;

        write();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void ensureFileExists() {
        this.optionFile.getParentFile().mkdirs();

        try {
            this.optionFile.createNewFile();
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void write() {
        try {
            IOUtils.write(gson.toJson(this).getBytes(StandardCharsets.UTF_8), new FileOutputStream(optionConfig));
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    static {
        optionConfig = FabricLoader.getInstance().getConfigDir().resolve("globalsettings/options.txt").toFile();

        final File homeConfigFile;

        if (configConfigFile.exists()) {
            homeConfigFile = new File(new String(IOUtils.toByteArray(configConfigFile.toURI())));
            configConfig = configConfigFile;

            configDir.set(FabricLoader.getInstance(), homeConfigFile);
        } else {
            homeConfigFile = new File(home, ".config/minecraft/globalsettings.json");
        }
    }
}
