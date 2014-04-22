package sk.v01d.sentry;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

public interface EventService {
    @GET("/api/v1/events")
    void listEvents(Callback<EventsResponse> picturesCallback);

    static class EventsResponse {
        List<Event> events;
    }
}
