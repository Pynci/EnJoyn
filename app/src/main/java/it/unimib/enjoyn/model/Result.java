package it.unimib.enjoyn.model;

import android.net.Uri;

import com.mapbox.search.result.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

public abstract class Result {
    private Result(){

    }

    public boolean isSuccessful() {
        return this instanceof UserSuccess
                || this instanceof Success
                || this instanceof BooleanSuccess
                || this instanceof CategorySuccess
                || this instanceof ImagesReadFromRemote
                || this instanceof EventSuccess
                || this instanceof WeatherSuccess
                || this instanceof MapSuggestionSuccess;
    }


    public static final class Success extends Result{
        //class representing a generic Success of an operation
    }

    public static final class UserSuccess extends Result {
        private final User user;

        public UserSuccess(User user){
            this.user = user;
        }

        public User getData(){
            return user;
        }
    }

    public static final class EventSuccess extends Result {
        private final EventsDatabaseResponse eventResponse;

        public EventSuccess(EventsDatabaseResponse eventResponse) {
            this.eventResponse = eventResponse;
        }

        public EventsDatabaseResponse getData() {
            return eventResponse;
        }
    }

    public static final class WeatherSuccess extends Result{

        private final WeatherResponse weatherResponse;

        public WeatherSuccess(WeatherResponse weatherResponse) {
            this.weatherResponse = weatherResponse;
        }
        public WeatherResponse getData(){
            return weatherResponse;
        }
    }

    public static final class MapSuggestionSuccess extends Result{

        private final List<SearchSuggestion> suggestions;

        public MapSuggestionSuccess(List<SearchSuggestion> suggestions) {
            this.suggestions = suggestions;
        }
        public List<SearchSuggestion> getData(){
            return suggestions;
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

    public static final class CategorySuccess extends Result {

        private final List<Category> categoryList;

        public CategorySuccess(List<Category> list) {
            categoryList = list;
        }

        public List<Category> getCategoryList() {
            return  categoryList;
        }
    }

    public static final class ImagesReadFromRemote extends Result{

        private final List<Uri> imageUri;

        public ImagesReadFromRemote(List<Uri> uri) {
            imageUri = uri;
        }

        public List<Uri> getImagesUri(){
            return imageUri;
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

    public static final class Error extends Result {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }

    public static final class WeatherError extends Result{
        private final String message;

        public WeatherError(String message) {
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }

    public static final class EventError extends Result {
        private final String message;

        public EventError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
