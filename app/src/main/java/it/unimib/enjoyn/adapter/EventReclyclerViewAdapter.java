package it.unimib.enjoyn.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.R;
import it.unimib.enjoyn.util.ColorObject;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;
import it.unimib.enjoyn.util.ImageConverter;

public class EventReclyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EVENT_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;

    public interface OnItemClickListener{
        void onEventItemClick(Event event);

        void onJoinButtonPressed(int position);
    }

    private final List<Event> eventList;

    private final Context context;
    private final OnItemClickListener onItemClickListener;
    private final EventViewModel eventViewModel;
    private final UserViewModel userViewModel;



    public EventReclyclerViewAdapter(List<Event> eventList, Context context, OnItemClickListener onItemClickListener){
        this.eventList = eventList;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        eventViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(EventViewModel.class);
        userViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(UserViewModel.class);
    }

    public int getItemViewType(int position){
        if(eventList.get(position) == null){
            return LOADING_VIEW_TYPE;
        } else {
            return EVENT_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == EVENT_VIEW_TYPE){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
            return new EventViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_loading_item, parent, false);
            return new LoadingEventViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventViewHolder){
            ((EventViewHolder) holder).bind(eventList.get(position));

//            }
        } else if (holder instanceof LoadingEventViewHolder) {
            ((LoadingEventViewHolder) holder).activate();
        }
    }


    @Override
    public int getItemCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }


    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textViewTitle;
        private final TextView textViewData;
        private final TextView textViewTime;
        private final TextView textViewPlace;
        private final TextView textViewPeopleNumber;
        private final TextView textViewDistance;
        private final Button joinButton;
        private final ImageView weatherImage;
        private final ImageView backgroundImage;
        private final ImageView categoryImage;
        private ImageConverter imageConverter;



        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.eventListItem_textView_eventTitle);
            textViewData = itemView.findViewById(R.id.eventListItem_textView_date);
            textViewTime = itemView.findViewById(R.id.eventListItem_textView_time);
            textViewPlace = itemView.findViewById(R.id.eventListItem_textView_place);
            textViewPeopleNumber = itemView.findViewById(R.id.eventListItem_textView_peopleNumber);
            textViewDistance = itemView.findViewById(R.id.eventListItem_textView_distance);
            joinButton = itemView.findViewById(R.id.eventListItem_button_joinButton);
            weatherImage = itemView.findViewById(R.id.eventListItem_imageView_weather);
            backgroundImage = itemView.findViewById(R.id.eventListItem_imageView_background);
            categoryImage = itemView.findViewById(R.id.eventListItem_imageView_categoryVector);

            imageConverter = new ImageConverter();

            itemView.setOnClickListener(this);
            joinButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.eventListItem_button_joinButton){

                Event event = eventList.get(getBindingAdapterPosition());
                userViewModel.getCurrentUser().observe((LifecycleOwner) context, result -> {
                    if(result.isSuccessful()){
                        User user = ((Result.UserSuccess) result).getData();
                        if(event.isTodo()){
                            eventViewModel.leaveEvent(event, user).observe((LifecycleOwner) context, result1 -> {
                                if(getBindingAdapterPosition() != -1)
                                    setTextButtonTodoEvent(eventList.get(getBindingAdapterPosition()).isTodo());
                                 onItemClickListener.onJoinButtonPressed(getBindingAdapterPosition());
                            });
                        } else {
                            eventViewModel.joinEvent(event, user).observe((LifecycleOwner) context, result1 -> {
                                if(getBindingAdapterPosition() != -1)
                                    setTextButtonTodoEvent(eventList.get(getBindingAdapterPosition()).isTodo());
                                 onItemClickListener.onJoinButtonPressed(getBindingAdapterPosition());
                            });
                        }

                    }
                });


            }else{
                onItemClickListener.onEventItemClick(eventList.get(getBindingAdapterPosition()));
            }
        }

        public void bind(Event event) {

            textViewTitle.setText(event.getTitle());
            textViewData.setText(event.getDate());
            textViewTime.setText(event.getTime());
            if(event.getLocation() != null)
                textViewPlace.setText(event.getLocation().getName());
            textViewPeopleNumber.setText(event.getPeopleNumberString());
            textViewDistance.setText(event.getDistanceString());
            imageConverter.setWeatherIcon(weatherImage, event.getWeatherCode());
            setTextButtonTodoEvent(event.isTodo());
            String color = event.getColor().getName();

            imageConverter.setColorEvent(textViewPlace, backgroundImage, joinButton, event.getColor(), itemView);
            imageConverter.setCategoryImage(categoryImage, event.getCategory().getNome());


        }


        public void setTextButtonTodoEvent(boolean isTodo){
            if(isTodo){
                joinButton.setText(R.string.remove);
            }
            else{
                joinButton.setText(R.string.Join);
            }
        }


    }

    public static class LoadingEventViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;
        public LoadingEventViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.eventLoadingItem_progressBar);
        }

        public void activate(){
            progressBar.setIndeterminate(true);
        }
    }


}