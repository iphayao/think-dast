package com.phayao.thinkdast;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.*;

public class WikiNodeIterable implements Iterable<Node> {
    private Node root;

    public WikiNodeIterable(Node root) {
        this.root = root;
    }

    @Override
    public Iterator<Node> iterator() {
        return new WikiNodeIterator(root);
    }

    private class WikiNodeIterator implements Iterator<Node> {
        Deque<Node> stack;

        public WikiNodeIterator(Node node) {
            stack = new ArrayDeque<Node>();
            stack.push(node);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Node next() {
            // if the stack is empty, we're done
            if(stack.isEmpty()) {
                throw new NoSuchElementException();
            }
            // otherwise pop the next Node off the stack
            Node node = stack.pop();
            // push the children onto the stack in reverse order
            if(node instanceof Element) {
                List<Node> nodes = new ArrayList<Node>(node.childNodes());
                Collections.reverse(nodes);
                for (Node child : nodes) {
                    stack.push(child);
                }
            }
            return node;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();

        }
    }
}
