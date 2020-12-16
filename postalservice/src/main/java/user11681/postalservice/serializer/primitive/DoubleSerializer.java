package user11681.postalservice.serializer.primitive;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class DoubleSerializer extends Serializer {
    public static final DoubleSerializer instance = new DoubleSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return klass == double.class;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) {
        buffer.writeDouble((double) object);
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return buffer.readDouble();
    }
}
