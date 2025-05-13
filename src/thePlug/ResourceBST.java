package thePlug;

import java.security.Key;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Arrays;

public class ResourceBST implements ResourceBSTInterface<Resource>{
    private Node root; // Root of BST
    ArrayList<Resource> resources = new ArrayList<>();
    //could maybe make a get feature function, if they're 

    public class Node {
        private Resource element; // element for sorting, will be resource
        private Node left, right; // Roots of left and right subtrees
        private int size; // Number of nodes in subtree rooted at this node

        public Node(Resource element, int size) {
            this.element = element;
            this.size = size;
        }
    }

    public Resource search(String name) { //recursive implementation
        return search(root, name);
    }

    public Node getRoot(){
        return root;
    }
    
    protected Resource search(Node x, String name){
        if (x == null) return null;
        int cmp = name.compareTo(x.element.getName());
        if (cmp < 0) return search(x.left, name);
        else if (cmp > 0) return search(x.right, name);
        else return x.element;
    }
    

    public void insert(Resource element){ // inserts a new node
        root = insert(root, element);
        
    }

    // helper (@returns root of subtree at x)
    protected Node insert(Node x, Resource element) {
        if (x == null) return new Node(element, 1); //empty subtree, insert new node
        int cmp = element.compareTo(x.element);
        if (cmp < 0) x.left = insert(x.left, element);
        else if (cmp > 0) x.right = insert(x.right, element);
        else x.element = element; //update existing node
        x.size = size(x.left) + size(x.right) + 1; //update size
        return x;
    }

    public void delete(String name){ // deletes a node 
        root = delete(root, name);
    }

    public void deleteResource (Resource resource){
        delete(resource.getName());
    }

    //helper (@returns root of new subtree at x)
    protected Node delete(Node x, String name) {
        if (x == null) return null; 
        //search part
        int cmp = name.compareTo(x.element.getName());
        if (cmp < 0) x.left = delete(x.left, name);
        else if (cmp > 0) x.right = delete(x.right, name);
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

    //////////////////////
    //get the minimum value of the subtree at x
    protected Node min(Node x) { 
        if (x.left == null) return x;
        return min(x.left);
    }

    protected int size(Node x) {
        return (x == null) ? 0 : x.size;
    }

    //delete the minimum val
    protected Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;  //recalculate size given size of subtrees plus self
        return x;
    }
    /////////////////////

    // public Resource highestRated(){ //max rating
    //     return null;
    // }
    
    // public Resource lowestRated(){
    //     return null;
    // }
    
    public void inOrderTraversal(Node node, ArrayList<Resource> resources){  //in case we want to represent the data alphabetically 
        if (node.right == null && node.left == null){
            resources.add(node.element);
        } else if(node.left == null){
            resources.add(node.element);
            inOrderTraversal(node.right, resources);
        } else if(node.right == null){
            inOrderTraversal(node.left, resources);
            resources.add(node.element);
        }else{
            inOrderTraversal(node.left, resources);
            resources.add(node.element);
            inOrderTraversal(node.right, resources);
        }
              
    }


    public static void main(String[] args){
        // Resource r1 = new Resource("Apple", "pomona", "free", "item", "Mutual Aid", new ArrayList<String>(Arrays.asList("a", "b")), "None", "None"); // create new resource objects
        // Resource r2 = new Resource("Berry", "Claremont", "free","item", "Mutual Aid", new ArrayList<String>(Arrays.asList("a","b")), "None","None");
        // Resource r3 = new Resource("Cake", "pomona", "free", "item", "Mutual Aid", new ArrayList<String>(Arrays.asList("a", "b")), "None", "None");

        // ResourceBST rBST = new ResourceBST(); //add them to BST
        // rBST.insert(r3);
        // rBST.insert(r1);
        // rBST.insert(r2);
        
        // ArrayList<Resource> output = new ArrayList<>();
        // rBST.inOrderTraversal(rBST.root, output);
        // System.out.println(output);



        //////// TESTING SPLIT:::
        String s = "f,,two";
        String s1 = "f,blank";
        // Book Room,Pomona,Free,Item,Academic,zztofpzgjv@pomona.edu,,
        String s2 = "f,blank,,";
        String[] split = s2.split(",");
        System.out.println(Arrays.toString(split));
        System.out.println(split[1].equals(""));
        System.out.println(split[1].equals(" "));
        System.out.println(split[2].equals("two"));
        System.out.println(split[2].equals(" two"));


    }





    
}