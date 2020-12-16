package user11681.postalservice.serializer;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.gudenau.lib.unsafe.Unsafe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user11681.postalservice.FieldEntry;
import user11681.postalservice.ThePostalService;
import user11681.reflect.Fields;

public abstract class Serializer {
    public static final Int2ReferenceMap<Object> readObjects = new Int2ReferenceOpenHashMap<>();
    public static final Reference2IntMap<Object> writtenObjects = new Reference2IntOpenHashMap<>();

    protected static final Charset charset = StandardCharsets.UTF_8;
    protected static final Reference2ReferenceMap<Class<?>, ObjectOpenHashSet<FieldEntry>> fieldCache = new Reference2ReferenceOpenHashMap<>();

    protected static final Logger logger = LogManager.getLogger(ThePostalService.class);

    private static byte nextIdentifier;

    public final byte identifier;


    protected Serializer() {
        this.identifier = nextIdentifier++;
    }

    public abstract boolean canSerialize(Object object, Class<?> klass);

    public abstract void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable;

    public abstract Object deserialize(ByteBuf buffer) throws Throwable;

    protected static String readString(ByteBuf buffer) {
        return buffer.readCharSequence(buffer.readInt(), charset).toString();
    }

    protected static String writeString(ByteBuf buffer, String string) {
        buffer.writeInt(string.length());
        buffer.writeCharSequence(string, charset);

        return string;
    }

    protected static void cacheWrittenObject(Object object) {
        writtenObjects.put(object, writtenObjects.size());
    }

    protected static <T> T cacheReadObject(T object) {
        readObjects.put(readObjects.size(), object);

        return object;
    }

    protected static void cacheFields(Class<?> klass) {
        try {
            while (klass != Object.class) {
                ObjectOpenHashSet<FieldEntry> cachedFields = new ObjectOpenHashSet<>();

                for (Field field : Fields.getInstanceFields(klass)) {
                    cachedFields.add(new FieldEntry(field));
                }

                fieldCache.put(klass, cachedFields);
                klass = klass.getSuperclass();
            }
        } catch (final Throwable throwable) {
            throw Unsafe.throwException(throwable);
        }
    }

    protected void writeIdentifier(ByteBuf buffer, Object object, Class<?> klass) {
        buffer.writeByte(this.identifier);
    }

    public static <T> T readObject(final ByteBuf buffer) {
        try {
            int identifier = buffer.readByte();

            for (Serializer serializer : ThePostalService.serializers) {
                if (serializer.identifier == identifier) {
                    return (T) serializer.deserialize(buffer);
                }
            }
        } catch (Throwable throwable) {
            logger.error("An error was encountered while attempting to read an object from a buffer: ", throwable);
        }

        return null;
    }

    public static void writeObject(ByteBuf buffer, Object object, Class<?> klass) {
        try {
            for (Serializer serializer : ThePostalService.serializers) {
                if (serializer.canSerialize(object, klass)) {
                    serializer.writeIdentifier(buffer, object, klass);
                    serializer.serialize(buffer, object, klass);

                    return;
                }
            }
        } catch (Throwable throwable) {
            logger.error("An error occurred while attempting to write an object to a buffer: ", throwable);
        }
    }
}
