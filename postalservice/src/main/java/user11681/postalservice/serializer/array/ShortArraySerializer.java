package user11681.postalservice.serializer.array;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class ShortArraySerializer extends Serializer {
    public static final ShortArraySerializer instance = new ShortArraySerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof short[];
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        short[] array = (short[]) object;
        int length = array.length;

        buffer.writeInt(length);

        for (int i = 0; i != length; i++) {
            buffer.writeShort(array[i]);
        }
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        int length = buffer.readInt();
        short[] array = new short[length];

        for (int i = 0; i != length; i++) {
            array[i] = buffer.readShort();
        }

        return cacheReadObject(array);
    }
}
