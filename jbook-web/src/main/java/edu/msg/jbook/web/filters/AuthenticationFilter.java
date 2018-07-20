package edu.msg.jbook.web.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by iacobd on 28.07.2016.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"*.xhtml"})
public class AuthenticationFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpSession httpSession = httpRequest.getSession(false);
            String requestUrl = httpRequest.getRequestURI();
            boolean isLoginPage = requestUrl.indexOf("/login.xhtml") >= 0;
            boolean isRegisterPage = requestUrl.indexOf("/register.xhtml") >= 0;
            boolean isConfirmationPage = requestUrl.indexOf("/confirm.xhtml") >= 0;
            boolean isUserLoggedIn = httpSession != null
                    && httpSession.getAttribute("isUser") != null;
            boolean isResource = requestUrl.contains("javax.faces.resource");
            boolean isAdmin = httpSession != null
                    && httpSession.getAttribute("isAdmin") != null;
            if (isAdmin || isLoginPage || isUserLoggedIn || isResource || isRegisterPage || isConfirmationPage) {
                if (requestUrl.contains("admin.xhtml")) {
                    if (isAdmin) {
                        chain.doFilter(request, response);
                    } else if (isUserLoggedIn) {
                        httpResponse.sendRedirect(httpRequest.getContextPath()
                                + "/home.xhtml");
                    } else {
                        httpResponse.sendRedirect(httpRequest.getContextPath()
                                + "/login.xhtml");
                    }
                } else if (requestUrl.contains("home.xhtml") || requestUrl.contains("news_feed.xhtml")
                        || requestUrl.contains("user_wall.xhtml") || requestUrl.contains("user_wall_friends.xhtml")
                        || requestUrl.contains("user_wall_events.xhtml") || requestUrl.contains("friend_requests.xhtml")
                        || requestUrl.contains("friend_wall.xhtml") || requestUrl.contains("friend_wall_events.xhtml")
                        || requestUrl.contains("friend_wall_friends.xhtml") || requestUrl.contains("profile.xhtml") || requestUrl.contains("eventLayout.xhtml") || requestUrl.contains("post_details.xhtml")
                        ||requestUrl.contains("like_popup.xhtml") || requestUrl.contains("notifications.xhtml")||
                        requestUrl.contains("user_wall_update_event.xhtml")||requestUrl.contains("search.xhtml") ) {
                    if (isUserLoggedIn) {
                        chain.doFilter(request, response);
                    } else if (isAdmin) {
                        httpResponse.sendRedirect(httpRequest.getContextPath()
                                + "/admin.xhtml");
                    } else {
                        httpResponse.sendRedirect(httpRequest.getContextPath()
                                + "/login.xhtml");
                    }
                }
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath()
                        + "/login.xhtml");
            }
            if (isLoginPage || isResource || isRegisterPage || isConfirmationPage) {
                chain.doFilter(request, response);
            }
        } catch (Throwable t) {
            LOG.error("Authentication error.", t);
        }
    }

    @Override
    public void destroy() {
    }
}
