package user11681.projectfabrok.synthesis;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.List;
import net.devtech.grossfabrichacks.entrypoints.PrePrePreLaunch;
import net.devtech.grossfabrichacks.transformer.TransformerApi;
import net.devtech.grossfabrichacks.transformer.asm.AsmClassTransformer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.metadata.MetadataGenerator;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import user11681.dynamicentry.DynamicEntry;
import user11681.projectfabrok.annotation.AccessType;
import user11681.projectfabrok.annotation.Callback;
import user11681.projectfabrok.annotation.ChainType;
import user11681.projectfabrok.annotation.Entrypoint;
import user11681.projectfabrok.annotation.Getter;
import user11681.projectfabrok.annotation.ModInterface;
import user11681.projectfabrok.annotation.Proxy;
import user11681.projectfabrok.annotation.Setter;
import user11681.projectfabrok.annotation.Var;
import user11681.projectfabrok.annotation.processing.AnnotationContext;
import user11681.projectfabrok.annotation.processing.json.AnnotationHierarchy;
import user11681.projectfabrok.synthesis.accessor.AccessorInfo;
import user11681.projectfabrok.synthesis.accessor.GetterInfo;
import user11681.projectfabrok.synthesis.accessor.SetterInfo;
import user11681.projectfabrok.synthesis.proxy.ProxyInfo;
import user11681.reflect.Classes;
import user11681.shortcode.Shortcode;
import user11681.shortcode.instruction.DelegatingInsnList;

public class FabrokTransformer extends Shortcode implements PrePrePreLaunch, AsmClassTransformer {
    public static final FabricLoader fabric = FabricLoader.getInstance();

    private static final Object2ReferenceOpenHashMap<String, ObjectOpenHashSet<String>> interfaceHierarchy = new Object2ReferenceOpenHashMap<>();
    private static final Object2ReferenceOpenHashMap<String, ReferenceArrayList<FieldNode>> inheritableFields = new Object2ReferenceOpenHashMap<>();
    private static final Object2ReferenceOpenHashMap<String, ReferenceArrayList<AccessorInfo>> inheritableMethods = new Object2ReferenceOpenHashMap<>();
    private static final Object2ReferenceOpenHashMap<String, ObjectOpenHashSet<String>> inheritors = new Object2ReferenceOpenHashMap<>();
    private static final Object2ReferenceOpenHashMap<String, Object2ReferenceOpenHashMap<String, ProxyInfo>> proxies = new Object2ReferenceOpenHashMap<>();

    private static final AnnotationHierarchy annotations = AnnotationHierarchy.read().process();

    @Override
    public void onPrePrePreLaunch() {
        DynamicEntry.tryExecute("projectfabrok:preinit", Runnable.class, Runnable::run);

        Classes.addURL(Classes.systemClassLoader, FabrokTransformer.class.getProtectionDomain().getCodeSource().getLocation());
        Classes.load(Classes.systemClassLoader, "net.fabricmc.loader.metadata.MetadataGenerator");

        TransformerApi.registerPostMixinAsmClassTransformer(this);

        for (final AnnotationContext annotation : annotations.getAnnotations(Entrypoint.class)) {
            final String modID = annotation.get("id");
            final String languageAdapter = annotation.get("adapter");

            for (final String entrypoint : (List<String>) annotation.get("value")) {
                MetadataGenerator.addEntrypoint(modID, entrypoint, languageAdapter, annotation.element.replaceFirst("\\(.*", ""));
            }
        }

        for (final AnnotationContext annotation : annotations.getAnnotations(Callback.class)) {
            for (final String event : (List<String>) annotation.get("value")) {

            }
        }

        for (final AnnotationContext annotation : annotations.getAnnotations(Proxy.class)) {
            final String owner = annotation.get("owner");
            final String name = annotation.get("value");
            final String descriptor = annotation.get("descriptor");

            Object2ReferenceOpenHashMap<String, ProxyInfo> memberProxies = proxies.get(owner);

            if (memberProxies == null) {
                memberProxies = new Object2ReferenceOpenHashMap<>();

                proxies.put(owner, memberProxies);
            }

//            memberProxies.put(name, new );
        }

        DynamicEntry.tryExecute("projectfabrok:postinit", Runnable.class, Runnable::run);
    }

