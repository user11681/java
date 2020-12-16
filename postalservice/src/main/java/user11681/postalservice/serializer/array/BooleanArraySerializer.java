package user11681.postalservice.serializer.array;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class BooleanArraySerializer extends Serializer {
    public static final BooleanArraySerializer instance = new BooleanArraySerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof boolean[];
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        boolean[] array = (boolean[]) object;
        int length = array.length;

        buffer.writeInt(length);

        for (int i = 0; i != length; i++) {
            buffer.writeBoolean(array[i]);
        }
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        int length = buffer.readInt();
        boolean[] array = new boolean[length];

        for (int i = 0; i != length; i++) {
            array[i] = buffer.readBoolean();
        }

        return cacheReadObject(array);
    }
}
