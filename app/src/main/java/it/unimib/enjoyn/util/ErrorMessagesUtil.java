package it.unimib.enjoyn.util;

import android.app.Application;
import static it.unimib.enjoyn.util.Constants.*;

import it.unimib.enjoyn.R;

public class ErrorMessagesUtil {

    private final Application application;

    public ErrorMessagesUtil(Application application){
        this.application = application;
    }

    public int getUserErrorMessage(String error){
        switch (error){
            case USER_ALREADY_LOGGED_ERROR:
                return R.string.user_already_logged_error;
            case USER_NOT_LOGGED_ERROR:
                return R.string.user_not_logged_error;
            case SIGNUP_ERROR:
                return R.string.signup_error;
            case SIGNIN_ERROR:
                return R.string.signin_error;
            case SESSION_REFRESH_ERROR:
                return R.string.session_refresh_error;
            case USER_LOCAL_FETCH_ERROR:
                return R.string.user_local_fetch_error;
            case USER_LOCAL_UPDATE_ERROR:
                return R.string.user_local_update_error;
            case USER_LOCAL_CREATION_ERROR:
                return R.string.user_local_creation_error;
            case USER_LOCAL_DELETION_ERROR:
                return R.string.user_local_deletion_error;
            case USER_REMOTE_FETCH_ERROR:
                return R.string.user_remote_fetch_error;
            case USER_REMOTE_UPDATE_ERROR:
                return R.string.user_remote_update_error;
            case EMAIL_SENDING_ERROR:
                return R.string.error_sending_confermation_email;
            case EMAIL_RESET_PASSWORD_SENDING_ERROR:
                return R.string.email_reset_password_sending_error;
            default:
                return R.string.unexpected_error;
        }
    }

    public int getInterestErrorMessage(String error){
        switch (error){
            case INTEREST_LOCAL_FETCH_ERROR:
                return R.string.interest_local_fetch_error;
            case INTEREST_LOCAL_CREATION_ERROR:
                return R.string.interest_local_creation_error;
            case INTEREST_LOCAL_DELETION_ERROR:
                return R.string.interest_local_deletion_error;
            case INTEREST_REMOTE_FETCH_ERROR:
                return R.string.interest_remote_fetch_error;
            case INTEREST_REMOTE_CREATION_ERROR:
                return R.string.interest_remote_creation_error;
            default:
                return R.string.unexpected_error;
        }
    }


    public int getWeatherErrorMessage(String error){
        switch (error){
            case RETROFIT_ERROR:
                return R.string.error_retrieving_weather;
            case API_ERROR:
                return R.string.error_on_API;
            default:
                return R.string.unexpected_error;
        }
    }

    public int getMapErrorMessage(String error){
        switch (error){
            case EMPTY_LOCATION:
                return R.string.location_missing;

            default:
                return R.string.unexpected_error;
        }
    }

    public int getNewEventErrorMessage(String error){
        switch (error){
            case EMPTY_FIELDS:
                return R.string.some_fields_must_be_empty;
            default:
                return R.string.unexpected_error;
        }
    }

    public int getEventErrorMessage(String error){
        switch (error){
            case VIEW_MODEL_ERROR:
                return R.string.error_retrieving_events;
            case EVENT_REMOTE_CREATION_ERROR:
                return R.string.event_remote_creation_error;
            case EVENT_REMOTE_FETCH_ERROR:
                return R.string.event_remote_fetch_error;
            case EVENT_REMOTE_UPDATE_ERROR:
                return R.string.event_remote_update_error;
            default:
                return R.string.unexpected_error;
        }
    }

    public int getParticipationError(String error){
        switch (error){
            case PARTICIPATION_REMOTE_CREATION_ERROR:
                return R.string.participation_remote_creation_error;
            case PARTICIPATION_REMOTE_FETCH_ERROR:
                return R.string.participation_remote_fetch_error;
            case PARTICIPATION_REMOTE_DELETION_ERROR:
                return R.string.participation_remote_deletion_error;
            default:
                return R.string.unexpected_error;
        }
    }
}
