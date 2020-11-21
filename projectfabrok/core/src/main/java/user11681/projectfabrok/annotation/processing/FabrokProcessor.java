package user11681.projectfabrok.annotation.processing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Type;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import net.gudenau.lib.unsafe.Unsafe;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import user11681.projectfabrok.annotation.Callback;
import user11681.projectfabrok.annotation.Entrypoint;
import user11681.projectfabrok.annotation.Proxy;
import user11681.projectfabrok.annotation.processing.json.AnnotationHierarchy;
import user11681.projectfabrok.annotation.processing.json.JsonAnnotation;
import user11681.projectfabrok.annotation.processing.json.JsonAnnotationType;
import user11681.projectfabrok.annotation.processing.json.JsonElementAnnotations;
import user11681.reflect.Classes;

public class FabrokProcessor extends BaseProcessor {
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Attribute.Array.class, FabrokTypeAdapters.array)
        .registerTypeAdapter(Attribute.Class.class, FabrokTypeAdapters.klass)
        .registerTypeAdapter(Attribute.Compound.class, FabrokTypeAdapters.compound)
        .registerTypeAdapter(Attribute.Constant.class, FabrokTypeAdapters.constant)
        .registerTypeAdapter(Attribute.Enum.class, FabrokTypeAdapters.enumeration)
        .registerTypeAdapter(Attribute.TypeCompound.class, FabrokTypeAdapters.typeCompound)
        .registerTypeAdapter(Attribute.UnresolvedClass.class, FabrokTypeAdapters.unresolvedClass)
        .registerTypeAdapter(Type.ClassType.class, FabrokTypeAdapters.classType)
        .create();
    private static final AnnotationHierarchy annotations = new AnnotationHierarchy();
    private static final ObjectOpenHashSet<String> processedTypes = new ObjectOpenHashSet<>();
    private static final ReferenceArrayList<Class<Annotation>> supportedAnnotationTypes = Classes.cast(ReferenceArrayList.wrap(new Class<?>[]{
//        Getter.class,
//        Setter.class,
        Callback.class,
        Callback.Callbacks.class,
        Entrypoint.class,
        Entrypoint.Entrypoints.class,
        Proxy.class
    }));
    private static final ObjectOpenHashSet<String> supportedAnnotationTypeNames = new ObjectOpenHashSet<>();
    private static final Object2ReferenceOpenHashMap<String, String> repeatableTypeNames = new Object2ReferenceOpenHashMap<>();

    static {
        for (final Class<Annotation> type : supportedAnnotationTypes) {
            supportedAnnotationTypeNames.add(type.getName().replace('$', '.'));

            try {
                final Method valueMethod = type.getDeclaredMethod("value");
                final Class<?> componentType = valueMethod.getReturnType().getComponentType();

                if (componentType != null && Annotation.class.isAssignableFrom(componentType)) {
                    final Repeatable repeatable = componentType.getAnnotation(Repeatable.class);

                    if (repeatable != null && repeatable.value() == type) {
                        repeatableTypeNames.put(type.getName(), componentType.getName());

                        continue;
                    }
                }
            } catch (final NoSuchMethodException ignored) {}

            annotations.put(type.getName(), new JsonAnnotationType());
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypeNames;
    }

    @Override
    public void init(final ProcessingEnvironment environment) {
        super.init(environment);

        this.printf("Project Fabrok™ annotation processor v0");

        FabrokTypeAdapters.processor = this;
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotationTypes, final RoundEnvironment environment) {
        if (environment.processingOver()) {
            try {
                final JsonObject serialized = new JsonObject();

                for (final Object2ReferenceMap.Entry<String, JsonAnnotationType> entry : annotations.object2ReferenceEntrySet()) {
                    serialized.add(entry.getKey(), gson.toJsonTree(entry.getValue()));
                }

                this.filer.createResource(StandardLocation.CLASS_OUTPUT, "", "projectfabrok-annotations.json").openOutputStream().write(gson.toJson(serialized).getBytes());
            } catch (final Throwable exception) {
                throw Unsafe.throwException(exception);
            }
        } else {
            for (final TypeElement annotationType : annotationTypes) {
                final String annotationTypeName = this.elements.getBinaryName(annotationType).toString();

                this.printf("Processing %s annotations.", annotationTypeName);

                for (final Element element : environment.getElementsAnnotatedWith(annotationType)) {
                    if (processedTypes.add(String.format("%s@%s", element.toString(), annotationTypeName))) {
                        final ElementKind kind = element.getKind();
                        final String name;

                        if (kind.isClass() || kind.isInterface()) {
                            name = TypeUtils.getTypeName(element.asType());
                        } else if (kind.isField()) {
                            name = String.format("%s::%s", TypeUtils.getTypeName(element.getEnclosingElement().asType()), element.toString());
                        } else {
                            name = String.format("%s::%s%s", TypeUtils.getTypeName(element.getEnclosingElement().asType()), element.getSimpleName().toString(), TypeUtils.getDescriptor(element));
                        }

                        this.printf("\t%s", name);

                        final String repeatableType = repeatableTypeNames.get(annotationTypeName);
                        final List<AnnotationMirror> elementAnnotations = getAnnotations(annotationType, element);
                        final Name repeatableName = this.getRepeatableType(annotationType).getQualifiedName();
                        final ReferenceArrayList<JsonAnnotation> serialized = this.serialize(elementAnnotations);

                        if (repeatableName.contentEquals(getName(Callback.class))) {
                            annotations:
                            for (final AnnotationMirror annotation : elementAnnotations) {
                                final Object2ReferenceOpenHashMap<String, Object> elements = this.getElementsWithDefaults(annotation);

                                for (final Element field : this.getEnclosedElements(this.elements.getTypeElement(elements.get("owner").toString()), ElementKind.FIELD)) {
                                    if (field.getSimpleName().contentEquals((CharSequence) elements.get("field"))) {
                                        continue annotations;
                                    }
                                }

                                throw new IllegalArgumentException(String.format("field %s was not found in %s", elements.get("field"), elements.get("owner")));
                            }
                        } else if (repeatableName.contentEquals(getName(Proxy.class))) {
                            for (final AnnotationMirror annotation : elementAnnotations) {
                                final Object2ReferenceOpenHashMap<String, Object> elements = this.getElementsWithDefaults(annotation);

                                this.printf("\t\t\towner: %s", elements.get("ownerName"));
//                                serialized.((Element) elements.get("owner")).getKind();
                            }
                        }

                        annotations.get(repeatableType == null ? annotationTypeName : repeatableType).computeIfAbsent(name, (final String typeName) -> new JsonElementAnnotations(kind)).annotations.addAll(serialized);
                    }
                }
            }
        }

        return false;
    }

    private ReferenceArrayList<JsonAnnotation> serialize(final List<AnnotationMirror> annotations) {
        final ReferenceArrayList<JsonAnnotation> serializedAnnotations = ReferenceArrayList.wrap(new JsonAnnotation[2], 0);

        for (final AnnotationMirror annotation : annotations) {
            final JsonAnnotation jsonAnnotation = new JsonAnnotation();

            try {
                for (final Object2ReferenceMap.Entry<String, Object> element : this.getElementsWithDefaults(annotation).object2ReferenceEntrySet()) {
                    jsonAnnotation.put(element.getKey(), element.getValue());
                }
            } catch (final Throwable throwable) {
                throw Unsafe.throwException(throwable);
            }

            serializedAnnotations.add(jsonAnnotation);
        }

        return serializedAnnotations;
    }
}
