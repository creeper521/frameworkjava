package com.bitejiuyeke.bitemstemplateservice.rabbit;

import com.bitejiuyeke.bitemstemplateservice.domain.MessageDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @RabbitListener(queues = "testQueue")
    public void listenerQueue(MessageDTO messageDTO) {
        System.out.println("接收到消息：" + messageDTO);
        System.out.println(MessageDTO.class);
    }
}
