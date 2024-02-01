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

package org.citydb.sqlbuilder.schema;

public class DefaultAliasGenerator implements AliasGenerator {
    char character = 'a';
    int counter = 0;

    @Override
    public String currentAlias() {
        StringBuilder alias = new StringBuilder(String.valueOf(character));
        if (counter > 0)
            alias.append(counter);

        return alias.toString();
    }

    @Override
    public String nextAlias() {
        StringBuilder alias = new StringBuilder(String.valueOf(character++));
        if (counter > 0)
            alias.append(counter);

        if (character > 'z') {
            character = 'a';

            if (++counter == 100)
                counter = 0;
        }

        return alias.toString();
    }

    @Override
    public void reset() {
        character = 'a';
        counter = 0;
    }

    public void updateFrom(DefaultAliasGenerator other) {
        character = other.character;
        counter = other.counter;
    }
}
