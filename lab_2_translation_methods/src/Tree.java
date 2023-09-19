import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tree {
    public final String node;
    public final List<Tree> children;

    public Tree(String node, Tree... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }

    public Tree(String node) {
        this.node = node;
        this.children = Collections.emptyList();
    }
}

