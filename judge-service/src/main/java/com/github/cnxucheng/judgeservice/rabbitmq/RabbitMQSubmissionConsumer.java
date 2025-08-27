package com.github.cnxucheng.judgeservice.rabbitmq;

import com.github.cnxucheng.judgeservice.service.JudgeService;
import com.github.cnxucheng.xcojfeignclient.service.SubmissionFeignClient;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RabbitMQSubmissionConsumer {

    @Resource
    private JudgeService judgeService;

    @Resource
    private SubmissionFeignClient submissionFeignClient;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {"submission_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        long submissionId = Long.parseLong(message);
        try {
            judgeService.doJudge(submissionFeignClient.getSubmissionById(submissionId));
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
