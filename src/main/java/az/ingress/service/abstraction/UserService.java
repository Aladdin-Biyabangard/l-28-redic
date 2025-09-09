package az.ingress.service.abstraction;

import az.ingress.dao.entity.UserEntity;
import az.ingress.model.dto.CustomPage;
import az.ingress.model.dto.CustomPageRequest;
import az.ingress.model.dto.UserRequest;
import az.ingress.model.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest request);

    void updateUser(Long id, UserRequest request);

    UserResponse getUser(Long id);

    CustomPage<UserResponse> getUsers(CustomPageRequest request);

    void deleteUser(Long id);

    UserEntity fetchUserIfExists(Long id);

}
