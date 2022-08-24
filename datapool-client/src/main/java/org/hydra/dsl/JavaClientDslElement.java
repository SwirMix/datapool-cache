package org.hydra.dsl;

import org.apache.jorphan.collections.HashTree;
import us.abstracta.jmeter.javadsl.core.BuildTreeContext;
import us.abstracta.jmeter.javadsl.core.DslTestElement;

public class JavaClientDslElement implements DslTestElement {
    @Override
    public HashTree buildTreeUnder(HashTree hashTree, BuildTreeContext buildTreeContext) {
        return null;
    }

    @Override
    public void showInGui() {

    }
}
