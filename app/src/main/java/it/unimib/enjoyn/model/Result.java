package it.unimib.enjoyn.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public abstract class Result {
    private Result(){

    }

    public boolean isSuccessful() {
        return this instanceof UserResponseSuccess
                || this instanceof Success
                || this instanceof BooleanSuccess
                || this instanceof CategoryResponseSuccess
                || this instanceof ImageReadFromRemote;
    }


    public static final class Success extends Result{
        //class representing a generic Success of an operation
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

    public static final class BooleanSuccess extends Result{
        private final boolean value;

        public BooleanSuccess(boolean value) {
            this.value = value;
        }

        public boolean getData(){
            return value;
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

    public static final class CategoryResponseSuccess extends Result {

        private final List<Category> categoryList;

        public CategoryResponseSuccess (List<Category> list) {
            categoryList = list;
        }

        public List<Category> getCategoryList() {
            return  categoryList;
        }
    }

    public static final class ImageReadFromRemote extends Result{

        private final Uri imageUri;

        public ImageReadFromRemote(Uri uri) {
            imageUri = uri;
        }

        public Uri getImageUri(){
            return imageUri;
        }
    }
}
