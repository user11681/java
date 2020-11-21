package user11681.projectfabrok.synthesis.proxy;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class FieldProxyInfo extends ProxyInfo {
    public FieldProxyInfo(final String owner, final String name, final String decriptor, final boolean inline, final boolean isStatic) {
        super(owner, name, decriptor, inline, isStatic);
    }

    @Override
    public AbstractInsnNode getInstruction() {
        return this.inline
            ? new FieldInsnNode(this.isStatic ? Opcodes.GETSTATIC : Opcodes.GETFIELD, this.owner, this.name, this.descriptor)
            : new MethodInsnNode(this.isStatic ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL, this.owner, this.name, this.descriptor);
    }
}
