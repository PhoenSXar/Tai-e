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

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ViewsTest {

    @Test
    public void testFilteredCollection() {
        Collection<Integer> view = Views.toFilteredCollection(
                List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), n -> n % 2 == 0);
        IntStream.of(1, 3, 5, 7, 9).forEach(
                n -> assertFalse(view.contains(n)));
        assertEquals(4, view.size());
    }

    @Test
    public void testCombinedSet() {
        Set<Integer> view = Views.toCombinedSet(
                Set.of(1, 3, 5, 7, 9), Set.of(2, 4, 6, 8, 10));
        IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).forEach(
                n -> assertTrue(view.contains(n)));
        assertEquals(10, view.size());
        int total = 0;
        for (int n : view) {
            total += n;
        }
        assertEquals(55, total);
    }
}
