package com.thomasvitale.tenantscope.camel;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BetaProcessor {
    private final ConsumerTemplate consumerTemplate;
    private final ProducerTemplate producerTemplate;

    @Autowired
    public BetaProcessor(ConsumerTemplate consumerTemplate, ProducerTemplate producerTemplate) {
        this.consumerTemplate = consumerTemplate;
        this.producerTemplate = producerTemplate;
    }

    public void process() {
        String invoice = consumerTemplate.receiveBody("jms:invoices", String.class);
        producerTemplate.sendBody("netty-http:http://thomasvitale.com/" + invoice);
    }
}
