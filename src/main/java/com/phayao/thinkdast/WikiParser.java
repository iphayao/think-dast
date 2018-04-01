package com.phayao.thinkdast;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class WikiParser {
    // the list of paragraphs we should search
    private Elements paragraphs;

    // the stack of open delimiters
    // TODO: consider simplifying this by counting parentheses
    private Deque<String> parenthesisStack;

    /**
     * Initializer a WikiParser with a list of Elements.
     * @param paragraphs
     */
    public WikiParser(Elements paragraphs) {
        this.paragraphs = paragraphs;
        this.parenthesisStack = new ArrayDeque<String>();
    }

    /**
     * Searches the paragraphs for a valid link.
     *
     * Warns if a paragraph ends with unbalanced parentheses.
     *
     * @return
     */
    public Element findFirstLink() {
        for(Element paragraph: paragraphs) {
            Element firstLink = findFirstLinkPara(paragraph);
            if(firstLink != null) {
                return firstLink;
            }
            if(!parenthesisStack.isEmpty()) {
                System.out.println("Warning: unbalanced parentheses");
            }
        }
        return null;
    }

    /**
     * Returns the first valid link in a paragraph, or null.
     *
     * @param root
     * @return
     */
    private Element findFirstLinkPara(Node root) {
        // create an Iterable that traverses the tree
        Iterable<Node> nodes = new WikiNodeIterable(root);

        // loop through the nodes
        for(Node node: nodes) {
            // process TextNodes to get parentheses
            if(node instanceof TextNode) {
                processTextNode((TextNode)node);
            }
            // process elements to get find links
            if(node instanceof Element) {
                Element firstLink = processElement((Element)node);
                if(firstLink != null) {
                    return firstLink;
                }
            }
        }
        return null;
    }

    /**
     * Returns the element if it's a valid link, null otherwise.
     * @param element
     * @return
     */
    private Element processElement(Element element) {
        if(validLink(element)) {
            return element;
        }
        return null;
    }

    /**
     * Checks wheters a link is value.
     * @param element
     * @return
     */
    private boolean validLink(Element element) {
        // it's no good if it's not link
        if(!element.tagName().equals("a")) {
            return false;
        }
        // in italics
        if(isItalic(element)) {
            return false;
        }
        // in parenthesis
        if(isInparens(element)) {
            return false;
        }
        if(startWith(element, "#")) {
            return false;
        }
        if(startWith(element, "/en.wikipedia.org/wiki/Help:")) {
            return false;
        }
        return true;
    }

    /**
     * Checks whether a link starts with a given String.
     * @param element
     * @param s
     * @return
     */
    private boolean startWith(Element element, String s) {
        return (element.attr("href").startsWith(s));
    }

    /**
     * Check whether the element is in parentheses
     * @param element
     * @return
     */
    private boolean isInparens(Element element) {
        return !parenthesisStack.isEmpty();
    }

    /**
     * Checks whether the elements is in italics.
     * @param element
     * @return
     */
    private boolean isItalic(Element element) {
        for(Element e = element; e != null; e = e.parent()) {
            if(e.tagName().equals("i") || e.tagName().equals("em")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Process a text node, splitting it up and checking parentheses.
     * @param node
     */
    private void processTextNode(TextNode node) {
        StringTokenizer st = new StringTokenizer(node.text(), " ()", true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if(token.equals("(")) {
                parenthesisStack.push(token);
            }
            if(token.equals(")")) {
                if(parenthesisStack.isEmpty()) {
                    System.out.println("Warning: unbalanced parentheses.");
                }
                parenthesisStack.pop();
            }
        }
    }

}
