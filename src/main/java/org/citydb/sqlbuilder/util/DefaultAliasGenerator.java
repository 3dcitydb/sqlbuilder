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

package org.citydb.sqlbuilder.util;

public class DefaultAliasGenerator implements AliasGenerator {
    private final Object lock = new Object();
    private char character = 'a' - 1;
    private int counter = 0;

    private DefaultAliasGenerator() {
    }

    private DefaultAliasGenerator(DefaultAliasGenerator generator) {
        this.character = generator.character;
        this.counter = generator.counter;
    }

    public static DefaultAliasGenerator newInstance() {
        return new DefaultAliasGenerator();
    }

    public static DefaultAliasGenerator of(DefaultAliasGenerator generator) {
        return new DefaultAliasGenerator(generator);
    }

    @Override
    public String current() {
        synchronized (lock) {
            String alias = String.valueOf(character < 'a' ? 'a' : character);
            return counter > 0 ? alias + counter : alias;
        }
    }

    @Override
    public String next() {
        synchronized (lock) {
            if (++character > 'z') {
                character = 'a';
                counter++;
            }

            return current();
        }
    }

    @Override
    public void reset() {
        synchronized (lock) {
            character = 'a' - 1;
            counter = 0;
        }
    }
}
