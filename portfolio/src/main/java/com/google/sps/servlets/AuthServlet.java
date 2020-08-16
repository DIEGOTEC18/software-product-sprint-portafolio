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

      response.sendRedirect(loginUrl);

    }
  }

    private String toJsonString(AuthResponse response){

        Gson gson = new Gson();
        String json = gson.toJson(response);
        return json;

    }

}