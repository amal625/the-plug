package thePlug;
import java.util.Iterator;
import java.util.Comparator;

public interface ResourceBSTInterface<E>{
    void insert(E resource); // inserts a new node 
    void delete(E resource); // deletes a node 
    boolean search(E resource); // searches for a node 
    E highestRated(); //max rating
    E lowestRated(); //lowest rating
    Comparator<E> comparator();//comparing, sorting
    // Comparator<Resource> byPrefixOrder(int r);
    // Comparator<Resource> byRating();
    // Comparator<Resource> by 
    Iterator<E> iterator(); //iteration
    void inorderTraversal(); //in case we want to represent the data alphabetically 
    
}
