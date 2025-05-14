package thePlug;
import java.util.Iterator;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.TreeSet;
import thePlug.ResourceBST.Node;

public interface ResourceBSTInterface<E>{
    void insert(E resource); // inserts a new node 
    void delete(String resourceName); // deletes a node 
    Resource search(String resourceName); // searches for a node 
    // E highestRated(); //max rating
    // E lowestRated(); //lowest rating
    // Comparator<E> comparator();//comparing, sorting
    // Comparator<Resource> byPrefixOrder(int r);
    // Comparator<Resource> byRating();
    // Iterator<E> iterator(); //iteration
    void inOrderTraversal(Node node, ArrayList<Resource> resources); //in case we want to represent the data alphabetically 
    
}
