/*
 * sqlbuilder - Dynamic SQL builder for the 3D City Database
 * http://www.3dcitydb.org/
 *
 * Copyright 2013-2018 Claus Nagel <claus.nagel@gmail.com>
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

import java.sql.Date;
import java.util.GregorianCalendar;

public class DateLiteral extends AbstractJDBCEscapeLiteral<Date> {

    public DateLiteral(Date value) {
        super(value, JDBCEscape.DATE);
    }

    public DateLiteral(java.util.Date date) {
        this(new Date(date.getTime()));
    }

    public DateLiteral(GregorianCalendar calendar) {
        this(calendar.getTime());
    }

    @Override
    public SQLLiteralType getType() {
        return SQLLiteralType.DATE;
    }

}
