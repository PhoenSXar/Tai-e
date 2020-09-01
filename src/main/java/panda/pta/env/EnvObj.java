/*
 * Panda - A Program Analysis Framework for Java
 *
 * Copyright (C) 2020 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020 Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * This software is designed for the "Static Program Analysis" course at
 * Nanjing University, and it supports a subset of Java features.
 * Panda is only for educational and academic purposes, and any form of
 * commercial use is disallowed.
 */

package panda.pta.env;

import panda.pta.element.AbstractObj;
import panda.pta.element.Method;
import panda.pta.element.Type;

import java.util.Objects;
import java.util.Optional;

/**
 * Objects managed/created by Java runtime environment.
 */
public class EnvObj extends AbstractObj {

    /**
     * Description of this object.
     */
    private final String name;
    private final Method containerMethod;

    public EnvObj(String name, Type type, Method containerMethod) {
        super(type);
        this.name = name;
        this.containerMethod = containerMethod;
    }

    @Override
    public Kind getKind() {
        return Kind.ENV;
    }

    @Override
    public String getAllocation() {
        return name;
    }

    @Override
    public Optional<Method> getContainerMethod() {
        return Optional.ofNullable(containerMethod);
    }

    @Override
    public Type getContainerType() {
        // TODO: set a better container type?
        return containerMethod != null
                ? containerMethod.getClassType()
                : type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvObj envObj = (EnvObj) o;
        return name.equals(envObj.name)
                && Objects.equals(containerMethod, envObj.containerMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, containerMethod);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Env]");
        if (containerMethod != null) {
            sb.append(containerMethod).append("/");
        }
        sb.append(name);
        return sb.toString();
    }
}