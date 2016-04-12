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
package io.koara.ast;

import io.koara.Renderer;

public class BlockElement extends Node {

	public boolean hasChildren() {
		return getChildren() != null && getChildren().length > 0;
	}
	
	public boolean isFirstChild() {
		return getParent().getChildren()[0] == this;
	}
	
	public boolean isLastChild() {
		Node[] children = getParent().getChildren();
		return children[children.length - 1] == this;
	}
	
    public boolean isNested() {
        return !(getParent() instanceof Document);
    }

    public boolean isSingleChild() {
        return ((Node) this.getParent()).getChildren().length == 1;
    }
    
    public Object next() {
    	for(int i = 0; i < getParent().getChildren().length - 1; i++) {
    		if(getParent().getChildren()[i] == this) {
    			return getParent().getChildren()[i + 1];
    		}
    	}
    	
    	
    	return null;
    }

    @Override
    public void accept(Renderer renderer) {
        renderer.visit(this);
    }

}
