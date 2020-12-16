package user11681.postalservice.serializer.primitive;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class LongSerializer extends Serializer {
    public static final LongSerializer instance = new LongSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return klass == long.class;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) {
        buffer.writeLong((int) object);
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return buffer.readLong();
    }
}
