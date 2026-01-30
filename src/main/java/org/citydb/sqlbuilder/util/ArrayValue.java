/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2022-2026
 * virtualcitysystems GmbH, Germany
 * https://vc.systems/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
