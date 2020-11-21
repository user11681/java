package user11681.projectfabrok.synthesis.accessor;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import user11681.projectfabrok.annotation.ChainType;
import user11681.shortcode.Shortcode;
import user11681.shortcode.instruction.DelegatingInsnList;

public class SetterInfo extends AccessorInfo {
    protected final ChainType chainType;

    public SetterInfo(final MethodNode method, final int access, final String fieldName, final String fieldDescriptor, final ChainType chainType) {
        super(method, access, fieldName, fieldDescriptor);

        this.chainType = chainType;
    }

    @Override
    public MethodNode toNode(final String owner) {
        final MethodNode method = new MethodNode(this.access, this.name, this.descriptor, this.signature, this.exceptions);
        final String returnType = Shortcode.parseDescriptor(method).top();

        method.visitVarInsn(Opcodes.ALOAD, 0);
        method.visitVarInsn(Shortcode.getLoadOpcode(this.fieldDescriptor), 1);
        method.visitFieldInsn(Opcodes.PUTFIELD, owner, this.fieldName, this.fieldDescriptor);

        if (this.chainType.enabled) {
            method.visitVarInsn(Opcodes.ALOAD, 0);
        }

        method.visitInsn(Shortcode.getReturnOpcode(returnType));

        return method;
    }

    @Override
    public void accept(final MethodNode method, final String owner) {
        final DelegatingInsnList instructions = new DelegatingInsnList();

        instructions.addVarInsn(Opcodes.ALOAD, 0);
        instructions.addVarInsn(Shortcode.getLoadOpcode(this.fieldDescriptor), 1);
        instructions.addFieldInsn(Opcodes.PUTFIELD, owner, this.fieldName, this.fieldDescriptor);

        if (this.chainType == ChainType.FORCED) {
            instructions.addVarInsn(Opcodes.ALOAD, 0);
        }

        Shortcode.insertBeforeEveryReturn(method, instructions);
    }
}
