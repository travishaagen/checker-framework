package checkers.flow.cfg.node;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import checkers.flow.util.HashCodeUtils;
import checkers.util.InternalUtils;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.util.TreePath;

/**
 * A node for method invocation
 * 
 * <pre>
 *   <em>target(arg1, arg2, ...)</em>
 * </pre>
 * 
 * CFGs may contain {@link MethodInvocationNode}s that correspond to no AST
 * {@link Tree}, in which case, the tree field will be null.
 * 
 * @author Stefan Heule
 * @author Charlie Garrett
 * 
 */
public class MethodInvocationNode extends Node {

    protected MethodInvocationTree tree;
    protected MethodAccessNode target;
    protected List<Node> arguments;
    protected TreePath treePath;

    public MethodInvocationNode(MethodInvocationTree tree,
            MethodAccessNode target, List<Node> arguments, TreePath treePath) {
        super(tree != null ? InternalUtils.typeOf(tree) : target.getMethod().getReturnType());
        this.tree = tree;
        this.target = target;
        this.arguments = arguments;
        this.treePath = treePath;
    }

    public MethodInvocationNode(MethodAccessNode target, List<Node> arguments,
            TreePath treePath) {
        this(null, target, arguments, treePath);
    }

    public MethodAccessNode getTarget() {
        return target;
    }

    public List<Node> getArguments() {
        return arguments;
    }

    public Node getArgument(int i) {
        return arguments.get(i);
    }

    public TreePath getTreePath() {
        return treePath;
    }

    @Override
    public MethodInvocationTree getTree() {
        return tree;
    }

    @Override
    public <R, P> R accept(NodeVisitor<R, P> visitor, P p) {
        return visitor.visitMethodInvocation(this, p);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(target);
        sb.append("(");
        boolean needComma = false;
        for (Node arg : arguments) {
            if (needComma) {
                sb.append(", ");
            }
            sb.append(arg);
            needComma = true;
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof MethodInvocationNode)) {
            return false;
        }
        MethodInvocationNode other = (MethodInvocationNode) obj;

        return getTarget().equals(other.getTarget())
                && getArguments().equals(other.getArguments());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash = HashCodeUtils.hash(target);
        for (Node arg : arguments) {
            hash = HashCodeUtils.hash(hash, arg.hashCode());
        }
        return hash;
    }

    @Override
    public Collection<Node> getOperands() {
        LinkedList<Node> list = new LinkedList<Node>();
        list.add(target);
        list.addAll(arguments);
        return list;
    }
}
