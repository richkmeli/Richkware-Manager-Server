//package it.richkmeli.rms.security;
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//
//@Component
//public class MyAuthenticationProvider implements AuthenticationProvider {
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = authentication.getCredentials().toString();
//        System.out.println(username + " " + password);
//        if ("richk@i.it".equals(username) && "00000000".equals(password)) {
//            System.out.println("LOGGED");
//            return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
//        } else {
//            System.out.println("Authentication failed");
//            throw new BadCredentialsException("Authentication failed");
//        }
//    }
//    @Override
//    public boolean supports(Class<?>aClass) {
//        return aClass.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}
