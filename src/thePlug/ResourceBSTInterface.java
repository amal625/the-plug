package thePlug;
import java.util.Iterator;
import java.util.Comparator;

public interface ResourceBSTInterface<E>{
    void insert(E resource); // inserts a new node 
    void delete(String resourceName); // deletes a node 
    Resource search(String resourceName); // searches for a node 
    E highestRated(); //max rating
    E lowestRated(); //lowest rating
    // Comparator<E> comparator();//comparing, sorting
    // Comparator<Resource> byPrefixOrder(int r);
    // Comparator<Resource> byRating();
    // Comparator<Resource> by 
    // Iterator<E> iterator(); //iteration
    void inorderTraversal(); //in case we want to represent the data alphabetically 
    
}
