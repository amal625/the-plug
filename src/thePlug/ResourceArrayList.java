package thePlug;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;


public class ResourceArrayList<Resource> {
    private ResourceArrayList<Resource>[] colleges; // colleges within intial array
    private int numColleges; //num of objects in colleges List
   

    /**
	 * Constructs an ArrayList with the specified capacity.
	 */
	@SuppressWarnings("unchecked")
	public ResourceArrayList(int capacity) {
		colleges = (ResourceArrayList<Resource>[]) new ResourceArrayList[capacity];
		numColleges = 0;
	}


    /**
	 * Returns true if the ArrayList contains no Es.
	 * 
	 * @return true if the ArrayList does not contain any E
	 */
	public boolean isEmpty() {
		return numColleges == 0;
	}

    /**
	 * Returns the number of Es in the ArrayList.
	 * 
	 * @return the number of Es in the ArrayList
	 */
	public int size() {
		return numColleges;
	}

    
    
    
    /**
	 * Resizes the ArrayList's capacity to the specified capacity.
	 */
	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		//reserve a new temporary array of Es with the provided capacity
		ResourceArrayList<Resource>[] temp = (ResourceArrayList<Resource>[]) new ResourceArrayList[capacity];

        //copy all elements from old array (data) to temp array
		for (int i = 0; i < numColleges; i++){
			temp[i] = colleges[i];
		}

