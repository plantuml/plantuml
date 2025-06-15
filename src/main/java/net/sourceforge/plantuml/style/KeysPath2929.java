/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.style;

import java.util.Collections;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

public final class KeysPath2929 {

    private final SortedSet<String> keys;

    private KeysPath2929(SortedSet<String> keys) {
        this.keys = Collections.unmodifiableSortedSet(new TreeSet<>(Objects.requireNonNull(keys)));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final SortedSet<String> keys = new TreeSet<>();

        private Builder() { }

        public Builder add(String key) {
            keys.add(Objects.requireNonNull(key));
            return this;
        }

        public KeysPath2929 build() {
            return new KeysPath2929(keys);
        }
    }

    public KeysPath2929 with(String keyToBeAdded) {
        Objects.requireNonNull(keyToBeAdded);
        TreeSet<String> newKeys = new TreeSet<>(this.keys);
        newKeys.add(keyToBeAdded);
        return new KeysPath2929(newKeys);
    }

    public SortedSet<String> getKeys() {
        return keys;
    }

    public int size() {
        return keys.size();
    }

    public boolean contains(String key) {
        return keys.contains(key);
    }

    @Override
    public String toString() {
        return keys.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof KeysPath2929)) return false;
        KeysPath2929 other = (KeysPath2929) obj;
        return keys.equals(other.keys);
    }

    @Override
    public int hashCode() {
        return keys.hashCode();
    }
}

