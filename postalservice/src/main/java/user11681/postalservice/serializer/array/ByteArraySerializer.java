package user11681.postalservice.serializer.array;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class ByteArraySerializer extends Serializer {
    public static final ByteArraySerializer instance = new ByteArraySerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof byte[];
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        byte[] byteArray = (byte[]) object;
        int length = byteArray.length;

        buffer.writeInt(length);

        for (int i = 0; i != length; i++) {
            buffer.writeByte(byteArray[i]);
        }
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        int length = buffer.readInt();
        byte[] array = new byte[length];

        for (int i = 0; i != length; i++) {
            array[i] = buffer.readByte();
        }

        return cacheReadObject(array);
    }
}
