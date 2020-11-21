package user11681.projectfabrok.synthesis.accessor;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import user11681.shortcode.Shortcode;
import user11681.shortcode.instruction.DelegatingInsnList;

public class GetterInfo extends AccessorInfo {
    public GetterInfo(final MethodNode method, final int access, final String fieldName, final String fieldDescriptor) {
        super(method, access, fieldName, fieldDescriptor);
    }

    @Override
    public MethodNode toNode(final String owner) {
        final MethodNode method = new MethodNode(this.access, this.name, this.descriptor, this.signature, this.exceptions);

        method.visitVarInsn(Opcodes.ALOAD, 0);
        method.visitFieldInsn(Opcodes.GETFIELD, owner, this.fieldName, this.fieldDescriptor);
        method.visitInsn(Shortcode.getReturnOpcode(this.fieldDescriptor));

        return method;
    }

    @Override
    public void accept(final MethodNode method, final String owner) {
        final DelegatingInsnList instructions = new DelegatingInsnList();

        instructions.addVarInsn(Opcodes.ALOAD, 0);
        instructions.addFieldInsn(Opcodes.GETFIELD, owner, this.fieldName, this.fieldDescriptor);

        Shortcode.insertBeforeEveryReturn(method, instructions);
    }
}
