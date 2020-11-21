package user11681.phormat.asm;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class PhormatTransformer implements IMixinConfigPlugin {
    @Override
    public void onLoad(final String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
        switch (mixinClassName) {
            case "user11681.phormat.asm.mixin.StyleMixin": {
                MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
                final ObjectOpenHashSet<String> methodNames = new ObjectOpenHashSet<>();

                methodNames.add(resolver.mapMethodName("intermediary", "net.minecraft.class_2583", "method_27705", "([Lnet/minecraft/class_124;)Lnet/minecraft/class_2583;"));
                methodNames.add(resolver.mapMethodName("intermediary", "net.minecraft.class_2583", "method_27706", "(Lnet/minecraft/class_124;)Lnet/minecraft/class_2583;"));
                methodNames.add(resolver.mapMethodName("intermediary", "net.minecraft.class_2583", "method_27707", "(Lnet/minecraft/class_124;)Lnet/minecraft/class_2583;"));

                for (final MethodNode method : targetClass.methods) {
                    final String name = method.name;

                    if (methodNames.contains(name)) {
                        AbstractInsnNode instruction = method.instructions.getFirst();

                        while (instruction != null) {
                            MethodInsnNode methodInstruction;

                            if (instruction.getOpcode() == Opcodes.INVOKEVIRTUAL && (methodInstruction = (MethodInsnNode) instruction).name.equals("ordinal")) {
                                method.instructions.set(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC, targetClassName.replace('.', '/'), "phormat_hackOrdinal", "(L" + methodInstruction.owner + ";)I"));

                                break;
                            }

                            instruction = instruction.getNext();
                        }
                    }
                }
            }

            break;

            case "user11681.phormat.asm.mixin.TextColorMixin":
                final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
                final String getRgb = resolver.mapMethodName("intermediary", "net.minecraft.class_5251", "method_27716", "()I");

                for (final MethodNode method : targetClass.methods) {
                    if (getRgb.equals(method.name)) {
                        final String internalName = targetClassName.replace('.', '/');
                        final String rgb = resolver.mapFieldName("intermediary", "net.minecraft.class_5251", "field_24364", "I");

                        final InsnList insertion = new InsnList();
                        final LabelNode endIf = new LabelNode();

                        insertion.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
                        insertion.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this this
                        insertion.add(new FieldInsnNode(Opcodes.GETFIELD, internalName, "phormat_hasColorFunction", "Z")); // this boolean
                        insertion.add(new JumpInsnNode(Opcodes.IFEQ, endIf)); // this
                        insertion.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this this
                        insertion.add(new FieldInsnNode(Opcodes.GETFIELD, internalName, "phormat_colorFunction", "Luser11681/phormat/ColorFunction;")); // this ColorFunction
                        insertion.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this ColorFunction this
                        insertion.add(new FieldInsnNode(Opcodes.GETFIELD, internalName, rgb, "I")); // this ColorFunction int
                        insertion.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "user11681/phormat/ColorFunction", "apply", "(I)I", true)); // this int
                        insertion.add(new InsnNode(Opcodes.DUP_X1)); // int this int
                        insertion.add(new FieldInsnNode(Opcodes.PUTFIELD, internalName, "phormat_previousColor", "I")); // int
                        insertion.add(new InsnNode(Opcodes.IRETURN));
                        insertion.add(endIf);

                        method.instructions.insertBefore(method.instructions.getFirst(), insertion);

                        break;
                    }
                }
        }
    }

    @Override
    public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {}
}
