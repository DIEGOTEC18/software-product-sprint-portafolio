// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.sps.data.Comment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.time.LocalDate;
import com.google.gson.Gson;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private ArrayList<Comment> comments = new ArrayList<Comment>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String json = toJsonString(comments);

        response.setContentType("application/json;");
        response.getWriter().println(json);

  }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        LocalDate messageDate = LocalDate.now();

        String username = request.getParameter("username");
        String message = request.getParameter("message");
        String date = messageDate.toString();

        Comment currentComment = new Comment(username, message);

        comments.add(currentComment);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("username", username);
        commentEntity.setProperty("message", message);
        commentEntity.setProperty("date", date);

        datastore.put(commentEntity);

        // Redirect back to the HTML page.
        response.sendRedirect("/index.html");

    }

    //Converts the ArrayList to JSON using GSON:
    private String toJsonString(ArrayList<Comment> commentList){

        Gson gson = new Gson();
        String json = gson.toJson(commentList);
        return json;

    }

}
