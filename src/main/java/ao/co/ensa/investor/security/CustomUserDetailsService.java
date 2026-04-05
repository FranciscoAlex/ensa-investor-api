package ao.co.ensa.investor.security;

import ao.co.ensa.investor.model.entity.User;
import ao.co.ensa.investor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found with username or email: " + usernameOrEmail));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
            .collect(Collectors.toSet());

        // Allow login regardless of enabled flag; activation is set when user logs in
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            true,             // enabled: never block login; account activated on first login
            true,             // accountNonExpired
            true,             // credentialsNonExpired
            !user.isLocked(), // accountNonLocked
            authorities
        );
    }
}
