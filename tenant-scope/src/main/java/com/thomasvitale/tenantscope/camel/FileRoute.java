package com.thomasvitale.tenantscope.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:///Users/thomasvitale/folder1?recursive=true&noop=true&scheduler=quartz&scheduler.cron=0 0/1 * 1/1 * ? *")
                .process(exchange -> System.out.println("transferring " + exchange.getIn().getBody()))
                .to("file:///Users/thomasvitale/folder2");
    }
}
