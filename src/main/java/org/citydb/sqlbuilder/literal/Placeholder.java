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

import org.citydb.sqlbuilder.common.SqlVisitor;
import org.citydb.sqlbuilder.query.QueryExpression;
import org.citydb.sqlbuilder.util.UserProperties;

public class Placeholder extends Literal<Object> implements QueryExpression {
    private UserProperties userProperties;

    private Placeholder(Object value) {
        super(value);
    }

    public static Placeholder empty() {
        return new Placeholder(null);
    }

    public static Placeholder of(Object value) {
        return new Placeholder(value);
    }

    public static Placeholder of(Literal<?> literal) {
        return of(literal != null ? literal.getValue().orElse(null) : null);
    }

    public Placeholder setValue(Object value) {
        this.value = value;
        return this;
    }

    public boolean hasUserProperties() {
        return userProperties != null && !userProperties.isEmpty();
    }

    public UserProperties getUserProperties() {
        if (userProperties == null) {
            userProperties = new UserProperties();
        }

        return userProperties;
    }

    @Override
    public Placeholder as(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void accept(SqlVisitor visitor) {
        visitor.visit(this);
    }
}
