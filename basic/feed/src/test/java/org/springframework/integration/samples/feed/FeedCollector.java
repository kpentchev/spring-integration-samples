package org.springframework.integration.samples.feed;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import com.sun.syndication.feed.synd.SyndEntry;

public class FeedCollector {
	
	private ConcurrentSkipListSet<SyndEntry> queue;
	private Integer capacity;
	private int size;
	
	private SyndEntry min;
	private SyndEntry max;
	
	public FeedCollector(int capacity, Comparator<SyndEntry> comparator){
		queue = new ConcurrentSkipListSet<SyndEntry>(comparator);
		this.capacity = capacity;
	}
	
	public void receiveFeed(SyndEntry feed){
		synchronized(capacity){
			if(capacity == size){
				queue.remove(min);
			}
		}
	}

}
