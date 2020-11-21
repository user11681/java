package user11681.projectfabrok.synthesis.accessor;

import org.objectweb.asm.tree.MethodNode;

public abstract class AccessorInfo {
    public final String name;
    public final String descriptor;
    public final String signature;
    public final String[] exceptions;

    public final int access;
    public final String fieldName;
    public final String fieldDescriptor;

    public AccessorInfo(final MethodNode method, final int access, final String fieldName, final String fieldDescriptor) {
        this.name = method.name;
        this.descriptor = method.desc;
        this.signature = method.signature;
        this.exceptions = method.exceptions.toArray(new String[0]);
        this.access = access;
        this.fieldName = fieldName;
        this.fieldDescriptor = fieldDescriptor;
    }

    public abstract MethodNode toNode(String owner);

    public abstract void accept(MethodNode method, String owner);
}
