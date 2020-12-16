package user11681.postalservice.serializer.primitive;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class BooleanSerializer extends Serializer {
    public static final BooleanSerializer instance = new BooleanSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return klass == boolean.class;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) {
        buffer.writeBoolean((boolean) object);
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return buffer.readBoolean();
    }
}
