package az.ingress.service.concret;

import az.ingress.config.RedisUtil;
import az.ingress.dao.entity.UserEntity;
import az.ingress.dao.repository.UserRepository;
import az.ingress.model.dto.CustomPage;
import az.ingress.model.dto.CustomPageRequest;
import az.ingress.model.dto.UserRequest;
import az.ingress.model.dto.UserResponse;
import az.ingress.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

import static az.ingress.model.enums.UserStatus.DELETED;
import static az.ingress.model.mapper.UserMapper.USER_MAPPER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisUtil redisUtil;

    private final String CACHE_KEY = "user-ms:user-id:";

    @Override
    public UserResponse createUser(UserRequest request) {
        var entity = USER_MAPPER.toEntity(request);
        var response = USER_MAPPER.toResponse(userRepository.save(entity));
        redisUtil.saveToCacheSafe((CACHE_KEY + response.getId()), response, 1L, ChronoUnit.MINUTES);
        return response;
    }

    @Override
    public void updateUser(Long id, UserRequest request) {
        var user = fetchUserIfExists(id);
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        var response = USER_MAPPER.toResponse(userRepository.save(user));
        redisUtil.saveToCacheSafe((CACHE_KEY + response.getId()), response, 1L, ChronoUnit.MINUTES);
    }

    @Override
    public UserResponse getUser(Long id) {
        var cachedUser = redisUtil.getBucketSafe(CACHE_KEY + id);
        if (cachedUser != null) {
            return (UserResponse) cachedUser;
        }

        var user = fetchUserIfExists(id);
        var response = USER_MAPPER.toResponse(user);

        redisUtil.saveToCacheSafe(CACHE_KEY + id, response, 1L, ChronoUnit.MINUTES);
        return response;
    }

    @Override
    public CustomPage<UserResponse> getUsers(CustomPageRequest request) {
        var pageable = PageRequest.of(request.getPage(), request.getSize());
        var users = userRepository.findAll(pageable);
        return new CustomPage<>(
                users.getContent().stream().map(USER_MAPPER::toResponse).toList(),
                users.getNumber(),
                users.getSize()
        );
    }

    @Override
    public void deleteUser(Long id) {
        var user = fetchUserIfExists(id);
        user.setStatus(DELETED);
        userRepository.save(user);

        redisUtil.deleteFromCacheSafe(CACHE_KEY + id);
    }

    @Override
    public UserEntity fetchUserIfExists(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NullPointerException("User not found"));
    }
}
