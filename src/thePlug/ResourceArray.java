import java.util.NoSuchElementException;
import java.lang.IndexOutOfBoundsException;
import java.util.Iterator;

/**
 * The {@code ArrayList} class represents a resizing list. It has been
 * adapted based on Sedgewick and Wayne's Algorithms textbook (4th edition).
 * 
 * 
 * It doubles the underlying array when it is full and halves the underlying
 * array when it is one-quarter full.
 * 
 * @author Alexandra Papoutsaki
 * @author Aden Siebel
 *
 */
public class ArrayList<E> implements List<E> {
	private E[] data; // underlying array of Es
	private int size; // number of Es in arraylist. 
	
	/**
	 * Constructs an ArrayList with an initial capacity of 2.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList() {
		data = (E[]) new Object[2];
		size = 0;
	}

	/**
	 * Constructs an ArrayList with the specified capacity.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList(int capacity) {
		data = (E[]) new Object[capacity];
		size = 0;
	}

	/**
	 * Returns true if the ArrayList contains no Es.
	 * 
	 * @return true if the ArrayList does not contain any E
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns the number of Es in the ArrayList.
	 * 
	 * @return the number of Es in the ArrayList
	 */
	public int size() {
		return size;
	}

	/**
	 * Resizes the ArrayList's capacity to the specified capacity.
	 */
	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		//reserve a new temporary array of Es with the provided capacity
		E[] temp = (E[]) new Object[capacity];

        //copy all elements from old array (data) to temp array
		for (int i = 0; i < size; i++){
			temp[i] = data[i];
		}

        //point data to the new temp array
		data = temp;
	}

	/**
	 * Appends the element to the end of the ArrayList. Doubles its capacity if
	 * necessary.
	 * 
	 * @param element the element to be inserted
	 */
	public void add(E element) {
		if (size == data.length){
			resize(2 * data.length);
		}

		data[size] = element;
		size++;
	}

	/**
	 * Inserts the element at the specified index. Shifts existing elements to the
	 * right and doubles its capacity if necessary.
	 * 
	 * @param index the index to insert the element
	 * @param element the element to be inserted
	 * @pre: 0 <= index <= size
	 */
	public void add(int index, E element) {
        //check whether index is in range
		if (index > size || index < 0){
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
		}

        //if full double in size
		if (size == data.length){
			resize(2 * data.length);
		}

		// shift elements to the right
		for (int i = size; i > index; i--){
			data[i] = data[i - 1];
		}
        //increase number of elements
		size++;
        //put the element at the right index in data
		data[index] = element;
	}

	/**
	 * Replaces the element at the specified index with the specified E.
	 * 
	 * @param index the index of the element to replace
	 * @param element element to be stored at specified index
	 * @return the old element that was replaced
	 * @pre: 0<=index< size
	 */
	public E set(int index, E element) {
        //check if index is in range
		if (index >= size || index < 0){
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
		}
        //retrieve old element at index
		E old = data[index];
        //update index with new element
		data[index] = element;
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
	public E get(int index) {
		if (index >= size || index < 0){
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
		}

		return data[index];
	}

	/**
	 * Removes and returns the element from the elementnd of the ArrayList.
	 * 
	 * @return the removed E
	 * @pre size>0
	 */
	public E remove() {
		if (isEmpty()){
			throw new NoSuchElementException("The list is empty");
		}
		size--;
		E element = data[size];
		data[size] = null; // Avoid loitering (see recommended textbook).

		// Shrink to save space if possible
		if (size > 0 && size == data.length / 4){
			resize(data.length / 2);
		}

		return element;
	}

	/**
	 * Removes and returns the element at the specified index.
	 * 
	 * @param index the index of the element to be removed
	 * @return the removed element
	 * @pre: 0<=index<size
	 */
	public E remove(int index) {
        //check if index is in range
		if (index >= size || index < 0){
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
		}
        //retrieve element at index
		E element = data[index];
        //reduce number of elements by 1
		size--;
        //shift all elements from the index until the end left 1
		for (int i = index; i < size; i++){
			data[i] = data[i + 1];
		}
        //set last element to null (since they've been shifted)
		data[size] = null;

		// shrink to save space if necessary
		if (size > 0 && size == data.length / 4){
			resize(data.length / 2);
		}
        //return removed element
		return element;
	}

	/**
	 * Checks if the ArrayList contains the specified E.
	 * 
	 * @param element
	 *            the element to check if it is included in the ArrayList
	 * @return true if element is in the list
	 */
	public boolean contains(E element) {
		return indexOf(element) >= 0;
	}

	/**
	 * Check for the first index of element in the ArrayList.
	 * 
	 * @param element the element to check for
	 * @return the index of first occurrence of element
	 */
	public int indexOf(E element) {
		if (element == null) { // Special check for null elements
			for (int i = 0; i < size; i++){
				if (data[i] == null){
					return i;
				}
			}
		} else { // Regular check
			for (int i = 0; i < size; i++){
				if (element.equals(data[i])){
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * Clears the ArrayList of all elements.
	 */
	public void clear() {

		// Help garbage collector.
		for (int i = 0; i < size; i++){
			data[i] = null;
		}

		size = 0;
	}

	/**
	 * Converts the ArrayList to a String.
	 */
	public String toString() {

		String ret = "ArrayList of " + data.length + " capacity: [";

		Iterator<E> i = this.iterator();
		while (i.hasNext()) {
			ret += i.next();
			ret += "; ";
		}

		ret += "]";
		return ret;
	}

	/**
	 * Constructs an iterator for the ArrayList
	 */
	public Iterator<E> iterator() {
		return new ArrayListIterator();
	}

	/**
	 * A subclass that defines the iterator for the ArrayList.
	 */
	private class ArrayListIterator implements Iterator<E> {
		private int i = 0;

		public boolean hasNext() {
			return i < size;
		}

		public E next() {
			return data[i++];
		}

		public void remove() {
		}
	}

	public static void main(String[] args) {
		ArrayList<String> a1 = new ArrayList<String>();
		a1.add("CS062");
		System.out.println(a1);
		a1.add("ROCKS");
		System.out.println(a1);
		a1.add("!");
		System.out.println(a1);
		a1.add(1,"THROWS");
		System.out.println(a1);
		a1.add(3,"?");
		System.out.println(a1);
		a1.remove();
		System.out.println(a1);
		a1.remove();
		System.out.println(a1);
		a1.remove();
		System.out.println(a1);
		a1.remove(0);
		System.out.println(a1);
	}