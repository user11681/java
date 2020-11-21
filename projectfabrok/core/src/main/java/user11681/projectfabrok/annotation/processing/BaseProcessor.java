package user11681.projectfabrok.annotation.processing;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.lang.annotation.Repeatable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public abstract class BaseProcessor implements Processor {
    protected static final Object NULL = null;

    protected Messager messager;
    protected Filer filer;
    protected Types types;
    protected Elements elements;

    protected static String getName(final Class<?> klass) {
        return klass.getName().replace('$', '.');
    }

    protected static String toSourceName(final String binaryName) {
        return binaryName.replace('$', '.');
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.EMPTY_SET;
    }

    @Override
    public Iterable<? extends Completion> getCompletions(final Element element, final AnnotationMirror annotation, final ExecutableElement member, final String userText) {
        return null;
    }

    @Override
    public void init(final ProcessingEnvironment environment) {
        this.messager = environment.getMessager();
        this.filer = environment.getFiler();
        this.types = environment.getTypeUtils();
        this.elements = environment.getElementUtils();
    }

    protected ReferenceArrayList<Element> getEnclosedElements(final Element container, final ElementKind kind) {
        final ReferenceArrayList<Element> elements = ReferenceArrayList.wrap(new Element[1], 0);

        for (final Element element : container.getEnclosedElements()) {
            if (element.getKind() == kind) {
                elements.add(element);
            }
        }

        return elements;
    }

    protected List<AnnotationMirror> getAnnotations(final TypeElement annotationType, final Element element) {
        if (this.isRepeatable(annotationType)) {
            return (List<AnnotationMirror>) element.getAnnotationMirrors();
        }

        return this.getValue(this.getAnnotation(annotationType, element), "value");
    }

    protected <T> T getValue(final AnnotationMirror annotation, final String name) {
        for (final Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> element : this.elements.getElementValuesWithDefaults(annotation).entrySet()) {
            if (element.getKey().getSimpleName().contentEquals(name)) {
                return (T) element.getValue().getValue();
            }
        }

        return (T) NULL;
    }

    protected Object2ReferenceOpenHashMap<String, Object> getElementsWithDefaults(final AnnotationMirror annotation) {
        final Object2ReferenceOpenHashMap<String, Object> values = new Object2ReferenceOpenHashMap<>();

        for (final Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> element : this.elements.getElementValuesWithDefaults(annotation).entrySet()) {
            values.put(element.getKey().getSimpleName().toString(), element.getValue().getValue());
        }

        return values;
    }

    protected TypeElement getRepeatableType(final TypeElement annotationType) {
        for (final Element element : annotationType.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD && element.getSimpleName().contentEquals("value")) {
                final TypeMirror returnType = ((ExecutableElement) element).getReturnType();

                if (returnType instanceof ArrayType) {
                    final TypeMirror componentType = ((ArrayType) returnType).getComponentType();
                    final AnnotationMirror repeatable = this.getAnnotation(Repeatable.class, this.toTypeElement(componentType));

                    if (repeatable != null) {
                        for (final Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> repeatableElement : repeatable.getElementValues().entrySet()) {
                            if (repeatableElement.getKey().getSimpleName().contentEquals("value") && this.types.isSameType((TypeMirror) repeatableElement.getValue().getValue(), annotationType.asType())) {
                                return this.elements.getTypeElement(componentType.toString());
                            }
                        }
                    }
                }
            }
        }

        return annotationType;
    }

    protected AnnotationMirror getAnnotation(final Class<?> type, final Element element) {
        for (final AnnotationMirror mirror : element.getAnnotationMirrors()) {
            if (this.types.isSameType(mirror.getAnnotationType(), this.toTypeElement(type).asType())) {
                return mirror;
            }
        }

        return null;
    }

    protected AnnotationMirror getAnnotation(final TypeElement type, final Element element) {
        for (final AnnotationMirror mirror : element.getAnnotationMirrors()) {
            if (this.types.isSameType(mirror.getAnnotationType(), type.asType())) {
                return mirror;
            }
        }

        return null;
    }

    protected TypeElement toTypeElement(final Class<?> klass) {
        return this.elements.getTypeElement(klass.getName().replace('$', '.'));
    }

    protected TypeElement toTypeElement(final TypeMirror type) {
        return this.elements.getTypeElement(type.toString());
    }

    protected boolean isRepeatable(final TypeElement annotationType) {
        return this.areTypesSame(this.getRepeatableType(annotationType), annotationType);
    }

    protected boolean areTypesSame(final Element first, final Element second) {
        return this.types.isSameType(first.asType(), second.asType());
    }

    protected void printf(final String format, final Object... arguments) {
        this.print(String.format(format, arguments));
    }

    protected void print(final Object... objects) {
        final String string = Arrays.toString(objects);

        this.print(string.substring(1, string.length() - 1).replace(",", ""));
    }

    protected void print(final String text) {
        this.messager.printMessage(Diagnostic.Kind.OTHER, text);
    }
}
