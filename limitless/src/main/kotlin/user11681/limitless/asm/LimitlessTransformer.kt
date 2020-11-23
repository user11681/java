package user11681.limitless.asm

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import net.devtech.grossfabrichacks.transformer.TransformerApi
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import user11681.fabricasmtools.plugin.TransformerPlugin
import user11681.limitless.config.LimitlessConfiguration
import user11681.limitless.config.enchantment.EnchantmentConfiguration
import user11681.limitless.enchanting.EnchantmentUtil
import user11681.shortcode.Shortcode
import user11681.shortcode.instruction.DelegatingInsnList
import java.util.function.Predicate

class LimitlessTransformer : TransformerPlugin(), Opcodes {
    override fun onLoad(mixinPackage: String) {
        super.onLoad(mixinPackage)

        putClass("World", 1937)
        putClass("BlockPos", 2338)
        putField("creativeMode", 7477)
        putMethod("create", 7246)
        putMethod("calculateRequiredExperienceLevel", 8227)
        putMethod("getPossibleEntries", 8229)
        putMethod("generateEnchantments", 8230)

        this.registerPostMixinMethodTransformer(klass(3853, 1648), this.method("create"), null, LimitlessTransformer::transformEnchantBookFactory)
        this.registerPostMixinMethodTransformer(klass(1890), this.method("getPossibleEntries"), null, LimitlessTransformer::transformEnchantmentHelperGetPossibleEntries)
        this.registerPostMixinMethodTransformer(klass(1890), this.method("generateEnchantments"), null, LimitlessTransformer::transformEnchantmentHelperGenerateEnchantments)
        this.registerPostMixinMethodTransformer(klass(1718), "method_17411", null, this::transformEnchantmentScreenHandler)
    }

    private fun transformEnchantmentScreenHandler(method: MethodNode) {
        val instructions = method.instructions
        val iterator = instructions.iterator()

        Shortcode.findForward(iterator,
                {instruction: AbstractInsnNode -> instruction.opcode == Opcodes.ICONST_0},
                {instruction: AbstractInsnNode ->
                    (instruction.next as VarInsnNode).opcode = Opcodes.FSTORE
                    iterator.set(MethodInsnNode(Opcodes.INVOKESTATIC, "user11681/limitless/enchantment/EnchantingBlocks", "countEnchantingPower", Shortcode.composeMethodDescriptor("F", this.klass("World"), this.klass("BlockPos")), true))
                    iterator.previous()
                    iterator.add(VarInsnNode(Opcodes.ALOAD, 2))
                    iterator.add(VarInsnNode(Opcodes.ALOAD, 3))
                }
        )

        iterator.next()
        iterator.next()

        var gotoCount = 0

        while (gotoCount != 3) {
            val next = iterator.next()

            if (next.opcode == Opcodes.GOTO) {
                ++gotoCount
            }

            iterator.remove()
        }

        Shortcode.findForward(iterator,
                {instruction: AbstractInsnNode -> instruction.opcode == Opcodes.ILOAD && (instruction as VarInsnNode).`var` == 4},
                {instruction: AbstractInsnNode -> (instruction as VarInsnNode).opcode = Opcodes.FLOAD}
        )

        Shortcode.findForward(iterator,
                {instruction: AbstractInsnNode -> instruction.opcode == Opcodes.INVOKESTATIC},
                {instruction: AbstractInsnNode ->
                    val methodInstruction = instruction as MethodInsnNode
                    methodInstruction.owner = "user11681/limitless/enchantment/EnchantingBlocks"
                    methodInstruction.name = "calculateRequiredExperienceLevel"
                    methodInstruction.desc = methodInstruction.desc.replaceFirst("II".toRegex(), "IF")
                    methodInstruction.itf = true
                }
        )
    }

