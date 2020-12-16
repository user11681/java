package user11681.postalservice.serializer.primitive;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class ByteSerializer extends Serializer {
    public static final ByteSerializer instance = new ByteSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return klass == byte.class;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) {
        buffer.writeByte((byte) object);
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return buffer.readByte();
    }
}
