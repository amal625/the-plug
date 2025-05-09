package thePlug;

import java.security.Key;

public class ResourceBST implements ResourceBSTInterface<Resource>{
    BST<String, Resource> bst = new BST(); // new tree made period.

    public void insert(Resource resource){ // inserts a new node
        bst.insert(resource.getName(), resource);
    }

    public void delete(Resource resource){ // deletes a node 

    }

    public boolean search(Resource resource){ // searches for a node 
        return false;
    }

    public Resource highestRated(){ //max rating
        return null;
        
    }
    public Resource lowestRated(){
        return null;
    }
    
    public void inorderTraversal(){  //in case we want to represent the data alphabetically 
        
    }
    
}