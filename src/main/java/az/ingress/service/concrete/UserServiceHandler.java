package az.ingress.service.concrete;

import az.ingress.dao.entity.UserEntity;
import az.ingress.dao.repository.UserRepository;
import az.ingress.exception.NotFoundException;
import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.criteria.UserCriteria;
import az.ingress.model.dto.EventDto;
import az.ingress.model.request.UserRequest;
import az.ingress.model.response.PageableUserResponse;
import az.ingress.model.response.UserResponse;
import az.ingress.model.specification.UserSpecification;
import az.ingress.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static az.ingress.exception.ErrorMessage.USER_NOT_FOUND;
import static az.ingress.mapper.PageableMapper.PAGEABLE_MAPPER;
import static az.ingress.mapper.UserMapper.USER_MAPPER;
import static az.ingress.model.constants.CriteriaConstants.COUNT_DEFAULT_VALUE;
import static az.ingress.model.constants.CriteriaConstants.PAGE_DEFAULT_VALUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceHandler implements UserService {

    private final UserRepository userRepository;

    @Override
    public void create(UserRequest request) {
        log.info("Creating user with username: {}", request.getUserName());
        var user = USER_MAPPER.mapUserRequestToUserEntity(request);
        userRepository.save(user);
        log.debug("user created: {}", user);
    }

    @Override
    public UserResponse getById(Long id) {
        log.info("Fetching user with id: {}", id);
        var entity = fetchIfExists(id);
        log.debug("User found: {}", entity);
        return USER_MAPPER.mapUserEntityToUserResponse(entity);
    }

    @Override
    public void update(Long id, UserRequest request) {
        log.info("Updating user with id: {}", id);
        var entity = fetchIfExists(id);
        USER_MAPPER.updateUser(request, entity);
        userRepository.save(entity);
        log.debug("User updated: {}", entity);
    }

    @Override
    public void delete(Long id) {
        log.warn("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public PageableUserResponse getUsers(PageCriteria pageCriteria, UserCriteria userCriteria) {
        var pageNumber = pageCriteria.getPage() == null ? PAGE_DEFAULT_VALUE : pageCriteria.getPage();
        var count = pageCriteria.getCount() == null ? COUNT_DEFAULT_VALUE : pageCriteria.getCount();

        log.info("Fetching users with page: {}, count: {}", pageNumber, count);
        var usersPage = userRepository.findAll(new UserSpecification(userCriteria), PageRequest.of(pageNumber, count));
        log.debug("Total users found: {}", usersPage.getTotalElements());
        return PAGEABLE_MAPPER.mapToPageableResponse(usersPage);
    }

    private UserEntity fetchIfExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with id: {} not found", id);
                    return new NotFoundException(USER_NOT_FOUND.getMessage(), id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public void notifyUsersAboutNewEvent(EventDto eventDto) {
        log.info("Notifying users about new event: {}", eventDto.getName());

        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();

        users.forEach(user -> {
            log.info("Sending notification to user: {} about event: {}",
                    user.getUserName(), eventDto.getName());
        });
    }
}
