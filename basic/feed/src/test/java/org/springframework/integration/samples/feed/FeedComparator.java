package org.springframework.integration.samples.feed;

import java.util.Comparator;

import org.springframework.integration.Message;

import com.sun.syndication.feed.synd.SyndEntry;

public class FeedComparator implements Comparator<Message<SyndEntry>>{

	@Override
	public int compare(Message<SyndEntry> o1, Message<SyndEntry> o2) {
		return o2.getPayload().getPublishedDate().compareTo(o1.getPayload().getPublishedDate());
	}

}
