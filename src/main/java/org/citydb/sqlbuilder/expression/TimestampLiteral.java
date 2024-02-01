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

package org.citydb.sqlbuilder.expression;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimestampLiteral extends JDBCEscapeLiteral<Timestamp> {

    public TimestampLiteral(Timestamp value) {
        super(value, JDBCEscape.TIMESTAMP);
    }

    public TimestampLiteral(Date value) {
        this(new Timestamp(value.getTime()));
    }

    public TimestampLiteral(Calendar calendar) {
        this(calendar.getTime());
    }

    public TimestampLiteral(GregorianCalendar calendar) {
        this(calendar.getTime());
    }

    @Override
    public SQLLiteralType getType() {
        return SQLLiteralType.TIMESTAMP;
    }
}
