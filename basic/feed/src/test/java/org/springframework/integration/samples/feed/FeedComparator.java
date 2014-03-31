package org.springframework.integration.samples.feed;

import java.util.Comparator;

import com.sun.syndication.feed.synd.SyndEntry;

public class FeedComparator implements Comparator<SyndEntry>{

	@Override
	public int compare(SyndEntry o1, SyndEntry o2) {
		return o1.getPublishedDate().compareTo(o2.getPublishedDate());
	}

}