    @Override
    public void transform(final ClassNode klass) {
        Classes.load(false, toBinaryName(klass.superName));

        for (final String interfase : klass.interfaces) {
            Classes.load(false, toBinaryName(interfase));
        }

        applyModInterfaces(klass);
        buildHierarchy(klass);
        processFields(klass);
        processMethods(klass);
        inherit(klass);

    }

    private static void applyModInterfaces(final ClassNode klass) {
        if (klass.invisibleAnnotations != null) {
            final ObjectOpenHashSet<String> additions = new ObjectOpenHashSet<>(1);
            final ObjectOpenHashSet<String> removals = new ObjectOpenHashSet<>(1);
            final List<String> interfaces = klass.interfaces;
            final List<AnnotationNode> annotations = getRepeatableAnnotations(klass.invisibleAnnotations, ModInterface.class, ModInterface.ModInterfaces.class);

            if (!annotations.isEmpty()) {
                annotations:
                for (final AnnotationNode annotation : annotations) {
                    final List<String> ids = getAnnotationValue(annotation, "id");
                    final List<String> types = getAnnotationValue(annotation, "type");

                    for (final String id : ids) {
                        if (!fabric.isModLoaded(id)) {
                            for (final String type : types) {
                                removals.add(toInternalName(type));
                            }

                            continue annotations;
                        }
                    }

                    for (final String type : types) {
                        additions.add(toInternalName(type));
                    }
                }

                for (final String removal : removals) {
                    if (!additions.contains(removal)) {
                        interfaces.remove(removal);
                    }
                }

                for (final String addition : additions) {
                    if (!interfaces.contains(addition)) {
                        interfaces.add(addition);
                    }
                }
            }
        }
    }

    private static void buildHierarchy(final ClassNode klass) {
        if (hasFlag(klass.access, ACC_INTERFACE)) {
            final String name = klass.name;
            final ObjectOpenHashSet<String> superinterfaces = new ObjectOpenHashSet<>(new String[0]);

            interfaceHierarchy.put(name, superinterfaces);

            for (final String interfase : klass.interfaces) {
                final ObjectOpenHashSet<String> higherInterfaces = interfaceHierarchy.get(interfase);

                if (higherInterfaces != null) {
                    superinterfaces.addAll(higherInterfaces);
                    superinterfaces.add(interfase);
                }
            }
        }
    }

    private static void processFields(final ClassNode klass) {
        if (klass.invisibleAnnotations != null) {
            final List<AnnotationNode> annotations = getRepeatableAnnotations(klass.invisibleAnnotations, Var.class, Var.Vars.class);
            final int annotationCount = annotations.size();

            if (annotationCount > 0) {
                if (hasFlag(klass.access, ACC_INTERFACE)) {
                    final FieldNode[] fieldNodes = new FieldNode[annotationCount];

                    for (int j = 0; j < annotationCount; j++) {
                        fieldNodes[j] = getField(klass, annotations.get(j));
                    }

                    inheritableFields.put(klass.name, ReferenceArrayList.wrap(fieldNodes));

                    initializeInheritorSet(klass.name);
                } else {
                    for (int j = 0; j < annotationCount; j++) {
                        klass.fields.add(getField(klass, annotations.get(j)));
                    }
                }
            }
        }
    }

    private static void processMethods(final ClassNode klass) {
        final boolean isInterface = hasFlag(klass.access, ACC_INTERFACE);

        for (final MethodNode method : klass.methods.toArray(new MethodNode[0])) {
            if (method.invisibleAnnotations != null) {
                for (final AnnotationNode annotation : method.invisibleAnnotations) {
                    if (getDescriptor(Getter.class).equals(annotation.desc)) {
                        getter(klass, method, annotation);

                        if (isInterface) {
                            initializeInheritorSet(klass.name);
                        }
                    }

                    if (getDescriptor(Setter.class).equals(annotation.desc)) {
                        setter(klass, method, annotation);

                        if (isInterface) {
                            initializeInheritorSet(klass.name);
                        }
                    }
                }
            }
        }
    }

