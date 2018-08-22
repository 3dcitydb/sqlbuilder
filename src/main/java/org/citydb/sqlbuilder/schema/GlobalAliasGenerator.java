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

package org.citydb.sqlbuilder.schema;

import java.util.concurrent.locks.ReentrantLock;

public class GlobalAliasGenerator implements AliasGenerator {
	private static GlobalAliasGenerator instance = null;

	private final ReentrantLock lock = new ReentrantLock();
	private final AliasGenerator aliasGenerator = new DefaultAliasGenerator();

	private GlobalAliasGenerator() {
		// just to thwart instantiation
	}

	public static synchronized GlobalAliasGenerator getInstance() {
		if (instance == null)
			instance = new GlobalAliasGenerator();

		return instance;
	}

	@Override
	public String currentAlias() {
		lock.lock();
		try {
			return aliasGenerator.currentAlias();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String nextAlias() {
		lock.lock();
		try {
			return aliasGenerator.nextAlias();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void reset() {
		lock.lock();
		try {
			aliasGenerator.reset();
		} finally {
			lock.unlock();
		}
	}

}
