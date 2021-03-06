/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.samples.feed;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author Oleg Zhurakousky
 *
 */
public class FeedInboundChannelAdapterSample {

	@Test
	public void runDemo() throws InterruptedException{
		ApplicationContext ac = 
			new ClassPathXmlApplicationContext("META-INF/spring/integration/FeedInboundChannelAdapterSample-context.xml");
		FeedCollector feedCollector = ac.getBean("feedCollector", FeedCollector.class);
		Thread.sleep(30000);
		
		List<SyndEntry> result =  feedCollector.get(0, 5000);
		assertEquals(20, result.size());
		for(SyndEntry entry : result){
			System.out.println(entry.getPublishedDate() + " - " + entry.getTitle() + ":"+entry.getLink());
		}
		
	}
}