    companion object {
        private const val getIntDescriptor = "(Ljava/lang/String;)I"
        private const val putIntDescriptor = "(Ljava/lang/String;I)V"
        private const val limitless_getOriginalMaxLevel = "limitless_getOriginalMaxLevel"
        private const val limitless_maxLevel = "limitless_maxLevel"

        private val Enchantment = internal(1887)
        private val CompoundTag = internal(2487)
        private val getMaxLevel = method(8183)
        private val getMaxPower = method(20742)
        private val getByte = method(10571)
        private val getShort = method(10568)
        private val getInt = method(10550)
        private val putByte = method(10567)
        private val putShort = method(10575)
        private val putInt = method(10569)

        private val enchantmentClassNames = ObjectOpenHashSet(arrayOf(Enchantment))

        private fun transformEnchantBookFactory(method: MethodNode) {
            var instruction = method.instructions.first

            while (instruction != null) {
                if (instruction.opcode == Opcodes.INVOKEVIRTUAL && (instruction as MethodInsnNode).name == getMaxLevel) {
                    instruction.name = limitless_getOriginalMaxLevel
                }

                instruction = instruction.next
            }
        }

        private fun transformEnchantmentHelperGetPossibleEntries(method: MethodNode) {
            val instructions = method.instructions
            val iterator = instructions.iterator()

            Shortcode.findForward(iterator,
                    Predicate {instruction: AbstractInsnNode -> instruction.opcode == Opcodes.INVOKEVIRTUAL && (instruction as MethodInsnNode).name == getMaxLevel},
                    Runnable {
                        Shortcode.removeBetweenInclusive(iterator, AbstractInsnNode.LINE, AbstractInsnNode.IINC_INSN)
                        iterator.next()
                        iterator.remove()

                        val insertion = DelegatingInsnList()
                        insertion.addVarInsn(Opcodes.ILOAD, 0)
                        insertion.addVarInsn(Opcodes.ALOAD, 7)
                        insertion.addVarInsn(Opcodes.ALOAD, 3)
                        insertion.addFieldInsn(Opcodes.GETSTATIC, EnchantmentUtil.INTERNAL_NAME, "INSTANCE", EnchantmentUtil.DESCRIPTOR)
                        insertion.addMethodInsn(Opcodes.INVOKESPECIAL, EnchantmentUtil.INTERNAL_NAME, "getHighestSuitableLevel", Shortcode.composeMethodDescriptor("V", "I", Enchantment, "java/util/List"), false)

                        instructions.insert(iterator.previous(), insertion)
                    }
            )
        }

        private fun transformEnchantmentHelperGenerateEnchantments(method: MethodNode) {
            val iterator = method.instructions.iterator()

            Shortcode.findForward(iterator,
                    Predicate {instruction: AbstractInsnNode -> instruction.type == AbstractInsnNode.INT_INSN && (instruction as IntInsnNode).operand == 50},
                    Runnable {
                        iterator.remove()
                        iterator.add(VarInsnNode(Opcodes.ILOAD, 2))
                        iterator.add(InsnNode(Opcodes.ICONST_2))
                        iterator.add(InsnNode(Opcodes.IDIV))
                        iterator.add(IntInsnNode(Opcodes.BIPUSH, 20))
                        iterator.add(InsnNode(Opcodes.IADD))
                    }
            )
        }

        fun transform(klass: ClassNode) {
            val methods = klass.methods
            val methodCount = methods.size
            var instruction: AbstractInsnNode?
            var methodInstruction: MethodInsnNode

            if (enchantmentClassNames.contains(klass.superName) || Enchantment == klass.name) {
                if (Enchantment != klass.name) {
                    enchantmentClassNames.add(klass.superName)
                }

                for (i: Int in 0 until methodCount) {
                    val method = methods[i]

                    if (method.name == getMaxLevel && method.desc == "()I") {
                        val newGetMaxLevel = klass.visitMethod(Opcodes.ACC_PUBLIC, getMaxLevel, "()I", null, null) as MethodNode
                        val getCustom = Label()

                        newGetMaxLevel.visitVarInsn(Opcodes.ALOAD, 0)
                        newGetMaxLevel.visitFieldInsn(Opcodes.GETFIELD, klass.name, "limitless_useGlobalMaxLevel", "Z")
                        newGetMaxLevel.visitJumpInsn(Opcodes.IFEQ, getCustom)
                        newGetMaxLevel.visitFieldInsn(Opcodes.GETSTATIC, LimitlessConfiguration.INTERNAL_NAME, "instance", LimitlessConfiguration.DESCRIPTOR)
                        newGetMaxLevel.visitFieldInsn(Opcodes.GETFIELD, LimitlessConfiguration.INTERNAL_NAME, "enchantment", EnchantmentConfiguration.DESCRIPTOR)
                        newGetMaxLevel.visitFieldInsn(Opcodes.GETFIELD, EnchantmentConfiguration.INTERNAL_NAME, "globalMaxLevel", "I")
                        newGetMaxLevel.visitInsn(Opcodes.IRETURN)
                        newGetMaxLevel.visitLabel(getCustom)
                        newGetMaxLevel.visitVarInsn(Opcodes.ALOAD, 0)
                        newGetMaxLevel.visitFieldInsn(Opcodes.GETFIELD, klass.name, limitless_maxLevel, "I")
                        newGetMaxLevel.visitInsn(Opcodes.IRETURN)

                        method.name = limitless_getOriginalMaxLevel

                        for (j in 0 until methodCount) {
                            if ("<init>" == methods[j].name) {
                                val instructions = methods[j].instructions
                                instruction = instructions.first

                                while (instruction != null) {
                                    if (instruction.opcode == Opcodes.RETURN) {
                                        val setField = DelegatingInsnList()
                                        val setOne = Label()

                                        setField.addVarInsn(Opcodes.ALOAD, 0) // this
                                        setField.addVarInsn(Opcodes.ALOAD, 0) // this this
                                        setField.addMethodInsn(Opcodes.INVOKEVIRTUAL, klass.name, limitless_getOriginalMaxLevel, "()I", false) // this I
                                        setField.addInsn(Opcodes.ICONST_1) // this I I
                                        setField.addJumpInsn(Opcodes.IF_ICMPLE, setOne) // this
                                        setField.addLdcInsn(Int.MAX_VALUE) // this I
                                        setField.addFieldInsn(Opcodes.PUTFIELD, klass.name, limitless_maxLevel, "I")
                                        setField.addInsn(Opcodes.RETURN)
                                        setField.addLabel(setOne)
                                        setField.addInsn(Opcodes.ICONST_1) // this I
                                        setField.addFieldInsn(Opcodes.PUTFIELD, klass.name, limitless_maxLevel, "I")

                                        instructions.insertBefore(instruction, setField)
                                    }

                                    instruction = instruction.next
                                }
                            }
                        }
                    } else if (method.name == getMaxPower && method.desc == "(I)I") {
                        method.name = "limitless_getOriginalMaxPower"

                        val newGetMaxPower = klass.visitMethod(Opcodes.ACC_PUBLIC, getMaxPower, "(I)I", null, null) as MethodNode
                        newGetMaxPower.visitLdcInsn(Int.MAX_VALUE)
                        newGetMaxPower.visitInsn(Opcodes.IRETURN)
                    }
                }
            }
            for (method in methods) {
                instruction = method.instructions.first
                while (instruction != null) {
                    if (instruction.type == AbstractInsnNode.METHOD_INSN) {
//                    switch (instruction.getOpcode()) {
//                        case Opcodes.INVOKEVIRTUAL:
                        if (CompoundTag == (instruction as MethodInsnNode).owner) {
                            methodInstruction = instruction
                            if (putShort == methodInstruction.name && methodInstruction.previous.opcode == Opcodes.I2S) {
                                method.instructions.remove(methodInstruction.previous)
                                methodInstruction.name = putInt
                                methodInstruction.desc = putIntDescriptor
                                if (methodInstruction.previous.opcode == Opcodes.I2B) {
                                    method.instructions.remove(methodInstruction.previous)
                                }
                            } else if (getShort == methodInstruction.name) {
                                methodInstruction.name = getInt
                                methodInstruction.desc = getIntDescriptor
                            } else if (putByte == methodInstruction.name && methodInstruction.previous.opcode == Opcodes.I2B) {
                                method.instructions.remove(methodInstruction.previous)
                                methodInstruction.name = putInt
                                methodInstruction.desc = putIntDescriptor
                            } else if (getByte == methodInstruction.name) {
                                methodInstruction.name = getInt
                                methodInstruction.desc = getIntDescriptor
                            }
                        }
                        //
//                            break;
//
//                        case Opcodes.INVOKESTATIC:
//                            if (CALCULATE_REQUIRED_EXPERIENCE_LEVEL_METHOD_NAME.equals(((MethodInsnNode) instruction).name)) {
//                                methodInstruction = (MethodInsnNode) instruction;
//
//                                if (REMAPPED_INTERNAL_ENCHANTMENT_HELPER_CLASS_NAME.equals(methodInstruction.owner)) {
//
//                                }
//                            }
//                    }
                    }
                    instruction = instruction.next
                }
            }
        }

        init {
            TransformerApi.registerPostMixinAsmClassTransformer(::transform)
        }
    }
}