//package org.thesix.member.security.filter;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Log4j2
//public class LoginFilter extends AbstractAuthenticationProcessingFilter implements AuthenticationFailureHandler {
//
//    public LoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
//        super(defaultFilterProcessesUrl, authenticationManager);
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//        String id = request.getParameter("id");
//        String password = request.getParameter("password");
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(id, password);
//
//        log.info("인가토큰 : "+authenticationToken);
//
//        Authentication result = this.getAuthenticationManager().authenticate(authenticationToken);
//
//        log.info("인가결과: " + result);
//
//
//        return result;
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//
//        log.info("success");
//        super.successfulAuthentication(request, response, chain, authResult);
//    }
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@fail남 인증");
//    }
//}
