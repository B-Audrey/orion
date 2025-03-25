package oc.mdd.seed;

import oc.mdd.entity.*;
import oc.mdd.repository.*;
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

    @Autowired
    private CommentRepository commentRepository;

    @Value("${spring.sql.init.mode}")
    private String initializationMode;

    private final String firstUserMail = "user@dev.fr";
    private final String secondUserMail = "author@dev.fr";


    /**
     * This method is called when the application is started.
     * It is used to create fake data in the database.
     * It is called only if the application is in "always" or "embedded" initialization mode.
     * The run method is overridden to make seed as we want.
     *
     * @param args the command line arguments passed to the application
     */
    @Override
    public void run(String... args) {

        if (initializationMode.equals("always") || initializationMode.equals("embedded")) {
            this.createTopics();
            this.createUser();
            this.createPosts();
            this.addComments();
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
            UserEntity firstUser = new UserEntity();
            firstUser.setEmail(firstUserMail);
            firstUser.setName("User Name");
            UserEntity secondUser = new UserEntity();
            secondUser.setEmail(secondUserMail);
            secondUser.setName("Author Name");
            String password = this.passwordEncoder.encode("password");
            firstUser.setPassword(password);
            secondUser.setPassword(password);
            List<TopicEntity> topics = new ArrayList<>();
            TopicEntity java = topicRepository.findByLabel("Java");
            topics.add(java);
            TopicEntity angular = topicRepository.findByLabel("Angular");
            topics.add(angular);
            firstUser.setTopics(topics);
            TopicEntity nest = topicRepository.findByLabel("NestJS");
            topics.add(nest);
            secondUser.setTopics(topics);
            this.userRepository.save(firstUser);
            this.userRepository.save(secondUser);
            SeedEntity seed = new SeedEntity();
            seed.setId(userSeedId);
            seed.setName("add fake user");
            seedRepository.save(seed);
        }
    }

    private void createPosts() {
        int postSeedId = 3;
        if (!seedRepository.existsById(postSeedId)) {
            PostEntity post;
            UserEntity user = userRepository.findByEmail(secondUserMail);
            TopicEntity angular = topicRepository.findByLabel("Angular");
            TopicEntity react = topicRepository.findByLabel("React");
            List<String> postTitle = Arrays.asList("Article 1", "Article 2", "Article 3");
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

    private void addComments() {
        int commentSeedId = 4;
        if (!seedRepository.existsById(commentSeedId)) {
            PostEntity post = postRepository.findByTitle("Article 1 sur Angular");
            UserEntity user = userRepository.findByEmail(firstUserMail);
            List<String> commentsContent = Arrays.asList("Comment 1", "Comment 2", "Comment 3");
            for (String content : commentsContent) {
                CommentEntity comment = generateCommentEntity(content, user, post);
                this.commentRepository.save(comment);
            }
            SeedEntity seed = new SeedEntity();
            seed.setId(commentSeedId);
            seed.setName("add fake comments");
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

    private static CommentEntity generateCommentEntity(String content, UserEntity user, PostEntity post) {
        CommentEntity comment = new CommentEntity();
        comment.setContent("Content of " + content + " is a correct sized comment");
        comment.setUser(user);
        comment.setPost(post);
        return comment;
    }

}
