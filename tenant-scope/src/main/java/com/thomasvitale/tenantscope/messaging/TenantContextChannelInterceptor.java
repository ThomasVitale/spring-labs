package com.thomasvitale.tenantscope.messaging;

import com.thomasvitale.tenantscope.context.TenantAttributes;
import com.thomasvitale.tenantscope.context.TenantContextHolder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TenantContextChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        String tenantId = TenantContextHolder.getTenantIdentifier();
        if (StringUtils.hasText(tenantId)) {
            return MessageBuilder
                    .fromMessage(message)
                    .setHeader("tenantId", tenantId)
                    .build();
        }
        return message;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        String tenantId = message.getHeaders().get("tenantId", String.class);
        if (StringUtils.hasText(tenantId)) {
            TenantAttributes tenantAttributes = new TenantAttributes(tenantId);
            TenantContextHolder.setTenantAttributes(tenantAttributes);
        }
        return message;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        TenantContextHolder.resetTenantAttributes();
    }
}
