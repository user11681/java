package user11681.postalservice.serializer.array;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class LongArraySerializer extends Serializer {
    public static final LongArraySerializer instance = new LongArraySerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof long[];
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        if (object instanceof long[]) {
            long[] array = (long[]) object;
            int length = array.length;

            buffer.writeInt(length);

            for (int i = 0; i != length; i++) {
                buffer.writeLong(array[i]);
            }
        }
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        int length = buffer.readInt();
        long[] array = new long[length];

        for (int i = 0; i != length; i++) {
            array[i] = buffer.readLong();
        }

        return cacheReadObject(array);
    }
}
