package org.example.springbootdemo.controller.mq;

import org.example.springbootdemo.service.IKafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * kafka controller
 * <p>
 * 「生产者」 生产 消息，消息发送到 kafka 集群 =>
 * kafka 集群将消息放在某一个 partition 分区中，依据 key 判定放在哪个分区，发送到消费者组 =>
 * 该消息只能被消费者组中的一个消费者消费，但是不同的消费组都可以消费该消息
 * <p>
 * 如果有 A，B 两个消费组，其中 A 消费组有 C1，C2 两个消费者，那么当 kafka 集群配置好消费组（包括其中消费者）时，那么 kafka 就已经定好哪几个分区固定发给消费组 A 中的 C1，哪几个分区未来始终固定发给消费组 B 中的 C2
 * <p>
 * 关键词：主题，生产者，消费组，消费者，分区，key，偏移量
 * <p>
 * 关键场景：
 * 1.生产者生产消息发往 kafka 集群 topic，发送指定 topic 和 msg
 * 2.kafka 集群依据 key 将消息放入依据 key 计算出来的分区，key 可由消费者发送
 * 3.kafka 默认策略，一旦 kafka 集群配置好消费组和消费者，消费组中具体哪个消费者消费具体哪一个分区就已经确定好了
 * 4.同一个 topic 的消息，不同消费组都可以消费，但是同一个消费组中只有一个消费者可以消费，消费指定 topic 和消费组
 * 5.消费者消费默认是自动提交偏移量，消费 5s 后自动告诉 kafka 消息被消费了，下次无需再发送了
 * 6.消费者手动提交偏移量，需要 application.yml 配置支持手动提交偏移量，且配置 Configuration 中指定 kafka 的 bean，然后编写方法和注解，注解指定这个 kafka 提交方式的 bean，代码中通过 ack.acknowledge(); 手动提交偏移量
 */
@RestController
@RequestMapping("/kafka")
public class kafkaController {
    @Autowired
    private IKafkaService kafkaService;

    /* ========== 生产者 ========== */

    /**
     * 生产者
     * 常规发送消息
     */
    @GetMapping("/product/send-msg")
    public String sendMsg() {
        kafkaService.sendMsg("my-topic", "hello world!");
        return "success";
    }

    /**
     * 生产者
     * 发送带有 key 的消息
     * kafka 使用 key 的哈希值来决定消息发往 topic 的哪个 partition，相同的 key 发到的分区也相同
     * 即：相同 key 的消息能保证在该分区是顺序发出的
     * key 通常表示业务主键，如用户 id，订单 id 等，比如 user_12345
     */
    @GetMapping("/product/send-with-key")
    public String sendMsgWithKey() {
        kafkaService.sendMsgWithKey("my-topic", "user_12345", "hello world!");
        return "success";
    }

    /**
     * 生产者
     * 发送消息并且获取结果
     */
    @GetMapping("/product/send-async")
    public String sendMsgAsync() {
        kafkaService.sendMsgAsync("my-topic", "hello world!");
        return "success";
    }

    /**
     * 生产者
     * 批量发送消息
     */
    @GetMapping("/product/send-batch")
    public String sendBatchMsg() {
        kafkaService.sendBatchMsg("my-topic", Arrays.asList("hello", "world!"));
        return "success";
    }

    /* ========== 消费者 ========== */

    /**
     * 消费者
     */
    @GetMapping("/consume")
    public String listenMsg() {
        /*
        如下 4 个方法都是监听方法，不用被 controller 调用，监听到有消息后自动处理
        void listenMsg(String msg);
        void listenMutiTopics(String msg);
        void listenWithMetadata(String msg, String topic, int partition, long offset, String key);
        void listenWithManualAck(String msg, Acknowledgment ack);
        * */

        return "success";
    }

}
