package user11681.postalservice.serializer.primitive;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class FloatSerializer extends Serializer {
    public static final FloatSerializer instance = new FloatSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return klass == float.class;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) {
        buffer.writeFloat((float) object);
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return buffer.readFloat();
    }
}
