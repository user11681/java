package user11681.postalservice.serializer.array;

import io.netty.buffer.ByteBuf;
import user11681.postalservice.serializer.Serializer;

public class CharArraySerializer extends Serializer {
    public static final CharArraySerializer instance = new CharArraySerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof char[];
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        char[] array = (char[]) object;
        int length = array.length;

        buffer.writeInt(length);

        for (int i = 0; i != length; i++) {
            buffer.writeChar(array[i]);
        }
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        int length = buffer.readInt();
        char[] array = new char[length];

        for (int i = 0; i != length; i++) {
            array[i] = buffer.readChar();
        }

        return cacheReadObject(array);
    }
}
