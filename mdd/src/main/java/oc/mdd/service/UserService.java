package oc.mdd.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import oc.mdd.dto.UserPasswordDto;
import oc.mdd.dto.UserSigninDto;
import oc.mdd.dto.UserUpdateDto;
import oc.mdd.entity.UserEntity;
import oc.mdd.model.UserModel;
import oc.mdd.model.error.ForbiddenException;
import oc.mdd.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserModel convertToUserModel(UserEntity user) {
        return new UserModel(user.getUuid(),
                             user.getEmail(),
                             user.getName(),
                             user.getCreated_at(),
                             user.getUpdated_at());
    }

    public UserEntity getUserByEmail(String email) throws Exception {
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new Exception("User not found"));
    }

    public UserEntity findUserByNameOrMail(String search) {
        if(search.contains("@")) {
            return userRepository.findByEmail(search);
        } else {
            return userRepository.findByName(search);
        }
    }

    public UserEntity registerUser(UserSigninDto userSinginDto) throws Exception {
        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(userSinginDto.getEmail()) != null) {
            throw new Exception("Email Already Exists" );
        }
        // Vérifier si le nom existe déjà
        if(userRepository.findByName(userSinginDto.getName()) != null) {
            throw new Exception("Name Already Exists");
        }
        // Créer et enregistrer le nouvel utilisateur
        UserEntity newUser = new UserEntity();
        newUser.setEmail(userSinginDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userSinginDto.getPassword()));
        newUser.setName(userSinginDto.getName());
        return userRepository.save(newUser);
    }


    public UserEntity updateUser(String uuid, UserUpdateDto userUpdateDto) {
        UserEntity user = userRepository.findByUuid(uuid);
        user.setName(userUpdateDto.getName());
        user.setEmail(userUpdateDto.getEmail());
        return userRepository.save(user);
    }

    public void updatePassword(String uuid, UserPasswordDto userPasswordDto) throws Exception {
        UserEntity user = userRepository.findByUuid(uuid);
        // compare old password before saving new one
        if (!passwordEncoder.matches(userPasswordDto.getActualPassword(), user.getPassword())) {
            throw new Exception("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(userPasswordDto.getNewPassword()));
        userRepository.save(user);
    }
}
