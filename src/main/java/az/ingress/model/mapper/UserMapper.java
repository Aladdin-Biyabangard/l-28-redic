package az.ingress.model.mapper;

import az.ingress.dao.entity.UserEntity;
import az.ingress.model.dto.UserRequest;
import az.ingress.model.dto.UserResponse;

public enum UserMapper {
    USER_MAPPER;

    public UserResponse toResponse(UserEntity entity) {
        return new UserResponse(
                entity.getId(),
                entity.getUserName(),
                entity.getEmail()
        );
    }

    public UserEntity toEntity(UserRequest dto) {
        return UserEntity.builder()
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }
}
