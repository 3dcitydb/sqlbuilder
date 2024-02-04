/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * https://www.3dcitydb.org/
 *
 * Copyright 2022-2024
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

package org.citydb.sqlbuilder.literal;

import org.citydb.sqlbuilder.query.LiteralList;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Literals {

    public static Literal<?> of(Object value) {
        if (value instanceof Literal<?> literal) {
            return literal;
        } else if (value instanceof String literal) {
            return StringLiteral.of(literal);
        } else if (value instanceof Double literal) {
            return DoubleLiteral.of(literal);
        } else if (value instanceof Long literal) {
            return IntegerLiteral.of(literal);
        } else if (value instanceof Integer literal) {
            return IntegerLiteral.of(literal);
        } else if (value instanceof Number literal) {
            return literal.doubleValue() == literal.longValue() ?
                    IntegerLiteral.of(literal.longValue()) :
                    DoubleLiteral.of(literal.doubleValue());
        } else if (value instanceof Boolean literal) {
            return BooleanLiteral.of(literal);
        } else if (value instanceof Date literal) {
            return DateLiteral.of(literal);
        } else if (value instanceof Timestamp literal) {
            return TimestampLiteral.of(literal);
        } else {
            return NullLiteral.newInstance();
        }
    }

    public static LiteralList asList(Object... values) {
        return LiteralList.of(values);
    }

    public static LiteralList asList(List<Object> values) {
        return LiteralList.of(values);
    }

    public static BooleanLiteral ofBoolean(boolean value) {
        return BooleanLiteral.of(value);
    }

    public static BooleanLiteral ofBoolean(Boolean value) {
        return BooleanLiteral.of(value);
    }

    public static DateLiteral ofDate(Date value) {
        return DateLiteral.of(value);
    }

    public static DateLiteral ofDate(java.util.Date value) {
        return DateLiteral.of(value);
    }

    public static DateLiteral ofDate(Instant value) {
        return DateLiteral.of(value);
    }

    public static DateLiteral ofDate(OffsetDateTime value) {
        return DateLiteral.of(value);
    }

    public static DateLiteral ofDate(Calendar value) {
        return DateLiteral.of(value);
    }

    public static DateLiteral ofDate(GregorianCalendar value) {
        return DateLiteral.of(value);
    }

    public static DoubleLiteral ofDouble(double value) {
        return DoubleLiteral.of(value);
    }

    public static DoubleLiteral ofDouble(Number value) {
        return DoubleLiteral.of(value);
    }

    public static IntegerLiteral ofInteger(int value) {
        return IntegerLiteral.of(value);
    }

    public static IntegerLiteral ofLong(long value) {
        return IntegerLiteral.of(value);
    }

    public static IntegerLiteral ofLong(Number value) {
        return IntegerLiteral.of(value);
    }

    public static NullLiteral ofNull() {
        return NullLiteral.newInstance();
    }

    public static PlaceHolder emptyPlaceHolder() {
        return PlaceHolder.empty();
    }

    public static PlaceHolder placeHolder(Object value) {
        return PlaceHolder.of(value);
    }

    public static StringLiteral ofString(String value) {
        return StringLiteral.of(value);
    }

    public static TimestampLiteral ofTimestamp(Timestamp value) {
        return TimestampLiteral.of(value);
    }

    public static TimestampLiteral ofTimestamp(Instant value) {
        return TimestampLiteral.of(value);
    }

    public static TimestampLiteral ofTimestamp(OffsetDateTime value) {
        return TimestampLiteral.of(value);
    }

    public static TimestampLiteral ofTimestamp(java.util.Date value) {
        return TimestampLiteral.of(value);
    }

    public static TimestampLiteral ofTimestamp(Calendar value) {
        return TimestampLiteral.of(value);
    }

    public static TimestampLiteral ofTimestamp(GregorianCalendar value) {
        return TimestampLiteral.of(value);
    }
}
