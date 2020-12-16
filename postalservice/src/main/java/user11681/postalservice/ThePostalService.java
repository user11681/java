package user11681.postalservice;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceList;
import net.minecraft.network.PacketByteBuf;
import user11681.postalservice.serializer.primitive.BooleanSerializer;
import user11681.postalservice.serializer.primitive.ByteSerializer;
import user11681.postalservice.serializer.primitive.CharSerializer;
import user11681.postalservice.serializer.ClassSerializer;
import user11681.postalservice.serializer.primitive.DoubleSerializer;
import user11681.postalservice.serializer.EnumSerializer;
import user11681.postalservice.serializer.primitive.FloatSerializer;
import user11681.postalservice.serializer.primitive.IntSerializer;
import user11681.postalservice.serializer.LambdaSerializer;
import user11681.postalservice.serializer.primitive.LongSerializer;
import user11681.postalservice.serializer.NullSerializer;
import user11681.postalservice.serializer.ObjectSerializer;
import user11681.postalservice.serializer.ReferenceSerializer;
import user11681.postalservice.serializer.Serializer;
import user11681.postalservice.serializer.primitive.ShortSerializer;
import user11681.postalservice.serializer.StringSerializer;
import user11681.postalservice.serializer.array.BooleanArraySerializer;
import user11681.postalservice.serializer.array.ByteArraySerializer;
import user11681.postalservice.serializer.array.CharArraySerializer;
import user11681.postalservice.serializer.array.DoubleArraySerializer;
import user11681.postalservice.serializer.array.FloatArraySerializer;
import user11681.postalservice.serializer.array.IntArraySerializer;
import user11681.postalservice.serializer.array.LongArraySerializer;
import user11681.postalservice.serializer.array.ObjectArraySerializer;
import user11681.postalservice.serializer.array.ShortArraySerializer;
import user11681.postalservice.test.TestPacket;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class ThePostalService {
    public static final ReferenceList<Serializer> serializers = ReferenceArrayList.wrap(new Serializer[]{
        NullSerializer.instance,
        BooleanSerializer.instance,
        ByteSerializer.instance,
        CharSerializer.instance,
        ShortSerializer.instance,
        IntSerializer.instance,
        LongSerializer.instance,
        FloatSerializer.instance,
        DoubleSerializer.instance,
        ReferenceSerializer.instance,
        StringSerializer.instance,
        EnumSerializer.instance,
        LambdaSerializer.instance,
        ClassSerializer.instance,
        BooleanArraySerializer.instance,
        ByteArraySerializer.instance,
        CharArraySerializer.instance,
        ShortArraySerializer.instance,
        IntArraySerializer.instance,
        LongArraySerializer.instance,
        FloatArraySerializer.instance,
        DoubleArraySerializer.instance,
        ObjectArraySerializer.instance,
        ObjectSerializer.instance
    });

    public static synchronized <T> T readObject(final ByteBuf buffer) {
        final T object = Serializer.readObject(buffer);

        Serializer.readObjects.clear();

        return object;
    }

    public static PacketByteBuf writeObject(final Object object) {
        return writeObject(new PacketByteBuf(Unpooled.buffer()), object);
    }

    public static synchronized <T extends ByteBuf> T writeObject(final T buffer, final Object object) {
        Serializer.writeObject(buffer, object, object == null ? null : object.getClass());

        Serializer.writtenObjects.clear();

        return buffer;
    }

    static {
        ServerSidePacketRegistry.INSTANCE.register(TestPacket.identifier, TestPacket.instance);
        ClientSidePacketRegistry.INSTANCE.register(TestPacket.identifier, TestPacket.instance);
    }
}
