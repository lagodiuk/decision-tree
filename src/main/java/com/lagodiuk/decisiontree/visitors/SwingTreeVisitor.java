/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Yurii Lahodiuk (yura_lagodiuk@ukr.net)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package com.lagodiuk.decisiontree.visitors;

import com.lagodiuk.decisiontree.DecisionTree;
import com.lagodiuk.decisiontree.DecisionTreeVisitor;
import com.lagodiuk.decisiontree.Predicate;

import javax.swing.tree.DefaultMutableTreeNode;

public class SwingTreeVisitor implements DecisionTreeVisitor {

    private DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    @Override
    public void visit(DecisionTree tree) {
        if (tree.getRule() != null) {
            String description;

            Predicate predicate = tree.getRule().getPredicate();
            String attribute = tree.getRule().getAttribute();
            Object value = tree.getRule().getSampleValue();
            switch (predicate) {
                case EQUAL:
                    description = attribute + " == " + value;
                    break;

                case EXISTS:
                    description = "exists " + attribute;
                    break;

                case GTE:
                    description = attribute + " >= " + value;
                    break;

                case LTE:
                    description = attribute + " =< " + value;
                    break;

                default:
                    description = attribute + predicate.toString() + value;
                    break;
            }

            this.root.setUserObject(description);
            this.root.add( buildSwingTree( tree.getMatchSubTree() ) );
            this.root.add( buildSwingTree( tree.getNotMatchSubTree() ) );
        } else {
            this.root.setUserObject(tree.getCategory().toString());
        }
    }

    public DefaultMutableTreeNode getRoot() {
        return this.root;
    }

    public static DefaultMutableTreeNode buildSwingTree( DecisionTree tree ) {
        SwingTreeVisitor visitor = new SwingTreeVisitor();
        tree.accept( visitor );
        return visitor.getRoot();
    }
}
