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

import org.citydb.sqlbuilder.SqlBuilder;
import org.citydb.sqlbuilder.common.SqlVisitor;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimestampLiteral extends Literal<Timestamp> implements ScalarExpression {

    private TimestampLiteral(Timestamp value) {
        super(value);
    }

    public static TimestampLiteral of(Timestamp value) {
        return new TimestampLiteral(value);
    }

    public static TimestampLiteral of(Instant value) {
        return of(value != null ? Timestamp.from(value) : null);
    }

    public static TimestampLiteral of(OffsetDateTime value) {
        return of(value != null ? value.toInstant() : null);
    }

    public static TimestampLiteral of(Date value) {
        return of(value != null ? value.toInstant() : null);
    }

    public static TimestampLiteral of(Calendar value) {
        return of(value != null ? value.toInstant() : null);
    }

    public static TimestampLiteral of(GregorianCalendar value) {
        return of(value != null ? value.toInstant() : null);
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        if (value != null) {
            builder.append(builder.isUseJdbcEscapeNotation() ?
                    "{ts '" + value + "'}" :
                    "'" + value + "'");
        } else {
            super.buildSql(builder);
        }
    }

    @Override
    public void accept(SqlVisitor visitor) {
        visitor.visit(this);
    }
}
