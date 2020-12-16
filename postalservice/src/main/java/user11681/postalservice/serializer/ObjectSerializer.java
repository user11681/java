package user11681.postalservice.serializer;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.gudenau.lib.unsafe.Unsafe;
import user11681.postalservice.FieldEntry;

public class ObjectSerializer extends Serializer {
    public static final ObjectSerializer instance = new ObjectSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return !klass.isPrimitive();
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        cacheWrittenObject(object);

        try {
            writeString(buffer, klass.getName());

            ObjectOpenHashSet<FieldEntry> classFields = fieldCache.get(klass);

            if (classFields == null) {
                cacheFields(klass);
            }

            while (klass != Object.class) {
                classFields = fieldCache.get(klass);
                int fieldCount = classFields.size();

                buffer.writeInt(fieldCount);

                for (final FieldEntry field : classFields) {
                    Class<?> fieldType = field.type;
                    String fieldName = field.name;

                    writeString(buffer, fieldName);

                    Object fieldValue = Unsafe.getObject(object, field.offset);

                    writeObject(buffer, fieldValue, fieldType.isPrimitive() ? fieldType : fieldValue == null ? null : fieldValue.getClass());
                }

                klass = klass.getSuperclass();
            }
        } catch (final Throwable throwable) {
            logger.error("An error was encountered while attempting to write an object to a buffer: ", throwable);
        }
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        Class<?> klass = Class.forName(readString(buffer));
        Object object = cacheReadObject(Unsafe.allocateInstance(klass));
        ObjectOpenHashSet<FieldEntry> fields = fieldCache.get(klass);

        if (fields == null) {
            cacheFields(klass);
        }

        while (klass != Object.class) {
            fields = fieldCache.get(klass);
            int fieldCount = buffer.readInt();

            for (int i = 0; i != fieldCount; i++) {
                FieldEntry entry = fields.get(readString(buffer));

                if (entry != null) {
                    Unsafe.putObject(object, entry.offset, readObject(buffer));
                }
            }

            klass = klass.getSuperclass();
        }

        return object;
    }
}
