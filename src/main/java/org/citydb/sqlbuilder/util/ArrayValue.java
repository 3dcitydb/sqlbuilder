package org.citydb.sqlbuilder.util;

import java.util.Collection;
import java.util.Objects;

public class ArrayValue {
    private final Collection<?> values;
    private final Class<?> elementType;

    private ArrayValue(Collection<?> values, Class<?> elementType) {
        this.values = Objects.requireNonNull(values, "The values must not be null.");
        this.elementType = Objects.requireNonNull(elementType, "The element type must not be null.");
    }

    public static <T> ArrayValue of(Collection<? extends T> values, Class<T> elementType) {
        return new ArrayValue(values, elementType);
    }

    public Collection<?> getValues() {
        return values;
    }

    public Class<?> getElementType() {
        return elementType;
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}
