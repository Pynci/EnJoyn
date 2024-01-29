package it.unimib.enjoyn.ui.main;

import static it.unimib.enjoyn.util.Constants.EMPTY_FIELDS;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

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
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Weather;
import it.unimib.enjoyn.model.WeatherApiResponse;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.ui.viewmodels.CategoryViewModel;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.util.ErrorMessagesUtil;
import it.unimib.enjoyn.util.JSONParserUtil;
import it.unimib.enjoyn.util.SnackbarBuilder;
import it.unimib.enjoyn.util.WeatherCallback;
import it.unimib.enjoyn.util.ServiceLocator;
import it.unimib.enjoyn.databinding.FragmentNewEventBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventFragment extends Fragment implements WeatherCallback {



    ImageButton date;
    TextView selectedDate;

    ImageButton time;
    ImageButton place;
    TextView selectedTime;

    TextView weather;
    TextView temperature;
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
    int numberOfPeople = -1;
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

    public NewEventFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewEventFragment newInstance(String param1, String param2) {
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
        }
        Log.d("API weather", "su OnCreate");
        IWeatherRepository weatherRepository = ServiceLocator.getInstance().getWeatherRepository(requireActivity().getApplication());
        weatherAPIdata = new Weather();
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        categoryViewModel = new CategoryViewModel(requireActivity().getApplication());
        Log.d("API weather", "su OnCreate dopo tutto");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

            if(title != null && dateWeather != null && timeWeather != null && locationName != null && description != null){
                newEvent.setTitle(title);
                newEvent.setDate(dateWeather);
                newEvent.setTime(timeWeather);;
                newEvent.setDescription(description);
                newEvent.setCategory(selectedCategory);
                // chiamata ai livelli sottostanti per il salvataggio (da ascoltare)
                eventViewModel.createEvent(newEvent).observe(getViewLifecycleOwner(), eventCreationObserver);
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

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                String dayOfMonthString = Integer.toString(dayOfMonth) ;
                                String monthOfYearString = Integer.toString(monthOfYear+1) ;
                                if(dayOfMonth<=9)
                                    dayOfMonthString= "0" + dayOfMonthString;
                                if(monthOfYear<=9)
                                    monthOfYearString= "0" + monthOfYearString;
                                dateWeather = year + "-" + monthOfYearString + "-" +dayOfMonthString ;
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
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
    }

    public void getTime(View view){
        String[] dateArray = weatherAPIdata.getHour();
        double[] temperatureArray = weatherAPIdata.getTemperature();

        time = view.findViewById(R.id.fragmentNewEvent_imageButton_pickTime);


        // on below line we are adding click
        // listener for our pick date button
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(time.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.

                                if (minute<10){
                                    timeWeather = hourOfDay+":0"+minute;
                                } else {
                                    timeWeather = hourOfDay + ":" + minute;
                                }
                                fragmentNewEventBinding.fragmentNewEventTextViewTime.setText(timeWeather);
                                hourWeather = hourOfDay + ":" + minute;
                                indexHour = hourOfDay*4;
                                indexMinute = minute/15;

                                String dateHourWeather = dateWeather + "T" + hourWeather;


                                assert weatherAPIdata != null;
                                assert weatherAPIdata.getHour()[indexHour] != null;
                                if(equals){
                                    temp = temperatureArray[indexDate+indexHour+indexMinute];
                                    String code = weatherAPIdata.getWeather_codeString(indexDate+indexHour+indexMinute);
                                    weatherCode = Integer.parseInt(code);
                                    //fragmentNewEventBinding.weather.setText(code);
                                    fragmentNewEventBinding.newEventFragmentTextViewTemperature.setText(temp+ "°C");
                                    setWeatherIcon(fragmentNewEventBinding.fragmentNewEventImageViewMeteoIcon, Integer.parseInt(code));
                                }
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }

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
        // Always call the superclass so it can save the view hierarchy state.
        super.onSaveInstanceState(savedInstanceState);
    }
}