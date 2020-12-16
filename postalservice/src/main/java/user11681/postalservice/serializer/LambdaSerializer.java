package user11681.postalservice.serializer;

import java.lang.instrument.Instrumentation;

import io.netty.buffer.ByteBuf;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;

public class LambdaSerializer extends Serializer {
    public static final LambdaSerializer instance = new LambdaSerializer();

    private static final Instrumentation instrumentation = ByteBuddyAgent.install();

    @Override
    public boolean canSerialize(Object object, Class<?> klass) {
        try {
            Class.forName(klass.getName());
        } catch (ClassNotFoundException exception) {
            return true;
        }

        return false;
    }

    @Override
    public void serialize(ByteBuf buffer, Object object, Class<?> klass) throws Throwable {
        writeString(buffer, klass.getName());

        byte[] bytecode = ClassFileLocator.AgentBased.of(instrumentation, klass).locate(new TypeDescription.ForLoadedType(klass).getName()).resolve();

        buffer.writeInt(bytecode.length);
        buffer.writeBytes(bytecode);
    }

    @Override
    public Object deserialize(ByteBuf buffer) throws Throwable {
        String className = readString(buffer);
        ByteBuf bytecode = buffer.readBytes(buffer.readInt());

        return null;
    }
}
