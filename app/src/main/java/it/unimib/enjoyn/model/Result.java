package it.unimib.enjoyn.model;

import com.google.firebase.auth.FirebaseUser;

public abstract class Result {
    private Result(){

    }

    public boolean isSuccess() {
        return this instanceof UserResponseSuccess
                || this instanceof Success
                || this instanceof SignInSuccess;
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

    public static final class SignInSuccess extends Result {
        private final FirebaseUser fbUser;

        public SignInSuccess(FirebaseUser fbUser) {
            this.fbUser = fbUser;
        }

        public FirebaseUser getData() {
            return fbUser;
        }
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

}
