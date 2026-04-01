package antifraud.example.antifraud.contraints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMsg {

    PHONE_ALREADY_REGISTERED_LOG("Phone Number Duplication: Phone number already registered."),
    AREA_ALREADY_OCCUPIED_LOG("Proximity Violation: Spot within 500m already claimed."),
    LOCATION_MISMATCH_LOG("Location Mismatch: GPS and IP are %s km apart."),
    SUCCESS("Saved successfully!"),
    PHONE_ALREADY_REGISTERED("Phone number already registered."),
    AREA_ALREADY_OCCUPIED("This area is already registered"),
    DEVICE_DUPLICATED("Yuo have been reported before"),
    LOCATION_MISMATCH("Security alert: Location inconsistency detected."),
    LINK_EXPIRED("Link is expired.");
    private final String message;
}
