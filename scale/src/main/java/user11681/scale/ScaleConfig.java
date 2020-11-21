package user11681.scale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public class ScaleConfig {
    public static final ScaleConfig INSTANCE = new ScaleConfig("scale.json");

    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private static final JsonParser PARSER = new JsonParser();

    private final File file;

    @Expose
    public boolean enabled;

    public ScaleConfig(final String path) {
        this.file = new File(FabricLoader.getInstance().getConfigDir().toFile(), path);
        this.enabled = true;
    }

    public void write() throws Throwable {
        ((OutputStream) new FileOutputStream(this.file)).write(GSON.toJson(this).getBytes());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void read() throws Throwable {
        if (this.file.createNewFile()) {
            this.write();
        } else {
            final InputStream input = new FileInputStream(this.file);
            final byte[] content = new byte[input.available()];

            while (input.read(content) > -1);

            final JsonObject element = (JsonObject) PARSER.parse(new String(content));

            this.enabled = element.get("enabled").getAsBoolean();
        }
    }
}