    private static void inherit(final ClassNode klass) {
        if (!hasFlag(klass.access, ACC_INTERFACE)) {
            for (final String interfase : getInterfaces(klass)) {
                final ObjectOpenHashSet<String> implementors = inheritors.get(interfase);

                if (implementors != null) {
                    if (!implementors.contains(klass.superName)) {
                        if (inheritableFields.containsKey(interfase)) {
                            final ObjectOpenHashSet<String> fieldNames = new ObjectOpenHashSet<>();

                            for (final FieldNode field : klass.fields) {
                                fieldNames.add(field.name);
                            }

                            for (final FieldNode field : inheritableFields.get(interfase).elements()) {
                                if (!fieldNames.contains(field.name)) {
                                    klass.fields.add(field);
                                }
                            }
                        }

                        if (inheritableMethods.containsKey(interfase)) {
                            final Object2ReferenceOpenHashMap<String, MethodNode> methods = new Object2ReferenceOpenHashMap<>();

                            for (final MethodNode method : klass.methods) {
                                methods.put(method.name + method.desc.substring(0, method.desc.indexOf(')') + 1), method);
                            }

                            for (final AccessorInfo accessor : inheritableMethods.get(interfase)) {
                                final MethodNode method = methods.get(accessor.name + accessor.descriptor.substring(0, accessor.descriptor.indexOf(')') + 1));

                                if (method == null) {
                                    klass.methods.add(accessor.toNode(klass.name));
                                } else if (hasFlag(method.access, ACC_NATIVE)) {
                                    accessor.accept(methods.get(accessor.name), klass.name);
                                }
                            }
                        }

                    }

                    implementors.add(klass.name);
                }
            }
        }
    }

    private static void initializeInheritorSet(final String klass) {
        if (!inheritors.containsKey(klass)) {
            inheritors.put(klass, new ObjectOpenHashSet<>());
        }
    }

    private static FieldNode getField(final ClassNode klass, final AnnotationNode annotation) {
        final String name = getAnnotationValue(annotation, "name");
        final FieldNode[] fields = klass.fields.toArray(new FieldNode[0]);
        final int fieldCount = fields.length;

        for (int i = 0; i < fieldCount; i++) {
            if (fields[i].name.equals(name)) {
                throw new RuntimeException(String.format("field %s already exists in %s.", name, klass.name));
            }
        }

        return new FieldNode(getAnnotationValue(annotation, "access", Var.DEFAULT_ACCESS), name, toDescriptor(getAnnotationValue(annotation, "descriptor")), null, null);
    }

    private static void getter(final ClassNode klass, final MethodNode method, final AnnotationNode annotation) {
        final int access = setAndGetAccess(method, annotation);
        final String fieldName = getAnnotationValue(annotation, "value");
        final String fieldDescriptor = getReturnType(method.desc);

        if (hasFlag(method.access, ACC_NATIVE)) {
            method.visitVarInsn(ALOAD, 0);
            method.visitFieldInsn(GETFIELD, klass.name, fieldName, fieldDescriptor);
            method.visitInsn(getReturnOpcode(fieldDescriptor));

            method.access = access;
        } else if (hasFlag(klass.access, ACC_INTERFACE)) {
            final ReferenceArrayList<AccessorInfo> accessors = inheritableMethods.get(klass.name);
            final GetterInfo info = new GetterInfo(method, access, fieldName, fieldDescriptor);

            if (accessors == null) {
                inheritableMethods.put(klass.name, ReferenceArrayList.wrap(new AccessorInfo[]{info}));
            } else {
                accessors.add(info);
            }
        } else {
            final DelegatingInsnList instructions = new DelegatingInsnList();

            instructions.addVarInsn(ALOAD, 0);
            instructions.addFieldInsn(GETFIELD, klass.name, fieldName, fieldDescriptor);
            instructions.addInsn(getReturnOpcode(fieldDescriptor));

            insertBeforeEveryReturn(method, instructions);

            method.access = access;
        }
    }

