package az.ingress.mapper;

import az.ingress.dao.entity.UserEntity;
import az.ingress.model.request.UserRequest;
import az.ingress.model.response.UserResponse;

public enum UserMapper {
    USER_MAPPER;

    public UserEntity mapUserRequestToUserEntity(UserRequest request) {
        return UserEntity.builder()
                .age(request.getAge())
                .name(request.getName())
                .lastName(request.getLastName())
                .userName(request.getUserName())
                .password(request.getPassword())
                .build();
    }

    public UserResponse mapUserEntityToUserResponse(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .age(entity.getAge())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .userName(entity.getUserName())
                .build();
    }

    public void updateUser(UserRequest request, UserEntity entity) {
        entity.setAge(request.getAge());
        entity.setName(request.getName());
        entity.setLastName(request.getLastName());
        entity.setUserName(request.getUserName());
        entity.setPassword(request.getPassword());
    }
}
