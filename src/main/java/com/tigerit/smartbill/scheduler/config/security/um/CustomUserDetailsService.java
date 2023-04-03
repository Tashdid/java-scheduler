package com.tigerit.smartbill.scheduler.config.security.um;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;


//@Qualifier("customUserDetailsService")
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
//    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return null;
    }

    // This method is used by JWTAuthenticationFilter
//    @Transactional
    public UserDetails loadUserById(Long id) {
        return null;
    }
}