        //point data to the new temp array
		colleges = temp;
	}


    /**
	 * Appends a new ResourceArrayList to the end of the outer ArrayList.
     * Doubles its capacity if necessary.
	 * 
	 * @param element the element to be inserted
	 */
	public void addList(ResourceArrayList<Resource> element) {
		if (numColleges == colleges.length){
			resize(2 * colleges.length);
		}

		colleges[numColleges] = element;
		numColleges++;
	}
    /**
     * Creates a new ResourceArrayList at the end of the outer ArrayList. 
     * Doubles its capacity if necessary.
     * @param capacity the capacity of the new ResourceArrayList
     * @return the index of the newly created ResourceArraylist 
    */
    public int createNewList(int capacity){
        if(numColleges == colleges.length){
            resize(2 * colleges.length);
        }

        colleges[numColleges] = new ResourceArrayList<Resource>(capacity);
        numColleges++;
        return numColleges - 1;
    }



    
    /**
     * adds an element to the ResourceArraylist at the specified index.
     * @param listindex the index of the ResourceArrayList
     * @param college the element to add
     * @throws IndexOutofBoundsException if listindex is out of range
     */
    public void addToList(int listIndex, ResourceArrayList<Resource> college){
        if (listIndex >= numColleges || listIndex < 0){
            throw new IndexOutOfBoundsException("List index" + listIndex + "out of bounds");

        }
        colleges[listIndex].add(listIndex, college);
    }

    /**
	 * Inserts the element at the specified index. Shifts existing elements to the
	 * right and doubles its capacity if necessary.
	 * 
	 * @param index the index to insert the element
	 * @param element the element to be inserted
	 * @pre: 0 <= index <= size
	 */
	public void add(int index, ResourceArrayList<Resource> element) {
        //check whether index is in range
		if (index > numColleges || index < 0){
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
		}

        //if full double in size
		if (numColleges == colleges.length){
			resize(2 * colleges.length);
		}

		// shift elements to the right
		for (int i = numColleges; i > index; i--){
			colleges[i] = colleges[i - 1];
		}
        //increase number of elements
		numColleges++;
        //put the element at the right index in data
		colleges[index] = element;
	}

    /**
     * Gets the nested ResourceArrayList at the specified index
     * 
     * @param inndex the index of the ResourceArrayList to return
     * @return the ResourceArrayList at the specified index 
     * @throws IndexOutofBoundsException if the index is out of range
     */
    public ResourceArrayList<Resource> getList(int index){
        if (index >= numColleges || index < 0){
            throw new IndexOutOfBoundsException("Index" + index + "out of bounds");
        }

        return colleges[index];
    }

    /**
     * Gets an element from a nested ResourceArrayList
     * 
     * @param listIndex
     * @param collegeIndex
     * @return the element at the specified indices
     * @throws IndexOutOfBoundsException if any index is out of range
     */
    public ResourceArrayList<Resource> get(int listIndex, int collegeIndex){
        if(listIndex >= numColleges || listIndex < 0){
            throw new IndexOutOfBoundsException("List index" + listIndex + "out of bounds");
        }

        return colleges[listIndex].get(collegeIndex);
    }

  

    /**
	 * Replaces the element at the specified index with the specified E.
	 * 
	 * @param index the index of the element to replace
	 * @param element element to be stored at specified index
	 * @return the old element that was replaced
	 * @pre: 0<=index< size
	 */
	public ResourceArrayList set(int index, ResourceArrayList<Resource> element) {
        //check if index is in range
		if (index >= numColleges || index < 0){
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
		}
        //retrieve old element at index
		ResourceArrayList old = colleges[index];
        //update index with new element
		colleges[index] = element;
        //return old element
		return old;
	}
    /**
	 * Returns the element at the specified index.
	 * 
	 * @param index the index of the element to return
	 * @return the element at the specified index
	 * @pre: 0<=index<size
	 */
	public ResourceArrayList get(int index) {
		if (index >= numColleges || index < 0){
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
		}

		return colleges[index];

	}

    /**
	 * Removes and returns the ResourceArraylist from the end of the outer ArrayList.
	 * 
	 * @return the removed ResourceArrayList
	 * @throws NoSuchElementException ("The list is empty")
	 */
	public ResourceArrayList<Resource> remove() {
		if (isEmpty()){
			throw new NoSuchElementException("The list is empty");
		}
		numColleges--;
		ResourceArrayList<Resource> element = colleges[numColleges];
		colleges[numColleges] = null; // Avoid loitering (see recommended textbook).

		// Shrink to save space if possible
		if (numColleges > 0 && numColleges == colleges.length / 4){
			resize(colleges.length / 2);
		}

		return element;
	}
    /**
	 * Removes and returns the ResourceArraylist at the specified index.
	 * 
	 * @param index the index of the element to be removed
	 * @return the removed element
	 * @throws IndexOutofBoundsException if index is out of range
	 */
	public ResourceArrayList <Resource> remove(int index) {
        //check if index is in range
		if (index >= numColleges || index < 0){
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
		}
        //retrieve element at index
		ResourceArrayList <Resource> element = colleges[index];
        //reduce number of elements by 1
		numColleges--;
        //shift all elements from the index until the end left 1
		for (int i = index; i < numColleges; i++){
			colleges[i] = colleges[i + 1];
		}
        //set last element to null (since they've been shifted)
		colleges[numColleges] = null;

		// shrink to save space if necessary
		if (numColleges > 0 && numColleges == colleges.length / 4){
			resize(colleges.length / 2);
		}
        //return removed element
		return element;
	}
    
    /**
     * New method to remove an element from a nested list 
     * removes an element from a nested ResourceArrayList
     * 
     * @param listIndex the index of the ResourceArrayList 
     * @param resourceIndex the index of the element to remove
     * @throws IndexOutofBoundsException if any index is out of range
     * @return the removed element
     */
    public ResourceArrayList <Resource> remove(int listIndex, int resourceIndex){
        if (listIndex >= numColleges || listIndex < 0){
            throw new IndexOutOfBoundsException("List index" + listIndex + "out of bounds");
        }
        return colleges[listIndex].remove(listIndex, resourceIndex);
    }

    /**
	 * Check for the first index of element in the ArrayList.
	 * 
	 * @param element the element to check for
	 * @return the index of first occurrence of element
	 */
	public int indexOf(Resource element) {
		if (element == null) { // Special check for null elements
			for (int i = 0; i < numColleges; i++){
				if (colleges[i] == null){
					return i;
				}
			}
		} else { // Regular check
			for (int i = 0; i < numColleges; i++){
				if (element.equals(colleges[i])){
					return i;
				}
			}
		}

		return -1;
	}
 /**
	 * Checks if the ArrayList contains the specified E.
	 * 
	 * @param element
	 *            the element to check if it is included in the ArrayList
	 * @return true if element is in the list
	 */
	public boolean contains(Resource element) {
		return indexOf(element) >= 0;
	}

    /**
	 * Clears all ResourceArrayLists & their elements
	 */
	public void clear() {

		// Help garbage collector.
		for (int i = 0; i < numColleges; i++){
            if (colleges[i] != null){
                colleges[i].clear();
                colleges[i] = null;

            }
		}

		numColleges = 0;
	}


    /**
	 * Converts the ArrayList to a String.
	 */
	public String toString() {

		StringBuilder ret = new StringBuilder("NestedResourceArrayList of" + colleges.length + "capacity: [\n");

        for (int i = 0; i < numColleges; i++){
            ret.append(" List ").append(i).append(": ");
            ret.append(colleges[i].toString());
            ret.append("\n");
        }

        ret.append("]");
        return ret.toString();
		
	}
	

    public static void main(String[] args) {
            // Create a nested ResourceArrayList with initial capacity of 3
            ResourceArrayList<String> nestedLists = new ResourceArrayList<>(3);
            
            // Create first inner list for CS courses
            int csList = nestedLists.createNewList(5);
            // Create a ResourceArrayList for CS062
            ResourceArrayList<String> cs062List = new ResourceArrayList<>(1);
            ResourceArrayList<String> cs054List = new ResourceArrayList<>(1);
            ResourceArrayList<String> cs081List = new ResourceArrayList<>(1);
            
            // Add the course lists to the CS list
            nestedLists.getList(csList).add(0, cs062List);
            nestedLists.getList(csList).add(1, cs054List);
            nestedLists.getList(csList).add(2, cs081List);
            
            // Create second inner list for Math courses
            int mathList = nestedLists.createNewList(5);
            ResourceArrayList<String> math055List = new ResourceArrayList<>(1);
            ResourceArrayList<String> math067List = new ResourceArrayList<>(1);
            
            // Add the course lists to the Math list
            nestedLists.getList(mathList).add(0, math055List);
            nestedLists.getList(mathList).add(1, math067List);
            
            // Create third inner list for Arts courses
            int artsList = nestedLists.createNewList(5);
            ResourceArrayList<String> art033List = new ResourceArrayList<>(1);
            ResourceArrayList<String> art062List = new ResourceArrayList<>(1);
            ResourceArrayList<String> music045List = new ResourceArrayList<>(1);
            
            // Add the course lists to the Arts list
            nestedLists.getList(artsList).add(0, art033List);
            nestedLists.getList(artsList).add(1, art062List);
            nestedLists.getList(artsList).add(2, music045List);
            
            // Print the whole structure
            System.out.println("Full nested structure:");
            System.out.println(nestedLists);
            
            // Print specific elements
            System.out.println("\nAccessing specific elements:");
            try {
                System.out.println("CS List, element 1: " + nestedLists.get(csList, 1));
                System.out.println("Math List, element 0: " + nestedLists.get(mathList, 0));
                System.out.println("Arts List, element 2: " + nestedLists.get(artsList, 2));
            } catch (Exception e) {
                System.out.println("Error accessing elements: " + e.getMessage());
            }
            
            // Remove an element and show the updated structure
            System.out.println("\nRemoving MATH067 list:");
            try {
                ResourceArrayList<String> removed = nestedLists.remove(mathList, 1);
                System.out.println("Removed: " + removed);
            } catch (Exception e) {
                System.out.println("Error removing element: " + e.getMessage());
            }
            
            System.out.println("\nUpdated structure:");
            System.out.println(nestedLists);
            
            // Remove an entire list
            System.out.println("\nRemoving the CS list:");
            try {
                ResourceArrayList<String> removedList = nestedLists.remove(csList);
                System.out.println("Removed list: " + removedList);
            } catch (Exception e) {
                System.out.println("Error removing list: " + e.getMessage());
            }
            
            System.out.println("\nFinal structure after removing CS list:");
            System.out.println(nestedLists);
    }


    
		
}
