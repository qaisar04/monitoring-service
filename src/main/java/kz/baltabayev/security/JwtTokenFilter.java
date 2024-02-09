package kz.baltabayev.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter
public class JwtTokenFilter implements Filter {

    private JwtTokenUtils jwtTokenUtils;
    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        jwtTokenUtils = (JwtTokenUtils) servletContext.getAttribute("jwtTokenUtils");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest)servletRequest).getHeader("Authorization");
        try {
            if (bearerToken != null && bearerToken.startsWith("Bearer ") && jwtTokenUtils.validateToken(bearerToken.substring(7))) {
                Authentication authentication = jwtTokenUtils.authentication(bearerToken.substring(7));
                servletContext.setAttribute("authentication", authentication);
            } else {
                servletContext.setAttribute("authentication", new Authentication(null, false, "Bearer token is null or invalid!"));
            }
        } catch (RuntimeException e) {
            servletContext.setAttribute("authentication", new Authentication(null, false, e.getMessage()));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
