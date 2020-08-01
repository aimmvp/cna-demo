package cnademo;

import cnademo.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class OrderStatusViewHandler {


    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrdered_then_CREATE_1 (@Payload Ordered ordered) {
        try {
            if (ordered.isMe()) {
                // view 객체 생성
                OrderStatus orderStatus = new OrderStatus();
                // view 객체에 이벤트의 Value 를 set 함
                orderStatus.setOrderId(ordered.getId());
                orderStatus.setProductId(ordered.getProductId());
                orderStatus.setQty(ordered.getQty());
                // view 레파지 토리에 save
                orderStatusRepository.save(orderStatus);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenShipped_then_UPDATE_1(@Payload Shipped shipped) {
        try {
            if (shipped.isMe()) {
                // view 객체 조회
                List<OrderStatus> orderStatusList = orderStatusRepository.findByOrderId(shipped.getOrderId());
                for(OrderStatus orderStatus : orderStatusList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    orderStatus.setStatus(shipped.getStatus());
                    orderStatus.setDeliveryId(shipped.getId());
                    // view 레파지 토리에 save
                    orderStatusRepository.save(orderStatus);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCanceled_then_UPDATE_2(@Payload DeliveryCanceled deliveryCanceled) {
        try {
            if (deliveryCanceled.isMe()) {
                // view 객체 조회
                List<OrderStatus> orderStatusList = orderStatusRepository.findByOrderId(Long.valueOf(deliveryCanceled.getOrderId()));
                for(OrderStatus orderStatus : orderStatusList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    orderStatus.setStatus(deliveryCanceled.getStatus());
                    // view 레파지 토리에 save
                    orderStatusRepository.save(orderStatus);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCanceled_then_DELETE_1(@Payload DeliveryCanceled deliveryCanceled) {
        try {
            if (deliveryCanceled.isMe()) {
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}