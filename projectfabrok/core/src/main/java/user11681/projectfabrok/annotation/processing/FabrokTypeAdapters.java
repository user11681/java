package user11681.projectfabrok.annotation.processing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.util.Pair;
import com.sun.tools.javac.util.UnsharedNameTable;
import java.lang.reflect.Type;
import net.gudenau.lib.unsafe.Unsafe;

public class FabrokTypeAdapters {
    public static FabrokProcessor processor;

    public static final JsonSerializer<Attribute.Array> array = ((final Attribute.Array src, final Type typeOfSrc, final JsonSerializationContext context) -> {
        final JsonArray array = new JsonArray();

        for (final Attribute attribute : src.values) {
            array.add(context.serialize(attribute));
        }

        return array;
    });

    public static final JsonSerializer<Attribute.Class> klass = ((final Attribute.Class src, final Type typeOfSrc, final JsonSerializationContext context) -> new JsonPrimitive(src.classType.toString()));

    public static final JsonSerializer<Attribute.Compound> compound = ((final Attribute.Compound src, final Type typeOfSrc, final JsonSerializationContext context) -> {
        final JsonObject object = new JsonObject();

        for (final Pair<Symbol.MethodSymbol, Attribute> pair : src.values) {
            object.add(pair.fst.name.toString(), context.serialize(pair.snd));
        }

        return object;
    });

    public static final JsonSerializer<Attribute.Constant> constant = ((final Attribute.Constant src, final Type typeOfSrc, final JsonSerializationContext context) -> context.serialize(src.value));

    public static final JsonSerializer<Attribute.Enum> enumeration = ((final Attribute.Enum src, final Type typeOfSrc, final JsonSerializationContext context) -> new JsonPrimitive(src.value.toString()));

    public static final JsonSerializer<Attribute.TypeCompound> typeCompound = ((final Attribute.TypeCompound src, final Type typeOfSrc, final JsonSerializationContext context) -> {
        throw Unsafe.throwException(null);
    });

    public static final JsonSerializer<Attribute.UnresolvedClass> unresolvedClass = ((final Attribute.UnresolvedClass src, final Type typeOfSrc, final JsonSerializationContext context) -> new JsonPrimitive(src.classType.toString()));

    public static final JsonSerializer<ClassType> classType = ((final ClassType src, final Type typeOfSrc, final JsonSerializationContext context) -> new JsonPrimitive(src.toString()));

    public static final JsonSerializer<UnsharedNameTable> unsharedNameTable = (final UnsharedNameTable src, final Type typeOfSrc, final JsonSerializationContext context) -> {
        processor.print(src);

        return null;
    };

    public static final JsonSerializer<Object> hashEntry = (final Object src, final Type typeOfSrc, final JsonSerializationContext context) -> {
        processor.print(src);

        return null;
    };
}
