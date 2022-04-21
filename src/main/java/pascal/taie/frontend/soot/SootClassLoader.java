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

package pascal.taie.frontend.soot;

import pascal.taie.World;
import pascal.taie.language.classes.ClassHierarchy;
import pascal.taie.language.classes.JClass;
import pascal.taie.language.classes.JClassLoader;
import soot.Scene;
import soot.SootClass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class SootClassLoader implements JClassLoader {

    private static final ClassDumper classDumper = new ClassDumper();

    private final Scene scene;

    private final ClassHierarchy hierarchy;

    private Converter converter;

    private final Map<String, JClass> classes = new HashMap<>(1024);

    SootClassLoader(Scene scene, ClassHierarchy hierarchy) {
        this.scene = scene;
        this.hierarchy = hierarchy;
    }

    @Override
    public JClass loadClass(String name) {
        JClass jclass = classes.get(name);
        if (jclass == null) {
            // TODO: confirm if this API is suitable
            SootClass sootClass = scene.getSootClassUnsafe(name, false);
            if (sootClass != null && !sootClass.isPhantom()) {
                // TODO: handle phantom class appropriately
                jclass = new JClass(this, sootClass.getName(),
                        sootClass.moduleName);
                // New class must be put into classes map at first,
                // at build(jclass) may also trigger the loading of
                // the new created class. Not putting the class into classes
                // may cause infinite recursion.
                classes.put(name, jclass);
                new SootClassBuilder(converter, sootClass)
                        .build(jclass);
                hierarchy.addClass(jclass);
                if (World.get().getOptions().isDumpClasses()) {
                    classDumper.dump(sootClass);
                }
            }
        }
        // TODO: add warning for missing classes
        return jclass;
    }

    @Override
    public Collection<JClass> getLoadedClasses() {
        return classes.values();
    }

    void setConverter(Converter converter) {
        this.converter = converter;
    }
}
