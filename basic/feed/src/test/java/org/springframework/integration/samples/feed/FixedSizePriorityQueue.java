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
	
	private Node root;
	
	private Node min;
	private Node max;
	
	public FixedSizePriorityQueue(int capacity, Comparator<E> comparator){
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

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
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
		if(root == null){
			root = new Node(e);
		}
		else{
			if(size == capacity){
				deleteNode(min);
			}
			insertValue(root, e);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		if(root == null){
			return false;
		}
		else{
			deleteValue(root, (E) o);
			return true;
		}
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
		root = null;
		min = null;
		max = null;
		size = 0;
	}

	@Override
	public E peekFirst() {
		return max.value;
	}

	@Override
	public E peekLast() {
		return min.value;
	}

	@Override
	public E poll() {
		Node result = this.max;
		deleteNode(this.max);
		return result.value;
	}
	
	private void insertValue(Node currentNode, E value){
		int result = comparator.compare(value, currentNode.value);
		if(result < 0){
			if(currentNode.left == null){
				currentNode.left = new Node(value);
				
				//TODO balance
			}
			else{
				insertValue(currentNode.left, value);
			}
		}
		else if(result > 0){
			if(currentNode.right == null){
				currentNode.right = new Node(value);
				
				//TODO balance
			}
			else{
				insertValue(currentNode.right, value);
			}
		}
		else{
			return;
		}
	}
	
	private void deleteValue(Node currentNode, E value){
		int result = comparator.compare(value, currentNode.value);
		if(result < 0){
			if(currentNode.left == null){
				return;
			}
			else{
				deleteValue(currentNode.left, value);
			}
		}
		else if(result > 0){
			if(currentNode.right == null){
				return;
			}
			else{
				deleteValue(currentNode.right, value);
			}
		}
		else{
			deleteNode(currentNode);
		}
	}
	
	private void balance(Node currentNode){
		if(height(currentNode.left ) - height(currentNode.right ) > ALLOWED_IMBALANCE )
            if(height(currentNode.left.left ) >= height(currentNode.left.right ) )
            	currentNode = rotateWithLeftChild(currentNode);
            else
            	currentNode = doubleWithLeftChild(currentNode);
        else
        if(height(currentNode.right ) - height(currentNode.left ) > ALLOWED_IMBALANCE )
            if(height(currentNode.right.right ) >= height(currentNode.right.left ) )
            	currentNode = rotateWithRightChild(currentNode);
            else
            	currentNode = doubleWithRightChild(currentNode);

		currentNode.height = Math.max( height(currentNode.left ), height(currentNode.right ) ) + 1;
        return t;
	}
	
	private int height (Node node){
        return node == null ? -1 : node.height;
    }
	
	private void deleteNode(Node node){
		//TODO
	}

	class Node {
		E value;
		Node left;
		Node right;
		
		int height;
		
		Node(E value){
			this.value = value;
			this.height = 0;
			this.left = null;
			this.right = null;
		}
		
	}
	

}
