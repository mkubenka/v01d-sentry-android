package sk.v01d.sentry;

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.SentrySender;

import retrofit.RestAdapter;

@ReportsCrashes(
        formKey = "", // This is required for backward compatibility but not used
        formUri = "" // TODO: Add your Sentry DSN.
)
public class EventApplication extends Application {
    public static final String API_SERVER = "http://192.168.56.1:5000";
    private static final String TAG = "EventApplication";

    private EventService eventService;

    @Override public void onCreate() {
        super.onCreate();

        ACRA.init(this);
        ACRA.getErrorReporter().setReportSender(new SentrySender());

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_SERVER)
                .build();
        eventService = restAdapter.create(EventService.class);
    }

    public EventService getEventService() {
        return eventService;
    }
}
