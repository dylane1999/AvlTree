//package com.hwcompsci2;

public class AVLTreeMap implements Map {

    private Node Lastnode;

    class Node {
        String key;
        String value;
        int height;
        Node parent;
        Node left;
        Node right;
        private int size; // number of nodes in subtree

        public Node(String key, String value, int height, int size) {
            this.key = key;
            this.value = value;
           this.height = height;
            this.parent = null;
            this.left = null;
            this.right = null;
           this.size = size;
        }

        public int balance() {
            return height(this.left) - height(this.right);
        }
    }

    public AVLTreeMap() {
        root = null;
    }

   // private int size;
    private Node root;

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null)
            return 0;
        return node.size;
    }


    public int height(Node x) {
        if (x == null)
            return -1;
        return x.height;
    }


    /* computes number of nodes in tree */


    public boolean put(String key, String value) {
        if (key == null)
            throw new IllegalArgumentException("first argument to put() is null");
        if (value == null) {
            return true;
        }
        root = put(root, key, value);
        return false;
    }


    private Node put(Node node, String key, String value) {
        String side;
        if (node == null){
           Node newnode =  new Node(key, value, 0, 1);
           newnode.parent = Lastnode;
            return newnode;
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) { //key is less than root
            side = "left";
            Lastnode = node;
            node.left = put(node.left, key, value);
        } else if (cmp > 0) { //key is greater than root
            Lastnode = node;
            side = "right";
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }
        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    private Node balance(Node node) {
        if (node.balance() < -1) {
            if (node.right.balance() > 0) {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        } else if (node.balance() > 1) {
            if (node.left.balance() < 0) {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);
        }
        return node;
    }


    private Node rotateRight(Node node) {
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;
        temp.size = node.size;
        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        temp.height = 1 + Math.max(height(temp.left), height(temp.right));
        return temp;
    }


    private Node rotateLeft(Node node) {
        Node rotated = node.right;
        node.right = rotated.left;
        rotated.left = node;
        rotated.size = node.size;
        node.size = 1 + size(node.left) + size(node.right);
        node.height = 1 + Math.max(height(node.left), height(node.right));
        rotated.height = 1 + Math.max(height(rotated.left), height(rotated.right));
        return rotated;
    }


    public String get(String key) {
        if (key == null)
            throw new IllegalArgumentException("argument to get() is null");
        Node x = get(root, key);
        if (x == null)
            return null;
        return x.value;
    }

    private Node get(Node x, String key) {
        if (x == null)
            return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            return get(x.left, key);
        else if (cmp > 0)
            return get(x.right, key);
        else
            return x;
    }

    public void clear() {
       root = null;
    }

    public void print() {
        this.print(this.root, "", 0);
    }

    private void print(Node node, String prefix, int depth) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
        if (!prefix.equals("")) {
            System.out.print(prefix);
            System.out.print(":");
        }
        System.out.print(node.key);
        System.out.print(" (");
        System.out.print("H:");
        System.out.print(node.height);
        System.out.println(")");
        this.print(node.left, "L", depth + 1);
        this.print(node.right, "R", depth + 1);
    }

    public String preorderString() {
        // DO NOT CHANGE THIS FUNCTION
        return this.preorderString(this.root);
    }

    private String preorderString(Node node) {
        // DO NOT CHANGE THIS FUNCTION
        if (node == null) {
            return "()";
        }
        return "(" + node.key + " "
                + this.preorderString(node.left) + " "
                + this.preorderString(node.right) + ")";
    }

    public static void main(String[] args) {
        AVLTreeMap map = new AVLTreeMap();
        String[] keys = {"7", "9", "6", "0", "4", "2", "1"};
        String[] values = {"seven", "nine", "six", "zero", "four", "two", "one"};

        // insert all keys
        for (int i = 0; i < keys.length; i++) {
            boolean exists = map.put(keys[i], values[i]);
            if (exists) {
                System.out.println("Failed to insert key " + keys[i] + " and value " + values[i]);
                return;
            }
        }

        // check size
        if (map.size() != keys.length) {
            System.out.println(
                    "Map should have size() = " + Integer.toString(keys.length) + " after insertion of numbers "
                            + "but had size " + Integer.toString(map.size()) + " instead"
            );
            return;
        }

        // retrieve all keys and check their values
        for (int i = 0; i < keys.length; i++) {
            String value = map.get(keys[i]);
            if (!value.equals(values[i])) {
                System.out.println(
                        "Expected " + values[i] + " from retrieve key " + keys[i] + " "
                                + "got " + value + " instead"
                );
            }
        }

        map.clear();

        // check size
        if (map.size() != 0) {
            System.out.println(
                    "Map should have size() = 0 after clear() "
                            + "but had size " + Integer.toString(map.size()) + " instead"
            );
            return;
        }

        map.put("doe", "A deer, a female deer.");
        map.put("ray", "A drop of golden sun.");
        map.put("me", "A name I call myself.");
        map.put("far", "A long long way to run.");

        // check size
        if (map.size() != 4) {
            System.out.println(
                    "Map should have size() = 4 after insertion of musical keys "
                            + "but had size " + Integer.toString(map.size()) + " instead"
            );
            return;
        }

        if (!map.get("ray").equals("A drop of golden sun.")) {
            System.out.println(
                    "Expected \"A drop of golden sun.\" from retrieve key \"ray\" "
                            + "got \"" + map.get("ray") + "\" instead"
            );
            return;
        }

        return;
    }
}

