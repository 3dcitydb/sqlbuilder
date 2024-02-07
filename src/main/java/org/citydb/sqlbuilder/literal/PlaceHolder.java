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

import org.citydb.sqlbuilder.SQLBuilder;
import org.citydb.sqlbuilder.util.UserProperties;

import java.util.List;
import java.util.Map;

public class PlaceHolder extends Literal<Object> {
    private UserProperties userProperties;

    private PlaceHolder(Object value) {
        super(value);
    }

    public static PlaceHolder empty() {
        return new PlaceHolder(null);
    }

    public static PlaceHolder of(Object value) {
        return new PlaceHolder(value);
    }

    public static PlaceHolder of(Object value, Map<String, Object> properties) {
        PlaceHolder placeHolder = new PlaceHolder(value);
        if (properties != null) {
            properties.forEach(placeHolder.getUserProperties()::set);
        }

        return placeHolder;
    }

    public PlaceHolder setValue(Object value) {
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
    public void getInvolvedPlaceHolders(List<PlaceHolder> placeHolders) {
        placeHolders.add(this);
    }

    @Override
    public void buildSQL(SQLBuilder builder) {
        builder.append("?");
    }
}
