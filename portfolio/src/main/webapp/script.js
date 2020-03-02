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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {

  const greetings =
      ['IM IN', 'Lets do this', '你好，世界！', 'Hee Hee'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

// Fetches a JSON with comments from the server, creates elements with the comments and adds them to the DOM:
function getComments(){

    fetch("/data").then(response => response.json()).then((comments) => {

        //Iterates the JSON by keys:
        for(let comment of comments){

            //Creates a div that displays the comment with its atributes and adds it to the DOM:
            const commentZone = document.getElementById("comment-zone");

            const currentCommentElement = commentElement(comment.username, comment.message, comment.date);

            commentZone.appendChild(currentCommentElement);

        }

    })

}

function commentElement(username, message, date){

    let divElement = document.createElement('div');
    let commentHeader = document.createElement('div');
    let dateDiv = document.createElement('div');
    let textDiv = document.createElement('div');
    let userHead = document.createElement('a');
    let dateHead = document.createElement('a');
    let commentText = document.createElement('p');

    userHead.text = username;
    dateHead.text = date;
    commentText.innerText = message;

    dateDiv.className = "comment-date-div";
    textDiv.className = "comment-text-div";
    divElement.className = "comment-div";
    commentHeader.className = "comment-header";
    userHead.className = "trebuchet comment-username";
    dateHead.className = "trebuchet comment-date";
    commentText.className = "trebuchet comment-text";

    dateDiv.appendChild(dateHead);
    textDiv.appendChild(commentText);

    commentHeader.appendChild(userHead);
    commentHeader.appendChild(dateDiv);

    divElement.appendChild(commentHeader);
    divElement.appendChild(textDiv);

    return divElement;

}