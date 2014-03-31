package org.springframework.integration.samples.feed;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import com.sun.syndication.feed.synd.SyndEntry;

public class FeedCollector {
	
	private FixedSizePriorityQueue<SyndEntry> queue;
	
	public FeedCollector(int capacity, Comparator<SyndEntry> comparator){
		queue = new FixedSizePriorityQueue<SyndEntry>(capacity, comparator);
	}
	
	public void receiveFeed(SyndEntry feed){
		queue.add(feed);
	}
	
	public List<SyndEntry> get(Integer offset, Integer limit){
		return queue.peekFirst(limit);
	}

}
