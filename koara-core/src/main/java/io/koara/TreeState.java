/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.koara;

import java.util.ArrayList;
import java.util.List;

import io.koara.ast.Node;

public class TreeState {

    private List<Node> nodes;
    private List<Integer> marks;
    private int nodesOnStack;
    private int currentMark;

    public TreeState() {
        nodes = new ArrayList<Node>();
        marks = new ArrayList<Integer>();
        nodesOnStack = 0;
        currentMark = 0;
    }

    public void openScope() {
        marks.add(currentMark);
        currentMark = nodesOnStack;
    }

    public void closeScope(Node n) {
        int a = nodeArity();
        currentMark = marks.remove(marks.size() - 1);
        while (a-- > 0) {
            Node c = popNode();
            c.setParent(n);
            n.add(c, a);
        }
        pushNode(n);
    }

    public void addSingleValue(Node n, Token t) {
        openScope();
        n.setValue(t.image);
        closeScope(n);
    }

    private int nodeArity() {
        return nodesOnStack - currentMark;
    }

    private Node popNode() {
        --nodesOnStack;
        Node n = nodes.remove(nodes.size() - 1);
        return n;
    }

    private void pushNode(Node n) {
        nodes.add(n);
        ++nodesOnStack;
    }

}