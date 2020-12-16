package user11681.postalservice.serializer;

import io.netty.buffer.ByteBuf;

public class ClassSerializer extends Serializer {
    public static final ClassSerializer instance = new ClassSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return klass == Class.class;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        writeString(buffer,((Class<?>) object).getName());
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return Class.forName(readString(buffer));
    }
}
