/*
 * Tai-e: A Static Analysis Framework for Java
 *
 * Copyright (C) 2020-- Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020-- Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * Tai-e is only for educational and academic purposes,
 * and any form of commercial use is disallowed.
 * Distribution of Tai-e is disallowed without the approval.
 */

package pascal.taie.analysis.oldpta.ir;

import pascal.taie.analysis.graph.callgraph.CallKind;
import pascal.taie.ir.proginfo.MethodRef;
import pascal.taie.language.classes.JMethod;

import java.util.List;

/**
 * All implementations of CallSite should inherit this class.
 */
public abstract class AbstractCallSite implements CallSite {

    protected final CallKind kind;
    protected Call call;
    protected MethodRef methodRef;
    protected Variable receiver;
    protected List<Variable> args = List.of();
    protected JMethod containerMethod;

    protected AbstractCallSite(CallKind kind) {
        this.kind = kind;
    }

    @Override
    public CallKind getKind() {
        return kind;
    }

    @Override
    public void setCall(Call call) {
        this.call = call;
        if (kind != CallKind.STATIC) {
            receiver.addCall(call);
        }
    }

    @Override
    public Call getCall() {
        return call;
    }

    @Override
    public MethodRef getMethodRef() {
        return methodRef;
    }

    @Override
    public Variable getReceiver() {
        assert kind != CallKind.STATIC;
        return receiver;
    }

    @Override
    public int getArgCount() {
        return args.size();
    }

    @Override
    public Variable getArg(int i) {
        return args.get(i);
    }

    @Override
    public JMethod getContainerMethod() {
        return containerMethod;
    }
}
