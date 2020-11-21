package user11681.projectfabrok.synthesis.proxy;

import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class ProxyInfo {
    public final String owner;
    public final String name;
    public final String descriptor;
    public final boolean inline;
    public final boolean isStatic;

    public ProxyInfo(final String owner, final String name, final String descriptor, final boolean inline, final boolean isStatic) {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
        this.inline = inline;
        this.isStatic = isStatic;
    }

    public abstract AbstractInsnNode getInstruction();
}
