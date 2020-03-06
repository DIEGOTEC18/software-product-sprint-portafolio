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
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
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

    private static final double NEUTRAL_THRESHOLD = 0.1;
    private static final double SLIGHTLY_THRESHOLD = 0.4;
    private static final double VERY_THRESHOLD = 0.8;

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
            String emoji = (String) entity.getProperty("emoji");
            double score = (double) entity.getProperty("score");

            Comment current = new Comment(username, message, date, score, emoji);

            comments.add(current);

        }

        //Create a JSON from the ArrayList and send it as a response:
        String json = toJsonString(comments);

        //It needs to specify its encoded in UTF-8 so the JSON parser in the client can recognize the emojis:
        response.setContentType("application/json; charset=utf-8");
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

            //Get the sentiment score:
            float score = getCommentSentiment(message);

            //Get emoji based on score:

            String emoji = emojify(score);

            //Get a datastore instance:
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

            //Create a Comment Entity with the values of the request:
            Entity commentEntity = new Entity("Comment");
            commentEntity.setProperty("username", username);
            commentEntity.setProperty("message", message);
            commentEntity.setProperty("date", date);
            commentEntity.setProperty("score", score);
            commentEntity.setProperty("emoji", emoji);

            //Put the Entity in the datastore:
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

    private float getCommentSentiment(String message) throws IOException {

        //Analise sentiment of the comment:

         //Create a text document containing the message text:
        Document commentDoc = Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();

        //Intantiate a Language Service Client:
        LanguageServiceClient languageService = LanguageServiceClient.create();

        //Create a sentiment object with the analyzed text document:
        Sentiment sentiment = languageService.analyzeSentiment(commentDoc).getDocumentSentiment();

        //Get the sentiment score (Negative -1.0 to 1.0 Positive):
        float score = sentiment.getScore();

        //Close connection to the Language Service Client:
        languageService.close();

        return score;

    }

    private String emojify(float score){

        if(score >= -NEUTRAL_THRESHOLD && score <= NEUTRAL_THRESHOLD){

            //Neutral
            return "😐";

        } else if(score > NEUTRAL_THRESHOLD){ 
            
            if(score > NEUTRAL_THRESHOLD && score <= SLIGHTLY_THRESHOLD){

                //Slightly poitive
                return "🙂";

            } else if(score > SLIGHTLY_THRESHOLD && score <= VERY_THRESHOLD){

                //Positive
                return "😄";

            } else {

                //Very positive
                return "😍";

            }

        } else {

            if(score >= -SLIGHTLY_THRESHOLD){

                //Slightly negative
                return "🤨";

            } else if(score < -SLIGHTLY_THRESHOLD && score >= -VERY_THRESHOLD){

                //Negative
                return "☹️";

            } else {

                //Very negative
                return "😡";

            }

        }

    }

}
