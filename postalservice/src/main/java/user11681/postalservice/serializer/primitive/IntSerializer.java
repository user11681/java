package user11681.postalservice.serializer.primitive;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class IntSerializer extends Serializer {
    public static final IntSerializer instance = new IntSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return klass == int.class;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) {
        buffer.writeInt((int) object);
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return buffer.readInt();
    }
}
