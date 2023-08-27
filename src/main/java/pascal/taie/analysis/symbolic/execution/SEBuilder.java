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

package pascal.taie.analysis.symbolic.execution;

import pascal.taie.World;
import pascal.taie.analysis.ProgramAnalysis;
import pascal.taie.analysis.graph.callgraph.CallGraph;
import pascal.taie.analysis.graph.callgraph.CallGraphBuilder;
import pascal.taie.analysis.graph.callgraph.DefaultCallGraph;
import pascal.taie.analysis.graph.cfg.CFG;
import pascal.taie.analysis.graph.cfg.CFGBuilder;
import pascal.taie.analysis.graph.cfg.CFGEdge;
import pascal.taie.analysis.graph.icfg.ICFG;
import pascal.taie.analysis.graph.icfg.ICFGBuilder;
import pascal.taie.config.AnalysisConfig;
import pascal.taie.ir.IR;
import pascal.taie.ir.proginfo.MethodRef;
import pascal.taie.ir.stmt.Invoke;
import pascal.taie.ir.stmt.Stmt;
import pascal.taie.ir.stmt.If;
import pascal.taie.language.classes.JMethod;
import kala.collection.immutable.ImmutableHashSet;
import pascal.taie.util.AnalysisException;

import java.util.*;

public class SEBuilder extends ProgramAnalysis<Set<Stmt>> {

    // declare field ID
    public static final String ID = "se";

    public AnalysisConfig config;

    // implement constructor
    public SEBuilder(AnalysisConfig config) {
        super(config);
        this.config = config;
    }

    public List<Stmt> expandIf (CFG<Stmt> cfg, If stmt) {
        System.out.println(stmt.toString());
        List<Stmt> nodes = cfg.getNodes().stream().sorted(Comparator.comparing(Stmt::getIndex)).toList();
        return new ArrayList<>();
    }

    public void findPath(IR start, String end) {
        List<IR> path = new ArrayList<>();
        path.add(start);

        find(start, end, path, new HashMap<String, CFG<Stmt>>());

        System.out.println(path.size());
    }

    public void find(IR ir, String destination, List<IR> path, Map<String, CFG<Stmt>> isVisited) {
        System.out.println(ir.getMethod().toString());
        ICFG<JMethod, Stmt> icfg = World.get().getResult(ICFGBuilder.ID);

        String methodName = ir.getMethod().toString();
        if (methodName.contains(destination)) {
            System.out.println(path.size());
        } else {
//            isVisited.put(methodName, cfg);

            List<Stmt> nodes = ir.getStmts();
            List<Invoke> targetList = nodes.stream().filter(i -> i instanceof Invoke).map(i -> (Invoke) i).toList();
            try {
                List<IR> ttt = targetList.stream().map(i -> {
                    try {
                        return Optional.of(i.getMethodRef().resolve().getIR());
                    } catch (AnalysisException e) {
                        return Optional.<IR>empty();
                    }
                }).filter(Optional::isPresent).map(Optional::get).toList();

                for (IR irt: ttt) {
                    String irtMethodName = irt.getMethod().toString();
                    if (!isVisited.containsKey(irtMethodName)) {
                        path.add(irt);
                        System.out.println(path.size());
                        find(irt, destination, path, isVisited);

                        isVisited.remove(irtMethodName);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            isVisited.remove(methodName);
        }
    }

    // implement analyze(IR) method
    public Set<Stmt> analyze() {
        // obtain results of dependent analyses
//        CallGraph cg = ir.getResult(CallGraphBuilder.ID);
        String destination = "<com.alibaba.fastjson.serializer.ObjectArrayCodec: java.lang.Object deserialze(com.alibaba.fastjson.parser.DefaultJSONParser,java.lang.reflect.Type,java.lang.Object)>";
        JMethod mainm = World.get().getMainMethod();
//        CallGraph<Stmt, JMethod> cg = World.get().getResult(CallGraphBuilder.ID);
        DefaultCallGraph cg = World.get().getResult(CallGraphBuilder.ID);
        ICFG<JMethod, Stmt> icfg = World.get().getResult(ICFGBuilder.ID);

        var tt = cg.reachableMethods();

        findPath(mainm.getIR(), destination);



//        var list = icfg.getNodes(mainm).stream().map(icfg::getSuccsOf).toList();
//        var list2 = icfg.getNodes(mainm).stream().map(icfg::getOutEdgesOf).toList();

        // analysis logic

//        System.out.println(config);

//        var irs = ir.getMethod().toString();
//
//        if (irs.contains(mainm.toString())) {
//        if (irs.contains(destination)) {
//            findPath(ir, destination);
//        }

        return null;
    }
}
