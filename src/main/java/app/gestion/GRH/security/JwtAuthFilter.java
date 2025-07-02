package app.gestion.GRH.security;

import app.gestion.GRH.model.JwtUtil;
import app.gestion.GRH.repository.IndividuRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final IndividuRepository individuRepository;
    private final TokenBlacklist tokenBlacklist;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        // 🚫 Vérification si le token est blacklisté
        if (tokenBlacklist.isBlacklisted(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expiré ou invalide");
            return;
        }

        final String individuEmail = jwtUtil.extractUsername(token);

        if (individuEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userOpt = individuRepository.findByEmail(individuEmail);
            if (userOpt.isPresent() && jwtUtil.validateToken(token, individuEmail)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        individuEmail, null, List.of() // ou authorities selon rôles
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
