package pascal.taie.analysis.symbolic.execution;

import java.util.Set;

public class Branch<N> extends AbstractPath<N> {
    public Set<AbstractPath<N>> next;
}
