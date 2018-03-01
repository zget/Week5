package com.example.demo.Security;

import com.example.demo.Model.Role;
import com.example.demo.Model.User;
import com.example.demo.Repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

//import javax.management.relation.Role;

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService {


    private UserRepository userRepository;
    public SSUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) {

        try {
            User user = userRepository.findByUserName(username);
            if (user == null) {
                return null;
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getPassword(),
                    getAuthorities(user));
        } catch (Exception e) {
            throw new UsernameNotFoundException("user not found");

        }

    }
    private Set<GrantedAuthority> getAuthorities (User user){
        Set<GrantedAuthority> authorities
                = new HashSet<GrantedAuthority>();
        for (Role role : user.getRoles()) {
            GrantedAuthority grantedAuthority
                    = new SimpleGrantedAuthority(role.getRolename());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }

}
