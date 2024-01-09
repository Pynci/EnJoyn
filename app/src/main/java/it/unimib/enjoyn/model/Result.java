package it.unimib.enjoyn.model;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public abstract class Result {
    private Result(){

    }

    public boolean isSuccessful() {
        return this instanceof UserResponseSuccess
                || this instanceof Success;
    }

    public static final class UserResponseSuccess extends Result {
        private final User user;

        public UserResponseSuccess(User user){
            this.user = user;
        }

        public User getData(){
            return user;
        }
    }

    public static final class Success extends Result{
        //class representing a generic Success of an operation
    }

    public static final class Error extends Result {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }

    public static final class ResultList extends Result {

        private final List<Result> messages;

        public ResultList() {
            messages = new ArrayList<>();
        }

        public void addResult(Result result) {
            messages.add(result);
        }

        public List<Result> getResults() {
            return messages;
        }
    }
}
