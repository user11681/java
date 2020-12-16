package user11681.postalservice.serializer.array;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class FloatArraySerializer extends Serializer {
    public static final FloatArraySerializer instance = new FloatArraySerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof float[];
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        float[] array = (float[]) object;
        int length = array.length;

        buffer.writeInt(length);

        for (int i = 0; i != length; i++) {
            buffer.writeFloat(array[i]);
        }
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        int length = buffer.readInt();
        float[] array = new float[length];

        for (int i = 0; i != length; i++) {
            array[i] = buffer.readFloat();
        }

        return cacheReadObject(array);
    }
}
