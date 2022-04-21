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

package pascal.taie.analysis.pta.plugin.util;

import pascal.taie.World;
import pascal.taie.analysis.pta.core.cs.element.CSObj;
import pascal.taie.ir.exp.ClassLiteral;
import pascal.taie.ir.exp.MethodHandle;
import pascal.taie.ir.exp.MethodType;
import pascal.taie.ir.exp.StringLiteral;
import pascal.taie.language.classes.ClassNames;
import pascal.taie.language.classes.JClass;
import pascal.taie.language.classes.JField;
import pascal.taie.language.classes.JMethod;
import pascal.taie.language.type.ArrayType;
import pascal.taie.language.type.ClassType;
import pascal.taie.language.type.Type;

import javax.annotation.Nullable;

/**
 * Static utility methods for {@link CSObjs}.
 */
public final class CSObjs {

    private CSObjs() {
    }

    /**
     * Converts a CSObj of string constant to corresponding String.
     * If the object is not a string constant, then return null.
     */
    public static @Nullable
    String toString(CSObj csObj) {
        Object alloc = csObj.getObject().getAllocation();
        return alloc instanceof StringLiteral ?
                ((StringLiteral) alloc).getString() : null;
    }

    /**
     * Converts a CSObj of class to corresponding JClass. If the object is
     * not a class constant, then return null.
     */
    public static @Nullable
    JClass toClass(CSObj csObj) {
        Object alloc = csObj.getObject().getAllocation();
        if (alloc instanceof ClassLiteral klass) {
            Type type = klass.getTypeValue();
            if (type instanceof ClassType) {
                return ((ClassType) type).getJClass();
            } else if (type instanceof ArrayType) {
                return World.get().getClassHierarchy()
                        .getJREClass(ClassNames.OBJECT);
            }
        }
        return null;
    }

    /**
     * Converts a CSObj of java.lang.reflect.Constructor to corresponding JMethod.
     * If the object does not represent a Constructor, then return null.
     */
    public static @Nullable
    JMethod toConstructor(CSObj csObj) {
        Object alloc = csObj.getObject().getAllocation();
        if (alloc instanceof JMethod method) {
            if (method.isConstructor()) {
                return method;
            }
        }
        return null;
    }

    /**
     * Converts a CSObj of java.lang.reflect.Method to corresponding JMethod.
     * If the object does not represent a Method, then return null.
     */
    public static @Nullable
    JMethod toMethod(CSObj csObj) {
        Object alloc = csObj.getObject().getAllocation();
        if (alloc instanceof JMethod method) {
            if (!method.isConstructor()) {
                return method;
            }
        }
        return null;
    }

    /**
     * Converts a CSObj of java.lang.reflect.Method to corresponding JMethod.
     * If the object does not represent a Method, then return null.
     */
    public static @Nullable
    JField toField(CSObj csObj) {
        Object alloc = csObj.getObject().getAllocation();
        return alloc instanceof JField ? (JField) alloc : null;
    }

    /**
     * Converts a CSObj of class to corresponding type. If the object is
     * not a class constant, then return null.
     */
    public static @Nullable
    Type toType(CSObj csObj) {
        Object alloc = csObj.getObject().getAllocation();
        return alloc instanceof ClassLiteral ?
                ((ClassLiteral) alloc).getTypeValue() : null;
    }

    /**
     * Converts a CSObj of MethodType to corresponding MethodType.
     * If the object is not a MethodType, then return null.
     */
    public static @Nullable
    MethodType toMethodType(CSObj csObj) {
        Object alloc = csObj.getObject().getAllocation();
        return alloc instanceof MethodType ? (MethodType) alloc : null;
    }

    /**
     * Converts a CSObj of MethodHandle constant to corresponding MethodHandle.
     * If the object is not a MethodHandle constant, then return null.
     */
    public static @Nullable
    MethodHandle toMethodHandle(CSObj csObj) {
        Object alloc = csObj.getObject().getAllocation();
        return alloc instanceof MethodHandle ? (MethodHandle) alloc : null;
    }
}
