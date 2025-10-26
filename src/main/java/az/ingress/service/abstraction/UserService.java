package az.ingress.service.abstraction;

import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.criteria.UserCriteria;
import az.ingress.model.dto.EventDto;
import az.ingress.model.request.UserRequest;
import az.ingress.model.response.PageableUserResponse;
import az.ingress.model.response.UserResponse;

import javax.validation.Valid;

public interface UserService {
    void create(@Valid UserRequest request);

    UserResponse getById(Long id);

    void update(Long id, @Valid UserRequest request);

    void delete(Long id);

    PageableUserResponse getUsers(PageCriteria pageCriteria, UserCriteria userCriteria);

    void notifyUsersAboutNewEvent(EventDto eventDto);
}
