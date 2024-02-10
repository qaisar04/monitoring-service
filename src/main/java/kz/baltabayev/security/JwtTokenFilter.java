package kz.baltabayev.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * A filter that intercepts all incoming HTTP requests and checks for the presence of a JWT in the Authorization header.
 * If a valid JWT is found, it authenticates the user and stores the authentication in the servlet context.
 * If no JWT is found or the JWT is invalid, it stores an unauthenticated Authentication object in the servlet context.
 */
@WebFilter(urlPatterns = "/*", initParams = @WebInitParam(name = "order", value = "1"))
public class JwtTokenFilter implements Filter {

    private JwtTokenUtils jwtTokenUtils;
    private ServletContext servletContext;

    /**
     * Initializes the filter.
     *
     * @param filterConfig the filter configuration
     * @throws ServletException if an error occurs during initialization
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        jwtTokenUtils = (JwtTokenUtils) servletContext.getAttribute("jwtTokenUtils");
    }

    /**
     * Checks for the presence of a JWT in the Authorization header of the incoming request.
     * If a valid JWT is found, it authenticates the user and stores the authentication in the servlet context.
     * If no JWT is found or the JWT is invalid, it stores an unauthenticated Authentication object in the servlet context.
     *
     * @param servletRequest the incoming request
     * @param servletResponse the outgoing response
     * @param filterChain the filter chain
     * @throws IOException if an I/O error occurs during this filter's processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        try {
            if (bearerToken != null && bearerToken.startsWith("Bearer ") && jwtTokenUtils.validateToken(bearerToken.substring(7))) {
                Authentication authentication = jwtTokenUtils.authentication(bearerToken.substring(7));
                servletContext.setAttribute("authentication", authentication);
            } else {
                servletContext.setAttribute("authentication", new Authentication(null, null, false, "Bearer token is null or invalid!"));
            }
        } catch (RuntimeException e) {
            servletContext.setAttribute("authentication", new Authentication(null, null, false, e.getMessage()));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}