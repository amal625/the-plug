package thePlug;
import java.util.Iterator;
import java.util.Comparator;

public interface ResourceBSTInterface<E>{


    /**
     * Inserts a new node with the given data into the BST.
     *
     * @param data the data to be inserted
     */
    void insert(E data);

    /**
     * Deletes a node with the given data from the BST.
     *
     * @param data the data to be deleted
     */
    void delete(E data);
    /**
     * Searches for a node with the given data in the BST.
     *
     * @param data the data to search for
     * @return true if the data is found, false otherwise
     */
    boolean search(E data);

    E highestRated(); //max rating
    E lowestRated(); //lowest rating

    Comparator<E> comparator();//comparing, sorting
    Iterator<E> iterator(); //iteration

    /**
     * Performs an inorder traversal of the BST and prints the data.
     * in case we want to represent the data alphabetically
     */
    void inorderTraversal(); 
    
}
