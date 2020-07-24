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

package com.google.sps;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public final class FindMeetingQuery {

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    ArrayList<TimeRange> possibleTimes = new ArrayList<TimeRange>();

    long requestDuration = request.getDuration();
    Collection<String> requestAtt = request.getAttendees();

    //If the request has no attendees, return the whole day:
    if(requestAtt.isEmpty()){

        return Arrays.asList(TimeRange.WHOLE_DAY);

    }

    //Check for meetings that are longer than a day:
    if(requestDuration > TimeRange.WHOLE_DAY.duration()){

        //Return an empty list:
        return possibleTimes;

    }

    Iterator<Event> iterator = events.iterator();

    //Use the start of the day as the last know event:
    int lastEventEnd = TimeRange.START_OF_DAY;
    boolean shareAttendees = false;

    //Iterate all the known events:
    while (iterator.hasNext()) {

        Event currentEvent = iterator.next();
        
        TimeRange when = currentEvent.getWhen();
        Set<String> attendees = currentEvent.getAttendees();

        //Check if the current event shares any attendee with the request for meeting:
        for(String currentAttendee : requestAtt){

            if(attendees.contains(currentAttendee)){

                shareAttendees = true;

                //Check if you can allocate the meeting before:
                if(when.start() - lastEventEnd >= requestDuration){

                    TimeRange possibleBefore = TimeRange.fromStartEnd(lastEventEnd, when.start(), false);
                    possibleTimes.add(possibleBefore);

                }

                //Check if its the last event of the day:
                if(!iterator.hasNext()){

                    //Check if you can allocate a meeting between the last event and the end of the day:
                    if(TimeRange.END_OF_DAY - when.end() >= requestDuration){

                        //Check for nested events:
                        if(lastEventEnd < when.end()){

                            TimeRange possibleAfter = TimeRange.fromStartEnd(when.end(), TimeRange.END_OF_DAY, true);
                            possibleTimes.add(possibleAfter);

                        } else {

                            TimeRange possibleAfter = TimeRange.fromStartEnd(lastEventEnd, TimeRange.END_OF_DAY, true);
                            possibleTimes.add(possibleAfter);

                        }

                    }

                } else {

                    //Its not the last one of the day:
                    //Check for nested events too:
                    if(lastEventEnd < when.end()){

                       lastEventEnd = when.end();

                    }

                }

                //Avoid repeating this process for the same event when more than one attendee is shared:
                break;

            }

        }

    }

    //If none of the given attendees are attending any of the known events:
    if(!shareAttendees){

        return Arrays.asList(TimeRange.WHOLE_DAY);

    }

    return possibleTimes;

  }
}
