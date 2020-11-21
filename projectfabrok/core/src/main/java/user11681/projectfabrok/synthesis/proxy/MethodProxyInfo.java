package user11681.projectfabrok.synthesis.proxy;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MethodProxyInfo extends ProxyInfo {
    public final boolean isInterface;

    public MethodProxyInfo(final String owner, final String name, final String descriptor, final boolean inline, final boolean isStatic, final boolean isInterface) {
        super(owner, name, descriptor, inline, isStatic);

        this.isInterface = isInterface;
    }

    @Override
    public AbstractInsnNode getInstruction() {
        return new MethodInsnNode(this.isStatic ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL, this.owner, this.name, this.descriptor, this.isInterface);
    }
}
