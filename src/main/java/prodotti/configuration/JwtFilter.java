package prodotti.configuration;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import prodotti.service.auth.AuthService;
import prodotti.shared.UserDataShared;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class JwtFilter implements Filter {

    private final AuthService authService;

    public JwtFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (path.startsWith("/swagger-ui")) {
            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/v3/api-docs")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"Missing or invalid Authorization header - login action to ms-anagrafica\"}");
            return;
        }
        final String token = authHeader.substring(7);

        if(!authService.existValidToken(token)){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"Missing Token On Cache - login action to ms-anagrafica \"}");
            return;
        }

        try {
            authService.userFindByToken(token);
        } catch (AuthException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"Missing Token On Cache - login action to ms-anagrafica \"}");
            return;
        }

        final UserDataShared userDataShared = authService.getUserDataShared();

        req.setAttribute("authenticatedUser", userDataShared.getUsername());

        chain.doFilter(request, response);
    }
}
