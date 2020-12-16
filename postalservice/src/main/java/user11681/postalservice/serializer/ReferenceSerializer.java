package user11681.postalservice.serializer;

import io.netty.buffer.ByteBuf;

public class ReferenceSerializer extends Serializer {
    public static final ReferenceSerializer instance = new ReferenceSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return writtenObjects.containsKey(object);
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) {
        buffer.writeInt(writtenObjects.getInt(object));
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return readObjects.get(buffer.readInt());
    }
}
