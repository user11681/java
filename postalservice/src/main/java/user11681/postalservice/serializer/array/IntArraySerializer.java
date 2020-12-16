package user11681.postalservice.serializer.array;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class IntArraySerializer extends Serializer {
    public static final IntArraySerializer instance = new IntArraySerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof int[];
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        if (object instanceof int[]) {
            int[] array = (int[]) object;
            int length = array.length;

            buffer.writeInt(length);

            for (int i = 0; i != length; i++) {
                buffer.writeInt(array[i]);
            }
        }
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        int length = buffer.readInt();
        int[] array = new int[length];

        for (int i = 0; i != length; i++) {
            array[i] = buffer.readInt();
        }

        return cacheReadObject(array);
    }
}
