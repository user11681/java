package user11681.postalservice.serializer.array;

import java.lang.reflect.Array;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class ObjectArraySerializer extends Serializer {
    public static final ObjectArraySerializer instance = new ObjectArraySerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof Object[];
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        Object[] array = (Object[]) object;
        int length = array.length;

        buffer.writeInt(length);

        String className = klass.getComponentType().getName();

        writeString(buffer, className);

        for (int i = 0; i != length; i++) {
            Object element = array[i];

            writeObject(buffer, element, element == null ? null : element.getClass());
        }
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        int length = buffer.readInt();

        Object[] array = (Object[]) Array.newInstance(Class.forName(readString(buffer)), length);

        for (int i = 0; i != length; i++) {
            array[i] = readObject(buffer);
        }

        return cacheReadObject(array);
    }
}
