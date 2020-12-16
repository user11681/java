package user11681.postalservice.serializer;

import io.netty.buffer.ByteBuf;

public class StringSerializer extends Serializer {
    public static final StringSerializer instance = new StringSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof String;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        cacheWrittenObject(writeString(buffer, (String) object));
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return cacheReadObject(readString(buffer));
    }
}
