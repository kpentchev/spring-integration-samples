package org.springframework.integration.samples.feed;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.Test;

public class TestFixedSizePriorityQueue {
	
	@Test
	public void testInsert(){
		FixedSizePriorityQueue<Integer> queue = new FixedSizePriorityQueue<Integer>(5, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 < o2 ? -1 : o2 < o1 ? 1 : 0;
			}
		});
		
		queue.add(4);
		queue.add(1);
		queue.add(2);
		
		assertEquals(3, queue.size());
		assertEquals((Integer) 1, queue.peekLast());
		assertEquals((Integer) 4, queue.peekFirst());
		assertEquals(Arrays.asList(4, 2), queue.peekFirst(2));
	}
	
	@Test
	public void testRemove(){
		FixedSizePriorityQueue<Integer> queue = new FixedSizePriorityQueue<Integer>(5, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 < o2 ? -1 : o2 < o1 ? 1 : 0;
			}
		});
		
		queue.add(4);
		queue.add(1);
		queue.add(2);
		queue.add(8);
		queue.add(3);
		queue.remove(2);
		
		assertEquals(4, queue.size());
		assertEquals((Integer) 1, queue.peekLast());
		assertEquals((Integer) 8, queue.peekFirst());
		
	}
	
	@Test
	public void testPoll(){
		FixedSizePriorityQueue<Integer> queue = new FixedSizePriorityQueue<Integer>(5, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 < o2 ? -1 : o2 < o1 ? 1 : 0;
			}
		});
		
		queue.add(4);
		queue.add(1);
		queue.add(2);
		queue.add(8);
		queue.add(3);
		
		assertEquals((Integer) 8, queue.pollFirst());
		assertEquals((Integer) 1, queue.pollLast());
		
		
		assertEquals((Integer) 2, queue.peekLast());
		assertEquals((Integer) 4, queue.peekFirst());
		
	}

}
