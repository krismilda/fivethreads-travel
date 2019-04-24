package lt.fivethreads.services;

import lt.fivethreads.entities.Trip;
import lt.fivethreads.entities.TripCancellation;
import lt.fivethreads.entities.TripMember;

public interface CreateNotificationService {
    void createNotificationsForApproval(Trip trip, String name);
    void createNotificationForApprovalTripMember(TripMember tripMember, String name);
    void createNotificationInformationChanged(TripMember tripMember, String name);
    void createNotificationForCancellation(TripCancellation tripCancellation, String name);
    void createNotificationForApproval(TripMember tripMember, String name);
}
