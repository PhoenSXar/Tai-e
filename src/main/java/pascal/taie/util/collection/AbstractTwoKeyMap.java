/*
 * Tai-e: A Static Analysis Framework for Java
 *
 * Copyright (C) 2022 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2022 Yue Li <yueli@nju.edu.cn>
 *
 * This file is part of Tai-e.
 *
 * Tai-e is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Tai-e is distributed in the hope that it will be useful,but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Tai-e. If not, see <https://www.gnu.org/licenses/>.
 */

package pascal.taie.util.collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractTwoKeyMap<K1, K2, V> implements
        TwoKeyMap<K1, K2, V> {

    protected static final String NULL_KEY = "TwoKeyMap does not permit null keys";

    protected static final String NULL_VALUE = "TwoKeyMap does not permit null values";

    @Override
    public boolean containsKey(K1 key1, K2 key2) {
        return get(key1, key2) != null;
    }

    @Override
    public boolean containsKey(K1 key1) {
        return get(key1) != null;
    }

    @Override
    public boolean containsValue(V value) {
        for (K1 key1 : keySet()) {
            //noinspection ConstantConditions
            if (get(key1).containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public V get(K1 key1, K2 key2) {
        Map<K2, V> map = get(key1);
        return map == null ? null : map.get(key2);
    }

    private Set<TwoKeyMap.Entry<K1, K2, V>> entrySet;

    @Override
    public Set<TwoKeyMap.Entry<K1, K2, V>> entrySet() {
        var es = entrySet;
        if (es == null) {
            es = Collections.unmodifiableSet(new EntrySet());
            entrySet = es;
        }
        return es;
    }

    private final class EntrySet extends AbstractSet<TwoKeyMap.Entry<K1, K2, V>> {

        @Override
        public boolean contains(Object o) {
            if (o instanceof Entry<?, ?, ?> entry) {
                //noinspection unchecked
                V v = AbstractTwoKeyMap.this.get(
                        (K1) entry.key1(), (K2) entry.key2());
                return Objects.equals(entry.value(), v);
            }
            return false;
        }

        @Nonnull
        @Override
        public Iterator<TwoKeyMap.Entry<K1, K2, V>> iterator() {
            return entryIterator();
        }

        @Override
        public int size() {
            return AbstractTwoKeyMap.this.size();
        }
    }

    protected abstract Iterator<TwoKeyMap.Entry<K1, K2, V>> entryIterator();

    private Set<KeyPair<K1, K2>> twoKeySet;

    @Override
    public Set<KeyPair<K1, K2>> keyPairSet() {
        Set<KeyPair<K1, K2>> set = twoKeySet;
        if (set == null) {
            set = Views.toMappedSet(entrySet(),
                    e -> new KeyPair<>(e.key1(), e.key2()),
                    o -> {
                        if (o instanceof KeyPair<?, ?> pair) {
                            return containsKey((K1) pair.key1(),
                                    (K2) pair.key2());
                        }
                        return false;
                    });
        }
        return set;
    }

    private Collection<V> values;

    @Override
    public Collection<V> values() {
        Collection<V> vals = values;
        if (vals == null) {
            vals = Views.toMappedCollection(entrySet(), Entry::value,
                    o -> containsValue((V) o));
            values = vals;
        }
        return vals;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TwoKeyMap)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        var that = (TwoKeyMap<K1, K2, V>) o;
        if (size() != that.size()) {
            return false;
        }
        try {
            for (var e : entrySet()) {
                K1 key1 = e.key1();
                K2 key2 = e.key2();
                V value = e.value();
                if (!Objects.equals(value, that.get(key1, key2))) {
                    return false;
                }
            }
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        if (!isEmpty()) {
            sb.append('\n');
        }
        for (var e : entrySet()) {
            K1 key1 = e.key1();
            K2 key2 = e.key2();
            V value = e.value();
            sb.append("  ")
                    .append(key1).append(',')
                    .append(key2).append('=')
                    .append(value).append('\n');
        }
        sb.append('}');
        return sb.toString();
    }
}
