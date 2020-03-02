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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
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

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //Get an instance of datastore and prepare a query of the comments sorted by date:
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Comment").addSort("date", SortDirection.DESCENDING);
    
        //Query the datastore and get the result with the comments:
        PreparedQuery results = datastore.prepare(query);

        //Create and ArrayList that stores Comment objects created with the properties form the entities that where queried from the datastore:
        ArrayList<Comment> comments = new ArrayList<Comment>();
        for(Entity entity : results.asIterable()){

            String username = (String) entity.getProperty("username");
            String message = (String) entity.getProperty("message");
            String date = (String) entity.getProperty("date");

            Comment current = new Comment(username, message, date);

            comments.add(current);

        }

        //Create a JSON from the ArrayList and send it as a response:
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

        //Prevents users from posting empty comments by checking the length of the username and message Strings:
        if(username.length() > 1 && message.length() > 1){

            //Get a datastore instance:
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

            //Create a Comment Entity with the values of the request:
            Entity commentEntity = new Entity("Comment");
            commentEntity.setProperty("username", username);
            commentEntity.setProperty("message", message);
            commentEntity.setProperty("date", date);

            //PUT the Entity in the datastore:
            datastore.put(commentEntity);

        }

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
