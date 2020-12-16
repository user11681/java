package user11681.postalservice.serializer;

import io.netty.buffer.ByteBuf;

public class EnumSerializer extends Serializer {
    public static final EnumSerializer instance = new EnumSerializer();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        return object instanceof Enum;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        writeString(buffer, klass.getName());

        buffer.writeInt(((Enum<?>) object).ordinal());
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        Class<?> enumeration = Class.forName(readString(buffer));

        if (enumeration.isAnonymousClass()) {
            enumeration = enumeration.getSuperclass();
        }

        Object enumConstant = enumeration.getEnumConstants()[buffer.readInt()];

        return cacheReadObject(enumConstant);
    }
}
