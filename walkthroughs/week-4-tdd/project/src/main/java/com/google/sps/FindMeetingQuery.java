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

//This should return a List<TimeRange> of all the possible TimeRange meetings:
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    ArrayList<TimeRange> possibleTimes = new ArrayList<TimeRange>();

    long requestDuration = request.getDuration();
    Collection<String> requestAtt = request.getAttendees();

    //Print the requirenments:
    System.out.println("----------->");
    System.out.println("REQUEST:");
    System.out.println("duration: " + requestDuration);
    System.out.println("attendees:");
    for(String attendee : requestAtt){

        System.out.println(attendee);

    }

    //If the request has no attendees, return the whole day:
    if(requestAtt.isEmpty()){

        System.out.println("There are no attendees.");
        return Arrays.asList(TimeRange.WHOLE_DAY);

    }

    //Check for meetings that are longer than a day:
    if(requestDuration > TimeRange.WHOLE_DAY.duration()){

        System.out.println("The requested time is longer than a day.");
        //Return an empty list:
        //return Arrays.asList();
        return possibleTimes;

    }

    //Check every event and split the day into different possible times for the meeting before and after the events:
    Iterator<Event> iterator = events.iterator();

    //I know this index shouldn't exist:
    int index = 0;
    int lastEventEnd = TimeRange.START_OF_DAY;
    boolean shareAttendees = false;

    while (iterator.hasNext()) {

        Event currentEvent = iterator.next();

        System.out.println(">-------------<");
        System.out.println("Index: " + index);
        System.out.println(currentEvent.getWhen().start());

        /*if(iterator.hasNext()){

            System.out.println(iterator.next().getWhen().start());

        }*/
        
        TimeRange when = currentEvent.getWhen();
        Set<String> attendees = currentEvent.getAttendees();

        for(String currentAttendee : requestAtt){

            if(attendees.contains(currentAttendee)){

                shareAttendees = true;

                //Check if you can allocate one before:
                if(when.start() - lastEventEnd >= requestDuration){

                    TimeRange possibleBefore = TimeRange.fromStartEnd(lastEventEnd, when.start(), false);
                    possibleTimes.add(possibleBefore);

                }

                //Check if its the last one of the day:
                if(!iterator.hasNext()){

                    if(TimeRange.END_OF_DAY - when.end() >= requestDuration){

                        TimeRange possibleAfter = TimeRange.fromStartEnd(when.end(), TimeRange.END_OF_DAY, true);
                        possibleTimes.add(possibleAfter);

                    }

                } else {

                    //Its not the last one of the day:
                    lastEventEnd = when.end();

                }

                index++;

                break;

            }

        }

    }

    //If none of the given attendees are attending any of the known events:
    if(!shareAttendees){

        return Arrays.asList(TimeRange.WHOLE_DAY);

    }

    //throw new UnsupportedOperationException("TODO: Implement this method.");

    return possibleTimes;

  }
}
