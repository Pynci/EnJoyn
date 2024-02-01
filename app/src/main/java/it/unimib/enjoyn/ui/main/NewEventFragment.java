package it.unimib.enjoyn.ui.main;

import static it.unimib.enjoyn.util.Constants.EMPTY_FIELDS;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.EventLocation;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.model.Weather;
import it.unimib.enjoyn.model.WeatherApiResponse;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.viewmodels.CategoryViewModel;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModelFactory;
import it.unimib.enjoyn.util.ErrorMessagesUtil;
import it.unimib.enjoyn.util.JSONParserUtil;
import it.unimib.enjoyn.util.WeatherCallback;
import it.unimib.enjoyn.util.ServiceLocator;
import it.unimib.enjoyn.databinding.FragmentNewEventBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventFragment extends Fragment implements WeatherCallback {


    private static final String STATE_URI = "uri";
    ImageButton date;
    ImageButton time;
    Spinner categorySpinner;
    String hourWeather;
    int indexHour = -1;
    int indexMinute = -1;
    int indexDate = -1;
    boolean equals = false;
    String title;
    String dateWeather;
    String timeWeather;
    int weatherCode = -1;
    String locationName;
    ImageView weatherIcon;
    String description;
    Uri eventImage = null;
    double temp = -10000;

    private Category selectedCategory;

    private EventViewModel eventViewModel;
    private Weather weatherAPIdata;

    private CategoryViewModel categoryViewModel;
    private static final String STATE_CATEGORY = "category";
    static final String STATE_TIME ="timeSelected";
    static final String STATE_DATE = "dateSelected";
    private static final String STATE_CODE = "weatherCode";

    private static final String STATE_TEMPERATURE = "temperature";

    private static final String STATE_EQUALS = "equals";
    private FragmentNewEventBinding fragmentNewEventBinding;
    private Observer<Result> eventCreationObserver;
    UserViewModel userViewModel;

    boolean sameDay = false;

    public NewEventFragment() {
        // Required empty public constructor
    }



    public static NewEventFragment newInstance() {
        return new NewEventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            dateWeather = savedInstanceState.getString(STATE_DATE);
            timeWeather= savedInstanceState.getString(STATE_TIME);
            weatherCode = savedInstanceState.getInt(STATE_CODE);
            temp = savedInstanceState.getDouble(STATE_TEMPERATURE);
            equals = savedInstanceState.getBoolean(STATE_EQUALS);
            selectedCategory = savedInstanceState.getParcelable(STATE_CATEGORY);
            Log.d("code", ""+weatherCode);
            eventImage = savedInstanceState.getParcelable(STATE_URI);
        }

        weatherAPIdata = new Weather();
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        Log.d("API weather", "su OnCreate dopo tutto");

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getViewLifecycleOwner();
        // Inflate the layout for this fragment
        fragmentNewEventBinding = FragmentNewEventBinding.inflate(inflater, container, false);
        return fragmentNewEventBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        savedInstanceState = new Bundle();
        super.onViewCreated(view, savedInstanceState);
        Event newEvent = new Event();

        newEvent.setLocation(NewEventFragmentArgs.fromBundle(getArguments()).getLocation());
        locationName = newEvent.getLocation().getName();
        fragmentNewEventBinding.fragmentNewEventTextViewLocation.setText(locationName);

        eventCreationObserver = result -> {
            if(result.isSuccessful()){
                Snackbar
                        .make(view, "FUNZICA", Snackbar.LENGTH_SHORT)
                        .show();
                EventLocation oldLocation = NewEventFragmentArgs.fromBundle(getArguments()).getLocation();
                oldLocation.setName(null);
                oldLocation.setLongitude(-1);
                oldLocation.setLatitude(-1);
                getParentFragmentManager().popBackStackImmediate();

            }
            else{
                Snackbar
                        .make(view, ((Result.Error) result).getMessage(), Snackbar.LENGTH_SHORT)
                        .show();
            }
        };


        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        ShapeableImageView image = fragmentNewEventBinding.fragmentNewEventShapeableImageViewEventImage;
        ImageButton photoPicker = fragmentNewEventBinding.fragmentNewEventImageButtonPhotoPicker;

        /*if(eventImage!= null){
            image.setImageURI(eventImage);
            photoPicker.setVisibility(View.GONE);
        }*/
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        image.setImageURI(uri);
                        photoPicker.setVisibility(View.GONE);
                        eventImage = uri;
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        photoPicker.setOnClickListener(v ->
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));




        Log.d("text", title+" "+description);
        weatherIcon = view.findViewById(R.id.fragmentNewEvent_imageView_meteoIcon);

        // latitude and longitude "52.52", "13.41"
        Bundle finalSavedInstanceState = savedInstanceState;
        eventViewModel.getWeather(newEvent.getLocation().getLatitudeToString(), newEvent.getLocation().getLongitudeToString()).observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccessful()){
                weatherAPIdata = ((Result.WeatherSuccess) result).getData().getWeather();
                //showWeatherOnNewEvent(requireView());
                getDate(requireView());
                getTime(requireView());
                onSaveInstanceState(finalSavedInstanceState);

                if(dateWeather != null )
                    fragmentNewEventBinding.fragmentNewEventTextViewDate.setText(dateWeather);
                if(timeWeather != null)
                    fragmentNewEventBinding.fragmentNewEventTextViewTime.setText(timeWeather);
                if(weatherCode != -1)
                    setWeatherIcon(fragmentNewEventBinding.fragmentNewEventImageViewMeteoIcon, weatherCode);
                if(temp != -10000)
                    fragmentNewEventBinding.newEventFragmentTextViewTemperature.setText(temp + "°C");

            } else {
                ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.getWeatherErrorMessage(((Result.WeatherError) result).getMessage()), Snackbar.LENGTH_LONG).show();
            }
        });

        categorySpinner = view.findViewById(R.id.fragmentNewEvent_spinner_categories);
            categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), result -> {
                if (result.isSuccessful()) {
                    List<Category> categoryList = ((Result.CategorySuccess) result).getCategoryList();
                    ArrayList<String> categoryNameList = new ArrayList<>();
                    for (Category category : categoryList) {
                        categoryNameList.add(category.getNome());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNameList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCategory = new Category(parent.getItemAtPosition(position).toString());
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if(selectedCategory != null){
                        boolean categoryEquals = true;
                        for (int i = 0 ; i<categoryNameList.size() && categoryEquals; i++) {
                            if(categoryNameList.get(i).equals(selectedCategory.getNome())) {
                                categorySpinner.setSelection(i);
                                categoryEquals = false;
                            }

                        }


                    }

                }
            });







        fragmentNewEventBinding.fragmentNewEventButtonCreateEvent.setOnClickListener(v -> {
            //TODO da inserire tag categoria
            title = String.valueOf(fragmentNewEventBinding.fragmentNewEventEditTextTitle.getText());
            description = String.valueOf(fragmentNewEventBinding.fragmentNewEventEditTextDescription.getText());

            if(title != null && dateWeather != null && timeWeather != null && locationName != null){
                newEvent.setTitle(title);
                newEvent.setDate(dateWeather);
                newEvent.setTime(timeWeather);
                newEvent.setDescription(description);
                newEvent.setCategory(selectedCategory);
                //newEvent.setImageUrl(eventImage);
                // chiamata ai livelli sottostanti per il salvataggio (da ascoltare)
                userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), result -> {
                    if(result.isSuccessful() || result instanceof Result.UserSuccess){
                        User currentUser = ((Result.UserSuccess) result).getData();
                        eventViewModel.createEvent(newEvent, currentUser).observe(getViewLifecycleOwner(), eventCreationObserver);
                    }
                });
            } else {
                ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.getNewEventErrorMessage(EMPTY_FIELDS), Snackbar.LENGTH_LONG).show();
            }

        });
    }

    public void getDate(View view){

        String[] dateArray = weatherAPIdata.getHour();
        double[] temperatureArray = weatherAPIdata.getTemperature();
        date = view.findViewById(R.id.fragmentNewEvent_imageButton_datePicker);

        date.setOnClickListener(v -> {
            // on below line we are getting
            // the instance of our calendar.
            final Calendar c = Calendar.getInstance();

            /*
            TextView luogo = view.findViewById(R.id.textViewProvaLuogo);
            Log.d("API weather", newEvent.getPlace());
            Log.d("API weather", newEvent.getPlace());
            luogo.setText(newEvent.getPlace() + " e " + newEvent.getPlace());
             */

            // on below line we are getting
            // our day, month and year.
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // on below line we are creating a variable for date picker dialog.
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    // on below line we are passing context.
                    date.getContext(),
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        c.get(Calendar.YEAR);
                        c.get(Calendar.MONTH);
                        c.get(Calendar.DAY_OF_MONTH);


                        if(year1 < c.get(Calendar.YEAR) ||
                                year1 == c.get(Calendar.YEAR) && monthOfYear <c.get(Calendar.MONTH) ||
                                year1 == c.get(Calendar.YEAR) && monthOfYear == c.get(Calendar.MONTH)
                                        && dayOfMonth<c.get(Calendar.DAY_OF_MONTH)){
                            Snackbar
                                    .make(getView(), "scelta data passata, riprova", Snackbar.LENGTH_SHORT)
                                    .show();
                            return ;
                        }
                        sameDay = year1 == c.get(Calendar.YEAR) && monthOfYear == c.get(Calendar.MONTH)
                                && dayOfMonth == c.get(Calendar.DAY_OF_MONTH);


                        // on below line we are setting date to our text view.
                        String dayOfMonthString = Integer.toString(dayOfMonth) ;
                        String monthOfYearString = Integer.toString(monthOfYear+1) ;
                        if(dayOfMonth<=9)
                            dayOfMonthString= "0" + dayOfMonthString;
                        if(monthOfYear<=9)
                            monthOfYearString= "0" + monthOfYearString;
                        dateWeather = year1 + "-" + monthOfYearString + "-" +dayOfMonthString ;
                        fragmentNewEventBinding.fragmentNewEventTextViewDate.setText(dateWeather);
                        equals = false;
                        for( int i = 0;i < dateArray.length && !equals ; i+=96){
                            //boolean test = dateWeather.equals(dateArray[i].substring(0, 10));
                            //String prova = dateArray[i].substring(0, 10);
                            if(dateWeather.equals(dateArray[i].substring(0, 10))) {
                                indexDate = i;
                                equals = true;
                            }
                        }
                      /* if(indexDate>0) {
                           indexDate += 96;
                       }*/
                        if(!equals){
                            //fragmentNewEventBinding.weather.setText("meteo non disponibile, troppo lontano , accuratezza di 16 giorni");
                            fragmentNewEventBinding.newEventFragmentTextViewTemperature.setText("");
                            fragmentNewEventBinding.fragmentNewEventImageViewMeteoIcon.setBackgroundResource(0);
                            //weatherIcon.setBackgroundResource(0);
                        }
                        else {
                            if (indexHour >= 0 && indexMinute >= 0) {
                                String code = weatherAPIdata.getWeather_codeString(indexDate + indexHour + indexMinute);
                                weatherCode = Integer.parseInt(code);
                                temp = temperatureArray[indexDate+indexHour+indexMinute];
                               // fragmentNewEventBinding.weather.setText(code);
                                fragmentNewEventBinding.newEventFragmentTextViewTemperature.setText(temp+ "°C");
                                setWeatherIcon(fragmentNewEventBinding.fragmentNewEventImageViewMeteoIcon, weatherCode);
                            }
                        }
                    },
                    // on below line we are passing year,
                    // month and day for selected date in our date picker.
                    year, month, day);
            // at last we are calling show to
            // display our date picker dialog.
            datePickerDialog.show();
        });
    }

    public void getTime(View view){
        double[] temperatureArray = weatherAPIdata.getTemperature();

        time = view.findViewById(R.id.fragmentNewEvent_imageButton_pickTime);


        // on below line we are adding click
        // listener for our pick date button
        time.setOnClickListener(v -> {
            if(dateWeather == null ){
                Snackbar
                        .make(getView(), "scegliere prima la data", Snackbar.LENGTH_SHORT)
                        .show();
                return ;
            }
            // on below line we are getting the
            // instance of our calendar.
            final Calendar c = Calendar.getInstance();

            // on below line we are getting our hour, minute.
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // on below line we are initializing our Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(time.getContext(),
                    (view1, hourOfDay, minute1) -> {
                        // on below line we are setting selected time
                        // in our text view.
                        if(sameDay){
                            if(hourOfDay < c.get(Calendar.HOUR_OF_DAY)||
                                    hourOfDay < c.get(Calendar.HOUR_OF_DAY) && minute1 <c.get(Calendar.MINUTE)){
                                Snackbar
                                        .make(getView(), "scelta ora passata, riprova", Snackbar.LENGTH_SHORT)
                                        .show();
                                return ;
                            }
                        }

                        if (hourOfDay < 10 && minute1 < 10){
                            timeWeather = "0"+hourOfDay+":0"+ minute1;
                        } else if (hourOfDay >= 10 && minute1 < 10) {
                            timeWeather = hourOfDay + ":0" + minute1;
                        } else if (hourOfDay < 10){
                            timeWeather = "0"+hourOfDay+":"+ minute1;
                        } else {
                            timeWeather = hourOfDay+":"+ minute1;
                        }

                        fragmentNewEventBinding.fragmentNewEventTextViewTime.setText(timeWeather);
                        hourWeather = hourOfDay + ":" + minute1;
                        indexHour = hourOfDay*4;
                        indexMinute = minute1 /15;

                        assert weatherAPIdata != null;
                        assert weatherAPIdata.getHour()[indexHour] != null;
                        if(equals){
                            temp = temperatureArray[indexDate+indexHour+indexMinute];
                            weatherCode = weatherAPIdata.getWeather_code(indexDate+indexHour+indexMinute);
                            //fragmentNewEventBinding.weather.setText(code);
                            fragmentNewEventBinding.newEventFragmentTextViewTemperature.setText(temp+ "°C");
                            setWeatherIcon(fragmentNewEventBinding.fragmentNewEventImageViewMeteoIcon, weatherCode);
                        }
                    }, hour, minute, false);
            // at last we are calling show to
            // display our time picker dialog.
            timePickerDialog.show();
        });
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
    private Weather getMeteoListWithGSon() {
        JSONParserUtil jsonParserUtil = new JSONParserUtil(requireActivity().getApplication());
        try {
            /*TODO
             * sistemare questa parte
            */
            /*
            Context context = requireActivity().getApplication().getApplicationContext();
            InputStream inputStream = context.getAssets().open("weather.json"); //apro file
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //estraggo json

            assert jsonParserUtil.parseJSONMeteoFileWithGSon(bufferedReader) != null;

            List<Weather> meteoList = jsonParserUtil.parseJSONFileWithJSONObjectArray("weather.json").getMeteoList();

            return meteoList;

             */
            WeatherApiResponse response = jsonParserUtil.parseJSONFileAPIMeteo("meteoCompleto.json");

            return response.getWeather();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onSuccessFromRemote(WeatherApiResponse weatherApiResponse) {
        Log.d("API weather", "Entra in OnSuccessEV");
    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state.
        savedInstanceState.putString(STATE_DATE, dateWeather);
        savedInstanceState.putString(STATE_TIME, timeWeather);
        savedInstanceState.putInt(STATE_CODE, weatherCode);
        savedInstanceState.putDouble(STATE_TEMPERATURE, temp);
        savedInstanceState.putBoolean(STATE_EQUALS, equals);
        savedInstanceState.putParcelable(STATE_CATEGORY, selectedCategory);
        savedInstanceState.putParcelable(STATE_URI, eventImage);
        // Always call the superclass so it can save the view hierarchy state.
        super.onSaveInstanceState(savedInstanceState);
    }
}