/*
 * Copyright 2015 the original author or authors.
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
package io.koara.ast;

import io.koara.renderer.Renderer;

public abstract class Node {

    private Node parent;
    private Node[] children;
    private Object value;

    public void add(Node n, int i) {
        if (children == null) {
            children = new Node[i + 1];
        }
        children[i] = n;
    }

    public void childrenAccept(Renderer renderer) {
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                children[i].accept(renderer);
            }
        }
    }

    public abstract void accept(Renderer renderer);

    public Node[] getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}