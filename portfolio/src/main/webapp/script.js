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

// Fetches a JSON with messages from the server, creates elements with the messages and adds them to the DOM:
function getMessages(){

    fetch("/data").then(response => response.json()).then((messages) => {

        //Iterates the JSON by keys:
        for(let message of messages){

            //Creates an element 'p' with the current message as the inner text and appends it as a child:
            const messageZone = document.getElementById("message-zone");
            let messageElement = document.createElement('p');
            messageElement.innerText = message;
            messageElement.className = "trebuchet";

            messageZone.appendChild(messageElement);

        }

    })

}