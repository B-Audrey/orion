package oc.mdd.seed;

import oc.mdd.entity.SeedEntity;
import oc.mdd.entity.TopicEntity;
import oc.mdd.repository.SeedRepository;
import oc.mdd.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MainSeed implements CommandLineRunner {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SeedRepository seedRepository;

    @Value("${spring.sql.init.mode}")
    private String initializationMode;

    @Override
    public void run(String... args) {

        if (initializationMode.equals("always") || initializationMode.equals("embedded")) {
            this.createTopics();
            // ...add more seeds here
            // seeds are created with id to be creatd only once,
            // think about increment ids correctly !!
        }
    }

    private void createTopics() {
        int topicSeedId = 1;
        if (!seedRepository.existsById(topicSeedId)) {
            List<String> topicLabels = Arrays.asList(
                    "Angular", "React", "JavaScript", "TypeScript", "Node", "NestJS",
                    "Java", "PostgreSQL", "Docker", "Kubernetes", "Spring Boot", "Microservices"
            );
            for (String label : topicLabels) {
                TopicEntity topic = getTopicEntity(label);
                topicRepository.save(topic);
            }
            SeedEntity seed = new SeedEntity();
            seed.setId(topicSeedId);
            seed.setName("add fake topics");
            seedRepository.save(seed);
        }
    }

    private static TopicEntity getTopicEntity(String label) {
        TopicEntity topic = new TopicEntity();
        topic.setLabel(label);
        topic.setDescription("Description détaillée de " + label + ". Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        return topic;
    }


}
