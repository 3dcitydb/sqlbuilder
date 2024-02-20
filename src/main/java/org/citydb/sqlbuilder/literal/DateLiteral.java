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

import java.sql.Date;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateLiteral extends Literal<Date> implements ScalarExpression {

    private DateLiteral(Date value) {
        super(value);
    }

    public static DateLiteral of(Date value) {
        return new DateLiteral(value);
    }

    public static DateLiteral of(java.util.Date value) {
        return of(value != null ? new Date(value.getTime()) : null);
    }

    public static DateLiteral of(Instant value) {
        return of(value != null ? Date.from(value) : null);
    }

    public static DateLiteral of(OffsetDateTime value) {
        return of(value != null ? value.toInstant() : null);
    }

    public static DateLiteral of(Calendar value) {
        return of(value != null ? value.toInstant() : null);
    }

    public static DateLiteral of(GregorianCalendar value) {
        return of(value != null ? value.toInstant() : null);
    }

    @Override
    public void buildSql(SqlBuilder builder) {
        if (value != null) {
            builder.append(builder.isUseJdbcEscapeNotation() ?
                    "{d '" + value + "'}" :
                    "'" + value + "'");
        } else {
            super.buildSql(builder);
        }
    }
}
