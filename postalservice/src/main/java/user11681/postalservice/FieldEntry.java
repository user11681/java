package user11681.postalservice;

import java.lang.reflect.Field;

import net.gudenau.lib.unsafe.Unsafe;

public class FieldEntry {
    public final long offset;
    public final Class<?> type;
    public final String name;

    public FieldEntry(Field field) {
        this.offset = Unsafe.objectFieldOffset(field);
        this.type = field.getType();
        this.name = field.getName();
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that instanceof String && this.name.equals(that);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
