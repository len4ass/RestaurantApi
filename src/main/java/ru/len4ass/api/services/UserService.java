package ru.len4ass.api.services;

import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.len4ass.api.exceptions.NotFoundException;
import ru.len4ass.api.models.GenericResponse;
import ru.len4ass.api.models.user.UserDto;
import ru.len4ass.api.models.user.UserRole;
import ru.len4ass.api.repositories.UserRepository;
import ru.len4ass.api.utils.Mapper;

@Service
public class UserService implements UserDetailsService {
    @Resource
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public UserDto getUserInfo(Integer id) {
        var optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(String.format("User with id %d does not exist.", id));
        }

        return Mapper.userToUserDto(optionalUser.get());
    }

    public GenericResponse updateUserRole(Integer userId, UserRole newRole) {
        var optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(String.format("No user with id %d found.", userId));
        }

        var user = optionalUser.get();
        var previousRole = user.getUserRole();
        user.setUserRole(newRole);
        userRepository.saveAndFlush(user);

        return new GenericResponse(String.format(
                "User role for user with id %d has been successfully changed from %s to %s.",
                userId,
                previousRole,
                newRole));
    }
}
