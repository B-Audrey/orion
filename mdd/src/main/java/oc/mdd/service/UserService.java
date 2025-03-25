package oc.mdd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.dto.UserPasswordDto;
import oc.mdd.dto.UserSigninDto;
import oc.mdd.dto.UserUpdateDto;
import oc.mdd.entity.TopicEntity;
import oc.mdd.entity.UserEntity;
import oc.mdd.repository.TopicRepository;
import oc.mdd.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TopicRepository topicRepository;

    /**
     * Get user by email
     *
     * @param email the email of the user
     * @return the user
     */
    public UserEntity getUserByEmail(String email) throws Exception {
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new Exception("User not found"));
    }

    /**
     * Get user by uuid
     *
     * @param search the email or the name of the user
     * @return the user
     */
    public UserEntity findUserByNameOrMail(String search) {
        if (search.contains("@")) {
            return userRepository.findByEmail(search);
        } else {
            return userRepository.findByName(search);
        }
    }

    /**
     * Save a new user if data is valid
     *
     * @param userSinginDto the user to register
     * @return the new created user
     */
    public UserEntity registerUser(UserSigninDto userSinginDto) throws Exception {
        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(userSinginDto.getEmail()) != null) {
            throw new Exception("Email Already Exists");
        }
        // Vérifier si le nom existe déjà
        if (userRepository.findByName(userSinginDto.getName()) != null) {
            throw new Exception("Name Already Exists");
        }
        // Créer et enregistrer le nouvel utilisateur
        UserEntity newUser = new UserEntity();
        newUser.setEmail(userSinginDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userSinginDto.getPassword()));
        newUser.setName(userSinginDto.getName());
        return userRepository.save(newUser);
    }

    /**
     * Update user email and name by a save method if data is valid
     *
     * @param uuid          the uuid of the user
     * @param userUpdateDto the new data
     * @return the updated user
     */
    public UserEntity updateUser(String uuid, UserUpdateDto userUpdateDto) throws Exception {
        UserEntity isFoundByEmail = userRepository.findByEmail(userUpdateDto.getEmail());
        if (isFoundByEmail != null && !isFoundByEmail.getUuid().equals(uuid)) {
            throw new Exception("Email Already Exists");
        }
        UserEntity isFoundByName = userRepository.findByName(userUpdateDto.getName());
        if (isFoundByName != null && !isFoundByName.getUuid().equals(uuid)) {
            throw new Exception("Name Already Exists");
        }
        UserEntity user = userRepository.findByUuid(uuid);
        user.setName(userUpdateDto.getName());
        user.setEmail(userUpdateDto.getEmail());
        return userRepository.save(user);
    }

    /**
     * Save user encoded password if data is valid
     *
     * @param uuid            the uuid of the user
     * @param userPasswordDto the ancient and new password
     */
    public void updatePassword(String uuid, UserPasswordDto userPasswordDto) throws Exception {
        UserEntity user = userRepository.findByUuid(uuid);
        // compare old password before saving new one
        if (!passwordEncoder.matches(userPasswordDto.getActualPassword(), user.getPassword())) {
            throw new Exception("Error");
        }
        user.setPassword(passwordEncoder.encode(userPasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Add a topic to the user
     *
     * @param userUuid  the uuid of the user
     * @param topicUuid the uuid of the topic
     * @return the updated user
     */
    public UserEntity addTopic(String userUuid, String topicUuid) {
        UserEntity user = userRepository.findByUuid(userUuid);
        TopicEntity topic = topicRepository.findByUuid(topicUuid);
        user.getTopics().add(topic);
        return userRepository.save(user);
    }

    /**
     * Remove a topic from the user
     *
     * @param userUuid  the uuid of the user
     * @param topicUuid the uuid of the topic
     * @return the updated user
     */
    public UserEntity removeTopic(String userUuid, String topicUuid) {
        UserEntity user = userRepository.findByUuid(userUuid);
        TopicEntity topic = topicRepository.findByUuid(topicUuid);
        user.getTopics().remove(topic);
        return userRepository.save(user);
    }
}
