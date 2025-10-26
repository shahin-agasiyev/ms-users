package az.ingress.mapper;

import az.ingress.dao.entity.UserEntity;
import az.ingress.model.response.PageableUserResponse;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

import static az.ingress.mapper.UserMapper.USER_MAPPER;

public enum PageableMapper {
    PAGEABLE_MAPPER;

    public static PageableUserResponse mapToPageableResponse(Page<UserEntity> usersPage) {
        return PageableUserResponse.builder()
                .users(usersPage.getContent().stream().map(USER_MAPPER::mapUserEntityToUserResponse).collect(Collectors.toList()))
                .hasNext(usersPage.hasNext())
                .lastPageNumber(usersPage.getTotalPages())
                .totalElements(usersPage.getTotalElements())
                .build();
    }
}
