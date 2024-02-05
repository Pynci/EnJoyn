package it.unimib.enjoyn.adapter;

import android.app.Application;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import androidx.appcompat.view.menu.MenuView;
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

//            Random random = new Random();
//            int randomNumber = random.nextInt(4) + 1;
//
//            if (randomNumber % 4 == 0) {
//                ((EventViewHolder) holder).backgroundImage.setBackgroundColor(
//                        ContextCompat.getColor(
//                                holder.itemView.getContext(),
//                                R.color.md_theme_dark_error));
//                ((EventViewHolder) holder).joinButton.setBackgroundColor(
//                        ContextCompat.getColor(
//                                holder.itemView.getContext(),
//                                R.color.md_theme_dark_error));
//            } else if (randomNumber % 4 == 1) {
//                ((EventViewHolder) holder).backgroundImage.setBackgroundColor(
//                        ContextCompat.getColor(holder.itemView.getContext(), R.color.md_theme_light_secondary));
//                ((EventViewHolder) holder).joinButton.setBackgroundColor(
//                        ContextCompat.getColor(holder.itemView.getContext(), R.color.md_theme_light_secondary));
//            } else if (randomNumber % 4 == 2){
//                ((EventViewHolder) holder).backgroundImage.setBackgroundColor(
//                        ContextCompat.getColor(holder.itemView.getContext(), R.color.md_theme_light_error));
//                ((EventViewHolder) holder).joinButton.setBackgroundColor(
//                        ContextCompat.getColor(holder.itemView.getContext(), R.color.md_theme_light_error));
//            } else {
//                ((EventViewHolder) holder).backgroundImage.setBackgroundColor(
//                        ContextCompat.getColor(holder.itemView.getContext(), R.color.md_theme_light_surfaceTint));
//                ((EventViewHolder) holder).joinButton.setBackgroundColor(
//                        ContextCompat.getColor(holder.itemView.getContext(), R.color.md_theme_light_surfaceTint));
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
                                // onItemClickListener.onJoinButtonPressed(getBindingAdapterPosition());
                            });
                        } else {
                            eventViewModel.joinEvent(event, user).observe((LifecycleOwner) context, result1 -> {
                                if(getBindingAdapterPosition() != -1)
                                    setTextButtonTodoEvent(eventList.get(getBindingAdapterPosition()).isTodo());
                                // onItemClickListener.onJoinButtonPressed(getBindingAdapterPosition());
                            });
                        }

                    }
                });


                Log.d("index", getLayoutPosition()+" ");
                //backgroundImage.setBackgroundColor(ContextCompat.getColor(v.getContext(),R.color.md_theme_dark_tertiary));
                //joinButton.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),R.color.md_theme_light_error));

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
            setWeatherIcon(weatherImage, event.getWeatherCode());
            setTextButtonTodoEvent(event.isTodo());
            String color = event.getColor().getName();

            setColorEvent(textViewPlace, backgroundImage, joinButton, event.getColor(), itemView);
            setCategoryImage(categoryImage, event.getCategory());

//            backgroundImage.setBackgroundColor(
//                    ContextCompat.getColor(itemView.getContext(), event.getColor().getIdColor()));
//            joinButton.setBackgroundColor(
//                    ContextCompat.getColor(itemView.getContext(), event.getColor().getIdColor()));

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

    public void setWeatherIcon(ImageView weatherIcon, int code){
        if (code == 0){
            weatherIcon.setBackgroundResource(R.drawable.drawable_sun);
        } else if (code >= 1 && code <= 3){
            weatherIcon.setBackgroundResource(R.drawable.drawable_partlycloudy);
        } else if (code == 45 || code == 48){
            weatherIcon.setBackgroundResource(R.drawable.drawable_fog);
        } else if (code == 51 || code == 53 || code == 55 || code == 56 || code == 57) {
            weatherIcon.setBackgroundResource(R.drawable.drawable_drizzle);
        } else if (code == 61 || code == 63 || code == 65 || code == 66 || code == 67 || code == 80 || code == 81 || code == 82){
            weatherIcon.setBackgroundResource(R.drawable.drawable_rain);
        } else if (code == 71 || code == 73 || code == 75 || code == 77){
            weatherIcon.setBackgroundResource(R.drawable.drawable_snowlight);
        } else if (code == 85 || code == 86){
            weatherIcon.setBackgroundResource(R.drawable.drawable_snow);
        } else if (code == 95 || code == 96 || code == 99){
            weatherIcon.setBackgroundResource(R.drawable.drawable_thunderstorm);
        }
    }

    public void setColorEvent(TextView textViewPlace, ImageView backgroundImage, Button joinButton, ColorObject color, View itemView){

        switch (color.getHex()){
            case "#80FFDB":
                backgroundImage.setBackgroundColor(
                    ContextCompat.getColor(itemView.getContext(), R.color.Aquamarine));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Aquamarine));


                break;
            case "#72EFDD":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Turquoise));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Turquoise));
                break;
            case "#64DFDF":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Tiffany_Blue));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Tiffany_Blue));
                break;
            case "#56CFE1":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Sky_blue));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Sky_blue));
                break;
            case "#48BFE3":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Aero));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Aero));
                break;
            case "#4EA8DE":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Picton_Blue));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Picton_Blue));
                break;
            case "#5390D9":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.United_Nations_Blue));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.United_Nations_Blue));
                break;
            case "#5E60CE":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Slate_blue));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Slate_blue));

                break;
        }
    }

    public void setCategoryImage(ImageView imageView, Category category){
        switch (category.getNome()){
            case "Passeggiata":
                imageView.setBackgroundResource(R.drawable.passeggiata);
                break;
            case "Viaggi":
                imageView.setBackgroundResource(R.drawable.viaggi);
                break;
            case "Pranzo":
                imageView.setBackgroundResource(R.drawable.pranzo);
                break;
            case "Videogiochi":
                imageView.setBackgroundResource(R.drawable.videogiochi);
                break;
            case "Shopping":
                imageView.setBackgroundResource(R.drawable.shopping);
                break;
            case "Cinema":
                imageView.setBackgroundResource(R.drawable.cinema);
                break;
            case "Sport":
                imageView.setBackgroundResource(R.drawable.sport);
                break;
        }

    }

}