package com.brodygaudel.ebank;

import com.thoughtworks.xstream.XStream;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankApplication.class, args);
	}

	@Bean
	public CommandBus commandBus(){
		return SimpleCommandBus.builder().build();
	}


	@Bean
	public XStream xStream() {
		XStream xStream = new XStream();
		xStream.allowTypesByWildcard(new String[] { "com.brodygaudel.**" });
		return xStream;
	}

	@Bean
	public EmbeddedEventStore eventStore() {
		return EmbeddedEventStore.builder()
				.storageEngine(new InMemoryEventStorageEngine())
				.build();
	}




}
