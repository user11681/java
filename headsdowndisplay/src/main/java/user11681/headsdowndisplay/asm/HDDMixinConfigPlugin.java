package user11681.headsdowndisplay.asm;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import java.util.List;
import java.util.Set;
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

public class HDDMixinConfigPlugin implements IMixinConfigPlugin {
    public static Float2FloatFunction quadAlphaRedirector;
    public static boolean applyRedirector;

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
    public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {}

    @Override
    public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
        if (targetClassName.equals("net.minecraft.client.render.VertexConsumer")) {
            final MethodNode[] methods = targetClass.methods.toArray(new MethodNode[0]);
            final int methodCount = methods.length;

            for (int i = 0; i < methodCount; i++) {
                if (methods[i].name.equals("quad") && methods[i].desc.equals("(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;[FFFF[IIZ)V")) {
                    final InsnList instructions = methods[i].instructions;
                    AbstractInsnNode instruction = instructions.getFirst();

                    while (instruction != null) {
                        AbstractInsnNode previous;

                        if (instruction.getOpcode() == Opcodes.FCONST_1
                            && (previous = instruction.getPrevious()).getOpcode() == Opcodes.FLOAD
                            && ((VarInsnNode) previous).var == 26) {
                            final String thisClassName = "user11681/headsdowndisplay/asm/HDDMixinConfigPlugin";
                            final InsnList insertion = new InsnList();
                            final LabelNode applyLambda = new LabelNode();
                            final LabelNode end = new LabelNode();

                            insertion.add(new FieldInsnNode(Opcodes.GETSTATIC, thisClassName, "applyRedirector", "Z"));
                            insertion.add(new JumpInsnNode(Opcodes.IFNE, applyLambda));
                            insertion.add(new InsnNode(Opcodes.FCONST_1));
                            insertion.add(new JumpInsnNode(Opcodes.GOTO, end));
                            insertion.add(applyLambda);
                            insertion.add(new FieldInsnNode(Opcodes.GETSTATIC, thisClassName, "quadAlphaRedirector", "Lit/unimi/dsi/fastutil/floats/Float2FloatFunction;"));
                            insertion.add(new InsnNode(Opcodes.FCONST_1));
                            insertion.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "it/unimi/dsi/fastutil/floats/Float2FloatFunction", "get", "(F)F", true));
                            insertion.add(end);

                            instructions.insert(instruction, insertion);
                            instructions.remove(instruction);

                            return;
                        }

                        instruction = instruction.getNext();
                    }
                }
            }
        }
    }
}
