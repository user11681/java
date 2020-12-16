package user11681.postalservice.serializer.primitive;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class ShortSerializer extends Serializer {
    public static final ShortSerializer instance = new ShortSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return klass == short.class;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) {
        buffer.writeShort((short) object);
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return buffer.readShort();
    }
}
