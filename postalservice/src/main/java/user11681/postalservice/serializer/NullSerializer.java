package user11681.postalservice.serializer;

import io.netty.buffer.ByteBuf;

public class NullSerializer extends Serializer {
    public static final NullSerializer instance = new NullSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object == null;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) {}

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return null;
    }
}
