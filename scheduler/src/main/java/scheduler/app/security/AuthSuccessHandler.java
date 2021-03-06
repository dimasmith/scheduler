package scheduler.app.security;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import scheduler.app.models.UserSecureDetails;
import scheduler.app.services.users.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Inject
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();

        scheduler.app.models.User user = userService.findByLogin(principal.getUsername());
        UserSecureDetails userSecureDetails = userService.loadUserSecureDetails(user.getId());

        Map<String, String> map = Maps.newLinkedHashMap();
        map.put(AuthResponse.AUTH_RESULT, "Logged in successfully");
        map.put(AuthResponse.USER_NAME, user.getUsername());
        map.put(AuthResponse.USER_ROLE, userSecureDetails.getRole().toString());

        PrintWriter writer = response.getWriter();
        writer.write(new Gson().toJson(new AuthResponse(HttpStatus.SC_OK, map)));
        writer.flush();
    }
}