    private static void setter(final ClassNode klass, MethodNode method, final AnnotationNode annotation) {
        final ReferenceArrayList<String> descriptor = parseDescriptor(method);
        final String fieldName = getAnnotationValue(annotation, "value");
        final String fieldDescriptor = descriptor.get(0);
        final String returnType = descriptor.top();
        final int access = setAndGetAccess(method, annotation);
        final int returnOpcode = getReturnOpcode(returnType);
        final ChainType chainType = getChainType(klass, annotation, returnType, fieldDescriptor);

        if (hasFlag(method.access, ACC_NATIVE)) {
            method.visitVarInsn(ALOAD, 0);
            method.visitVarInsn(getLoadOpcode(fieldDescriptor), 1);
            method.visitFieldInsn(PUTFIELD, klass.name, fieldName, fieldDescriptor);

            if (chainType.enabled) {
                method.visitVarInsn(ALOAD, 0);
            }

            method.visitInsn(returnOpcode);

            method.access = access;
        } else if (!hasFlag(klass.access, ACC_INTERFACE)) {
            final DelegatingInsnList instructions = new DelegatingInsnList();

            instructions.addVarInsn(ALOAD, 0);
            instructions.addVarInsn(getLoadOpcode(fieldDescriptor), 1);
            instructions.addFieldInsn(PUTFIELD, klass.name, fieldName, fieldDescriptor);

            if (chainType == ChainType.FORCED) {
                instructions.addVarInsn(ALOAD, 0);
            }

            insertBeforeEveryReturn(method, instructions);

            method.access = access;
        } else {
            final SetterInfo info = new SetterInfo(method, access, fieldName, fieldDescriptor, chainType);

            inheritableMethods.computeIfAbsent(klass.name, (final String name) -> ReferenceArrayList.wrap(new AccessorInfo[0])).add(info);
        }
    }

    private static int setAndGetAccess(final MethodNode method, final AnnotationNode annotation) {
        final int access = getAnnotationValue(annotation, "access", Setter.DEFAULT_ACCESS);
        final int newAccess;

        if (access == Getter.DEFAULT_ACCESS) {
            newAccess = method.access & ~ACC_NATIVE;
        } else if (getAnnotationValue(annotation, "accessType", AccessType.OVERRIDE) == AccessType.OVERRIDE) {
            newAccess = access;
        } else {
            newAccess = method.access | access;
        }

        return newAccess;
    }

    private static ChainType getChainType(final ClassNode klass, final AnnotationNode setterAnnotation, final String returnType, final String fieldDescriptor) {
        final String returnTypeName = toBinaryName(returnType);
        final String fieldTypeName = toBinaryName(fieldDescriptor);
        final ChainType chainType = getAnnotationValue(setterAnnotation, "chainType", ChainType.AUTOMATIC);

        if (chainType == ChainType.FORCED) {
            return ChainType.FORCED;
        }

        final String classBinaryName = toBinaryName(klass.name);

        if (fieldTypeName.equals(classBinaryName)) {
            return ChainType.NONE;
        }

        if (returnTypeName.equals(classBinaryName)) {
            return chainType;
        }

        String superclassName = toBinaryName(klass.superName);
        Class<?> superclass = Classes.load(false, superclassName);

        while (!fieldTypeName.equals(superclassName)) {
            if (returnTypeName.equals(superclassName)) {
                return chainType;
            }

            superclass = superclass.getSuperclass();

            if (superclass == null) {
                break;
            }

            superclassName = superclass.getName();
        }

        for (final String interfaceName : klass.interfaces) {
            Class<?> interfase = Classes.load(interfaceName);
        }

        return ChainType.NONE;
    }

    private static ObjectOpenHashSet<String> getInterfaces(final ClassNode klass) {
        final ObjectOpenHashSet<String> interfaces = new ObjectOpenHashSet<>();

        for (final String interfase : klass.interfaces) {
            final ObjectOpenHashSet<String> superinterfaces = interfaceHierarchy.get(interfase);

            if (superinterfaces != null) {
                interfaces.addAll(superinterfaces);
            }
        }

        return interfaces;
    }
}
