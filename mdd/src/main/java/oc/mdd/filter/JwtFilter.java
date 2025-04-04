package oc.mdd.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oc.mdd.config.CustomUserDetailsService;
import oc.mdd.model.error.UnauthorizedException;
import oc.mdd.utils.JwtUtils;
import oc.mdd.entity.UserEntity;
import oc.mdd.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    /**
     * This method is used to filter the request and check if the token is valid
     * extract the user email from the token
     * load the user by its found email
     * if the token is valid the user is authenticated and the user is added to the request to be accessed by the controllers
     * if the token is not valid the request is not authenticated
     * then goes to next filter
     *
     * @param req   the request
     * @param res   the response
     * @param chain the filter chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException {
        final String authorizationHeader = req.getHeader("Authorization");
        String useremail = null;
        String jwtToken = null;
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwtToken = authorizationHeader.substring(7);
                useremail = jwtUtils.extractUserEmail(jwtToken);
            }
            if (useremail != null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(useremail);
                if (jwtUtils.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    UserEntity connectedUser = userService.getUserByEmail(useremail);
                    if (connectedUser == null) {
                        throw new UnauthorizedException("User not found");
                    }
                    req.setAttribute("user", connectedUser);
                }
            } else {
                throw new UnauthorizedException("Invalid JWT token");
            }
            chain.doFilter(req, res);
        } catch (Exception e) {
            throw new ServletException();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> PUBLIC_PATHS = List.of("/api/users/new",
                                            "/api/auth/login",
                                            "/api/auth/refresh"
        );
        String path = request.getServletPath();
        return PUBLIC_PATHS.contains(path);
    }
}
