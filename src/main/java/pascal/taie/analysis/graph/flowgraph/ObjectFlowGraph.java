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

package pascal.taie.analysis.graph.flowgraph;

import pascal.taie.analysis.graph.callgraph.CallGraph;
import pascal.taie.analysis.graph.callgraph.CallKind;
import pascal.taie.analysis.pta.core.cs.element.ArrayIndex;
import pascal.taie.analysis.pta.core.cs.element.CSVar;
import pascal.taie.analysis.pta.core.cs.element.InstanceField;
import pascal.taie.analysis.pta.core.cs.element.Pointer;
import pascal.taie.analysis.pta.core.cs.element.StaticField;
import pascal.taie.analysis.pta.core.solver.PointerFlowGraph;
import pascal.taie.ir.exp.InvokeInstanceExp;
import pascal.taie.ir.exp.Var;
import pascal.taie.ir.stmt.Invoke;
import pascal.taie.language.classes.JMethod;
import pascal.taie.util.Indexer;
import pascal.taie.util.collection.IndexMap;
import pascal.taie.util.collection.Maps;
import pascal.taie.util.collection.MultiMap;
import pascal.taie.util.collection.Views;
import pascal.taie.util.graph.Graph;

import java.util.Set;

public class ObjectFlowGraph extends NodeManager
        implements Graph<Node>, Indexer<Node> {

    private final MultiMap<Node, Edge> inEdges = Maps.newMultiMap(
            new IndexMap<>(this, 4096));

    private final MultiMap<Node, Edge> outEdges = Maps.newMultiMap(
            new IndexMap<>(this, 4096));

    public ObjectFlowGraph(PointerFlowGraph pfg, CallGraph<Invoke, JMethod> callGraph) {
        pfg.getNodes()
                .stream()
                .map(pfg::getOutEdgesOf)
                .flatMap(Set::stream)
                .forEach(e -> addEdge(e.getKind(),
                        toNode(e.source()), toNode(e.target())));
        callGraph.edges()
                .forEach(e -> {
                    if (e.getKind() != CallKind.OTHER &&
                            e.getCallSite().getInvokeExp() instanceof
                                    InvokeInstanceExp invokeExp) {
                        Var base = invokeExp.getBase();
                        Var thisVar = e.getCallee().getIR().getThis();
                        addEdge(FlowKind.THIS_PASSING,
                                getOrCreateVarNode(base),
                                getOrCreateVarNode(thisVar));
                    }
                });
    }

    private void addEdge(FlowKind kind, Node source, Node target) {
        BasicEdge edge = new BasicEdge(kind, source, target);
        outEdges.put(source, edge);
        inEdges.put(target, edge);
    }

    private Node toNode(Pointer pointer) {
        if (pointer instanceof CSVar csVar) {
            return getOrCreateVarNode(csVar.getVar());
        } else if (pointer instanceof InstanceField iField) {
            return getOrCreateInstanceFieldNode(
                    iField.getBase().getObject(), iField.getField());
        } else if (pointer instanceof ArrayIndex arrayIndex) {
            return getOrCreateArrayIndexNode(
                    arrayIndex.getArray().getObject());
        } else {
            return getOrCreateStaticFieldNode(
                    ((StaticField) pointer).getField());
        }
    }

    @Override
    public boolean hasEdge(Node source, Node target) {
        return getSuccsOf(source).contains(target);
    }

    @Override
    public Set<Node> getPredsOf(Node node) {
        return Views.toMappedSet(getInEdgesOf(node), Edge::source);
    }

    @Override
    public Set<Edge> getInEdgesOf(Node node) {
        return inEdges.get(node);
    }

    @Override
    public Set<Node> getSuccsOf(Node node) {
        return Views.toMappedSet(getOutEdgesOf(node), Edge::target);
    }

    @Override
    public Set<Edge> getOutEdgesOf(Node node) {
        return outEdges.get(node);
    }
}