package user11681.postalservice.serializer.primitive;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class CharSerializer extends Serializer {
    public static final CharSerializer instance = new CharSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return klass == char.class;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) {
        buffer.writeChar((char) object);
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        return buffer.readChar();
    }
}
