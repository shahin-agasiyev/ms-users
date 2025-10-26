package az.ingress.controller;

import az.ingress.model.criteria.PageCriteria;
import az.ingress.model.criteria.UserCriteria;
import az.ingress.model.request.UserRequest;
import az.ingress.model.response.PageableUserResponse;
import az.ingress.model.response.UserResponse;
import az.ingress.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void create(@RequestBody @Valid UserRequest request) {
        userService.create(request);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@PathVariable Long id,
                       @RequestBody @Valid UserRequest request) {
        userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping
    public PageableUserResponse getUsers(PageCriteria pageCriteria, UserCriteria userCriteria) {
        return userService.getUsers(pageCriteria, userCriteria);
    }
}
