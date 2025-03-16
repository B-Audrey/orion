package oc.mdd.seed;

import oc.mdd.entity.PostEntity;
import oc.mdd.entity.SeedEntity;
import oc.mdd.entity.TopicEntity;
import oc.mdd.entity.UserEntity;
import oc.mdd.repository.PostRepository;
import oc.mdd.repository.SeedRepository;
import oc.mdd.repository.TopicRepository;
import oc.mdd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MainSeed implements CommandLineRunner {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SeedRepository seedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostRepository postRepository;

    @Value("${spring.sql.init.mode}")
    private String initializationMode;

    @Override
    public void run(String... args) {

        if (initializationMode.equals("always") || initializationMode.equals("embedded")) {
            this.createTopics();
            this.createUser();
            this.createPosts();
            // ...add more seeds here
            // seeds are created with id to be created only once by a determined order
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
                TopicEntity topic = generateTopicEntity(label);
                topicRepository.save(topic);
            }
            SeedEntity seed = new SeedEntity();
            seed.setId(topicSeedId);
            seed.setName("add fake topics");
            seedRepository.save(seed);
        }
    }

    private void createUser() {
        int userSeedId = 2;
        if (!seedRepository.existsById(userSeedId)) {
            UserEntity user = new UserEntity();
            user.setEmail("user@dev.fr");
            user.setName("User Name");
            String password = this.passwordEncoder.encode("password");
            user.setPassword(password);
            List<TopicEntity> topics = new ArrayList<>();
            TopicEntity java = topicRepository.findByLabel("Java");
            topics.add(java);
            TopicEntity angular = topicRepository.findByLabel("Angular");
            topics.add(angular);
            TopicEntity nest = topicRepository.findByLabel("NestJS");
            topics.add(nest);
            user.setTopics(topics);
            this.userRepository.save(user);
            SeedEntity seed = new SeedEntity();
            seed.setId(userSeedId);
            seed.setName("add fake user");
            seedRepository.save(seed);
        }
    }

    private void createPosts() {
        int postSeedId = 3;
        if (!seedRepository.existsById(postSeedId)) {
            PostEntity post = new PostEntity();
            UserEntity user = userRepository.findByName("user");
            TopicEntity angular = topicRepository.findByLabel("Angular");
            TopicEntity react = topicRepository.findByLabel("React");
            List<String> postTitle = Arrays.asList("Article 1", "Article 2", "Article 3", "Article 4", "Article 5");
            for (String title : postTitle) {
                post = generatePostEntity(title, user, angular);
                this.postRepository.save(post);
            }
            for (String title : postTitle) {
                post = generatePostEntity(title, user, react);
                this.postRepository.save(post);
            }
            SeedEntity seed = new SeedEntity();
            seed.setId(postSeedId);
            seed.setName("add fake posts");
            seedRepository.save(seed);
        }
    }

    private static TopicEntity generateTopicEntity(String label) {
        TopicEntity topic = new TopicEntity();
        topic.setLabel(label);
        topic.setDescription("Description détaillée de " + label + ". Lorem Ipsum is simply dummy text of the " +
                                     "printing and typesetting industry. Lorem Ipsum has been the industry's standard" +
                                     " dummy text ever since the 1500s, when an unknown printer took a galley of type" +
                                     " and scrambled it to make a type specimen book. It has survived not only five " +
                                     "centuries, but also the leap into electronic typesetting, remaining essentially" +
                                     " unchanged. It was popularised in the 1960s with the release of Letraset sheets" +
                                     " containing Lorem Ipsum passages, and more recently with desktop publishing " +
                                     "software like Aldus PageMaker including versions of Lorem Ipsum.");
        return topic;
    }

    private static PostEntity generatePostEntity(String title, UserEntity user, TopicEntity topic) {
        PostEntity post = new PostEntity();
        post.setTitle(title + " sur " + topic.getLabel());
        post.setContent("Content of " + title + ". Lorem Ipsum is simply dummy text of the printing and typesetting" +
                                " industry. Lorem Ipsum has been the industry's standard dummy text ever since the " +
                                "1500s," +
                                " when an unknown printer took a galley of type and scrambled it to make a type " +
                                "specimen" +
                                " book. It has survived not only five centuries, but also the leap into electronic " +
                                "typesetting, remaining essentially unchanged. It was popularised in the 1960s with " +
                                "the" +
                                " release of Letraset sheets containing Lorem Ipsum passages, and more recently with " +
                                "desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        post.setUser(user);
        post.setTopic(topic);
        return post;
    }

}
