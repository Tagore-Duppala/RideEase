package com.project.rideEase.services;

import com.project.rideEase.entities.User;
import com.project.rideEase.exceptions.ResourceNotFoundException;
import com.project.rideEase.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+id));
    }
}
