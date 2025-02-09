import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    @KafkaListener(topics = "your_topic_name", groupId = "your_group_id")
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }
}
