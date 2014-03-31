package org.springframework.integration.samples.feed;

import java.util.Collection;
import java.util.List;

public interface BoundedQueue<E> extends Collection<E>{

	E peekFirst();
	
	E peekLast();
	
	E pollFirst();
	
	E pollLast();
	
	List<E> peekFirst(int offset);
	
	List<E> peekLast(int offset);
	
}
