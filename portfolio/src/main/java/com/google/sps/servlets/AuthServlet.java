package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.sps.data.AuthResponse;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {

      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

      AuthResponse userResponse = new AuthResponse(userEmail, logoutUrl);

      String json = toJsonString(userResponse);

      response.setContentType("application/json; charset=utf-8");
      response.getWriter().println(json);

    } else {

      String urlToRedirectToAfterUserLogsIn = "/#comment-button-div";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      response.getWriter().println("<head><meta name='viewport' content='width=device-width, initial-scale=1.0'><title>Comments Login</title><link rel='stylesheet' href='style.css'><link rel='shortcut icon' href='/images/favicon.ico' type='image/x-icon'><link rel='icon' href='/images/favicon.ico' type='image/x-icon'></head>");
      response.getWriter().println("<h3 class = 'login-title lucida'>Hello stranger...</h3>");
      response.getWriter().println("<p class = 'login-text trebuchet'>Login with Google to post a comment: </p><div class = 'center'><a href=\"" + loginUrl + "\"><img src = '/images/google_login.png' id = 'google-login-icon'</a></div>");

      //response.sendRedirect(loginUrl);

    }
  }

    private String toJsonString(AuthResponse response){

        Gson gson = new Gson();
        String json = gson.toJson(response);
        return json;

    }

}