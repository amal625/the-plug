package thePlug;
import java.util.TreeSet;


/**
 * 
 * It doubles the underlying array when it is full and halves the underlying
 * array when it is one-quarter full.
 * 
 * @author from CS 62 Class page
 *
 */
public class BST<Key extends Comparable<Key>, Value> {
    private Node root; // Root of BST

    protected class Node {
        private Key key; // Sorted by key
        private Value val; // Associated value
        private Node left, right; // Roots of left and right subtrees
        private int size; // Number of nodes in subtree rooted at this node

        public Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    protected Value get(Key key) { //recursive implementation
        return get(root, key);
    }

    protected Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }

    //insert 1 creates new node or 2 updates existing node
    protected void insert(Key key, Value val) { //recursive implementation
        root = insert(root, key, val);
    }

    // helper (@returns root of subtree at x)
    protected Node insert(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1); //empty subtree, insert new node
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = insert(x.left, key, val);
        else if (cmp > 0) x.right = insert(x.right, key, val);
        else x.val = val; //update existing node
        x.size = size(x.left) + size(x.right) + 1; //update size
        return x;
    }
    
    protected void delete(Key key) { //recursive implementation
        root = delete(root, key);
    }
    
    //helper (@returns root of new subtree at x)
    protected Node delete(Node x, Key key) {
        if (x == null) return null; 
        //search part
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        //found the node, now the 3 cases
        else {
            if (x.right == null) return x.left; //1 & 2 - no or single child
            if (x.left == null) return x.right; 
            Node temp = x; //3. replace with successor 
            x = min(temp.right); //changes root to new successor - min key of right subtree
            x.right = deleteMin(temp.right); //new root right is old root's right side minus successor
            x.left = temp.left; //new root left is old root's left
        }
        x.size = size(x.left) + size(x.right) + 1; //recalculate size given size of subtrees plus self
        // decrements size because subtree (x.left / x.right) was probably set to null
        return x;
    }

    //get the minimum value of the subtree at x
    protected Node min(Node x) { 
        if (x.left == null) return x;
        return min(x.left);
    }

    //delete the minimum val
    protected Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;  //recalculate size given size of subtrees plus self
        return x;
    }

    protected int size(Node x) {
        return (x == null) ? 0 : x.size;
    }


    // public String toString() {
    //     return buildTreeString(root, 0);
    // }

    // protected String buildTreeString(Node node, int level) {
    //     if (node == null) return "";
    //     StringBuilder sb = new StringBuilder();
    //     sb.append(" ".repeat(level * 4)).append(node.key).append("\n");
    //     if (node.left != null || node.right != null) {
    //         sb.append(buildTreeString(node.left, level + 1));
    //         sb.append(buildTreeString(node.right, level + 1));
    //     }
    //     return sb.toString();
    // }

    public static void main(String[] args) {
        BST<String, Double> bst = new BST<>();
        
        bst.insert("Date", 3.49);
        bst.insert("Apple", 1.99);
        bst.insert("Elderberry", 4.99);
        bst.insert("Fig", 2.49);
        bst.insert("Banana", 0.99);
        bst.insert("Cherry", 2.99);
        bst.insert("Grape", 2.79);
        
        System.out.println("BST structure: \n" + bst);

        System.out.println("Price of Cherry: " + bst.get("Cherry")); // Should print 2.99
        System.out.println("Price of Fig: " + bst.get("Fig")); // Should print 2.49
        System.out.println("Price of Mango: " + bst.get("Mango")); // Should print "null"
        bst.delete("Cherry");    

        bst.delete("Date");    
        System.out.println("BST structure after deleting date: \n" + bst);
        
        bst.insert("Grape", 99.99);
        System.out.println("Updating grapes due to inflation: " + bst.get("Grape")); // Should print 99.99

    }
}
