package org.springframework.integration.samples.feed;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class FixedSizePriorityQueue<E> implements Serializable, BoundedQueue<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8788158373581147781L;

	private static final int ALLOWED_IMBALANCE = 1;

	private int capacity;
	private int size;
	private Comparator<E> comparator;

	private AvlNode<E> root;

	private AvlNode<E> min;
	private AvlNode<E> max;

	public FixedSizePriorityQueue(int capacity, Comparator<E> comparator) {
		this.capacity = capacity;
		this.size = 0;
		this.comparator = comparator;
		this.root = null;
		this.min = null;
		this.max = null;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	// Assume t is either balanced or within one of being balanced
	private AvlNode<E> balance(AvlNode<E> currentNode) {
		if (currentNode == null)
			return currentNode;

		if (height(currentNode.left) - height(currentNode.right) > ALLOWED_IMBALANCE)
			if (height(currentNode.left.left) >= height(currentNode.left.right))
				currentNode = rotateWithLeftChild(currentNode);
			else
				currentNode = doubleWithLeftChild(currentNode);
		else if (height(currentNode.right) - height(currentNode.left) > ALLOWED_IMBALANCE)
			if (height(currentNode.right.right) >= height(currentNode.right.left))
				currentNode = rotateWithRightChild(currentNode);
			else
				currentNode = doubleWithRightChild(currentNode);

		currentNode.height = Math.max(height(currentNode.left), height(currentNode.right)) + 1;
		return currentNode;
	}

	/**
	 * Internal method to insert into a subtree.
	 * 
	 * @param x
	 *            the item to insert.
	 * @param currentNode
	 *            the node that roots the subtree.
	 * @return the new root of the subtree.
	 */
	private AvlNode<E> insert(E x, AvlNode<E> currentNode) {
		if (currentNode == null)
			return new AvlNode<E>(x, null, null);

		int compareResult = this.comparator.compare(x, currentNode.element);

		if (compareResult < 0)
			currentNode.left = insert(x, currentNode.left);
		else if (compareResult > 0)
			currentNode.right = insert(x, currentNode.right);
		else
			; // Duplicate; do nothing
		return balance(currentNode);
	}

	/**
	 * Internal method to remove from a subtree.
	 * 
	 * @param x
	 *            the item to remove.
	 * @param t
	 *            the node that roots the subtree.
	 * @return the new root of the subtree.
	 */
	private AvlNode<E> remove(E element, AvlNode<E> currentNode) {
		if (currentNode == null)
			return currentNode; // Item not found; do nothing

		int compareResult = this.comparator.compare(element, currentNode.element);

		if (compareResult < 0)
			currentNode.left = remove(element, currentNode.left);
		else if (compareResult > 0)
			currentNode.right = remove(element, currentNode.right);
		else if (currentNode.left != null && currentNode.right != null) // Two
																		// children
		{
			currentNode.element = findMin(currentNode.right).element;
			currentNode.right = remove(currentNode.element, currentNode.right);
		} else
			currentNode = (currentNode.left != null) ? currentNode.left : currentNode.right;
		return balance(currentNode);
	}

	/**
	 * Internal method to find the smallest item in a subtree.
	 * 
	 * @param currentNode
	 *            the node that roots the tree.
	 * @return node containing the smallest item.
	 */
	private AvlNode<E> findMin(AvlNode<E> currentNode) {
		if (currentNode == null)
			return currentNode;

		while (currentNode.left != null)
			currentNode = currentNode.left;
		return currentNode;
	}

	/**
	 * Internal method to find the largest item in a subtree.
	 * 
	 * @param currentNode
	 *            the node that roots the tree.
	 * @return node containing the largest item.
	 */
	private AvlNode<E> findMax(AvlNode<E> currentNode) {
		if (currentNode == null)
			return currentNode;

		while (currentNode.right != null)
			currentNode = currentNode.right;
		return currentNode;
	}

	/**
	 * Internal method to find an item in a subtree.
	 * 
	 * @param x
	 *            is item to search for.
	 * @param t
	 *            the node that roots the tree.
	 * @return true if x is found in subtree.
	 */
	private boolean contains(E x, AvlNode<E> t) {
		while (t != null) {
			int compareResult = this.comparator.compare(x, t.element);

			if (compareResult < 0)
				t = t.left;
			else if (compareResult > 0)
				t = t.right;
			else
				return true; // Match
		}

		return false; // No match
	}

	/**
	 * Internal method to print a subtree in sorted order.
	 * 
	 * @param t
	 *            the node that roots the tree.
	 */
	private String printTree(AvlNode<E> t) {
		if (t != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(printTree(t.left));
			sb.append("->");
			sb.append(t.element);
			sb.append("<-");
			sb.append(printTree(t.right));
			return sb.toString();
		}
		return "{}";
	}

	/**
	 * Return the height of node t, or -1, if null.
	 */
	private int height(AvlNode<E> t) {
		return t == null ? -1 : t.height;
	}

	/**
	 * Rotate binary tree node with left child. For AVL trees, this is a single
	 * rotation for case 1. Update heights, then return new root.
	 */
	private AvlNode<E> rotateWithLeftChild(AvlNode<E> k2) {
		AvlNode<E> k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		k1.height = Math.max(height(k1.left), k2.height) + 1;
		return k1;
	}

	/**
	 * Rotate binary tree node with right child. For AVL trees, this is a single
	 * rotation for case 4. Update heights, then return new root.
	 */
	private AvlNode<E> rotateWithRightChild(AvlNode<E> k1) {
		AvlNode<E> k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
		k2.height = Math.max(height(k2.right), k1.height) + 1;
		return k2;
	}

	/**
	 * Double rotate binary tree node: first left child with its right child;
	 * then node k3 with new left child. For AVL trees, this is a double
	 * rotation for case 2. Update heights, then return new root.
	 */
	private AvlNode<E> doubleWithLeftChild(AvlNode<E> k3) {
		k3.left = rotateWithRightChild(k3.left);
		return rotateWithLeftChild(k3);
	}

	/**
	 * Double rotate binary tree node: first right child with its left child;
	 * then node k1 with new right child. For AVL trees, this is a double
	 * rotation for case 3. Update heights, then return new root.
	 */
	private AvlNode<E> doubleWithRightChild(AvlNode<E> k1) {
		k1.right = rotateWithLeftChild(k1.right);
		return rotateWithRightChild(k1);
	}

	private static class AvlNode<AnyType> {
		// Constructors
		AvlNode(AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt) {
			element = theElement;
			left = lt;
			right = rt;
			height = 0;
		}

		AnyType element; // The data in the node
		AvlNode<AnyType> left; // Left child
		AvlNode<AnyType> right; // Right child
		int height; // Height
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		return contains((E) o, root);
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(E e) {
		if (size == capacity) {
			int compareResult = comparator.compare(e, this.min.element);
			if (compareResult < 0) {
				return false;
			} else {
				root = remove(this.min.element, root);
				root = insert(e, root);
				return true;
			}

		} else {
			root = insert(e, root);
			size++;
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		root = remove((E) o, root);
		size--;
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		this.root = null;
		this.max = null;
		this.min = null;
	}

	@Override
	public E peekFirst() {
		return this.max.element;
	}

	@Override
	public E peekLast() {
		return this.min.element;
	}

	@Override
	public E poll() {
		AvlNode<E> result = this.max;
		remove(result.element, root);
		this.max = findMax(root);
		return result.element;
	}
	
	@Override
	public String toString() {
		return printTree(root);
	}

}
