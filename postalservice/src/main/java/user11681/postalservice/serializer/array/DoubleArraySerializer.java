package user11681.postalservice.serializer.array;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class DoubleArraySerializer extends Serializer {
    public static final DoubleArraySerializer instance = new DoubleArraySerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof double[];
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        double[] array = (double[]) object;
        int length = array.length;

        buffer.writeInt(length);

        for (int i = 0; i != length; i++) {
            buffer.writeDouble(array[i]);
        }
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        int length = buffer.readInt();
        double[] array = new double[length];

        for (int i = 0; i != length; i++) {
            array[i] = buffer.readDouble();
        }

        return cacheReadObject(array);
    }
}
