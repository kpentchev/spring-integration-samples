package org.springframework.integration.samples.feed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class FixedSizePriorityQueue<E> implements Serializable, BoundedQueue<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8788158373581147781L;

	private Integer capacity;
	private Comparator<E> comparator;

	private ConcurrentSkipListSet<E> list;

	public FixedSizePriorityQueue(Integer capacity, Comparator<E> comparator) {
		this.capacity = capacity;
		this.comparator = comparator;
		this.list = new ConcurrentSkipListSet<E>(comparator);
		
	}

	@Override
	public boolean add(E e) {
		boolean shouldAdd = true;
		boolean added = false;
		synchronized(capacity){
			if(capacity.equals(list.size())){
				shouldAdd = false;
				int compareResult = comparator.compare(e, list.last());
				if(compareResult < 0){
					list.pollLast();
					added = list.add(e);
				} 
			}
		}
		if(shouldAdd){
			added = list.add(e);
		}
		return added;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean added = true;
		for(E element : c){
			added = added && add(element);
		}
		return added; 
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public E peekFirst() {
		return list.first();
	}

	@Override
	public E peekLast() {
		return list.last();
	}

	@Override
	public E pollFirst() {
		return list.pollFirst();
	}

	@Override
	public E pollLast() {
		return list.pollLast();
	}

	@Override
	public List<E> peekFirst(int offset) {
		List<E> resultList = new ArrayList<E>(offset);
		Iterator<E> it = list.iterator();
		while(it.hasNext() && resultList.size() < offset){
			resultList.add(it.next());
		}
		return resultList;
	}

	@Override
	public List<E> peekLast(int offset) {
		List<E> resultList = new ArrayList<E>(offset);
		Iterator<E> it = list.descendingIterator();
		while(it.hasNext() && resultList.size() < offset){
			resultList.add(it.next());
		}
		return resultList;
	}

	

}
