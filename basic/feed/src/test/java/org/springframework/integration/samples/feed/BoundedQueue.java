package org.springframework.integration.samples.feed;

import java.util.Collection;

public interface BoundedQueue<E> extends Collection<E>{

	E peekFirst();
	
	E peekLast();
	
	E poll();
	
}
