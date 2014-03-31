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

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.core.PollableChannel;

import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author Oleg Zhurakousky
 *
 */
public class FeedInboundChannelAdapterSample {

	@SuppressWarnings("unchecked") 
	@Test
	public void runDemo() throws InterruptedException{
		ApplicationContext ac = 
			new ClassPathXmlApplicationContext("META-INF/spring/integration/FeedInboundChannelAdapterSample-context.xml");
		PriorityChannel feedChannel = ac.getBean("sortedChannel", PriorityChannel.class);
		Thread.sleep(10000);
		System.out.println("Call 1 --------------");
		for(int i=0; i < 20; i++) {
			Message<SyndEntry> message = (Message<SyndEntry>) feedChannel.receive(1000);
			if (message != null){
				SyndEntry entry = message.getPayload();
				System.out.println(entry.getPublishedDate() + " - " + entry.getTitle() + ":"+entry.getLink());
			}
		}
		
		System.out.println("Call 2 --------------");
		for(int i=0; i < 20; i++) {
			Message<SyndEntry> message = (Message<SyndEntry>) feedChannel.receive(1000);
			if (message != null){
				SyndEntry entry = message.getPayload();
				System.out.println(entry.getPublishedDate() + " - " + entry.getTitle() + ":"+entry.getLink());
			}
		}
	}
}
