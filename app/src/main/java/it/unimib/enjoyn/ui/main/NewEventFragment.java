package it.unimib.enjoyn.ui.main;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Calendar;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Meteo;
import it.unimib.enjoyn.model.MeteoApiResponse;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.IMeteoRepository;
import it.unimib.enjoyn.util.ErrorMessagesUtil;
import it.unimib.enjoyn.util.JSONParserUtil;
import it.unimib.enjoyn.util.MeteoCallback;
import it.unimib.enjoyn.util.ServiceLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventFragment extends Fragment implements MeteoCallback {

    ImageButton date;
    TextView selectedDate;

    ImageButton time;
    TextView selectedTime;

    TextView meteo;
    TextView temperatura;
    String hourWeather;
    int indexHour = -1;
    int indexMinute = -1;
    int indexDate = -1;
    boolean equals = false;
    String dateWeather;
    ImageView weatherIcon;

    private EventViewModel eventViewModel;
    private Meteo weatherAPIdata;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        NewEventFragment fragment = new NewEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("API meteo", "su OnCreate");
        IMeteoRepository meteoRepository = ServiceLocator.getInstance().getWeatherRepository(requireActivity().getApplication());
        weatherAPIdata = new Meteo();
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        Log.d("API meteo", "su OnCreate dopo tutto");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getViewLifecycleOwner();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        eventViewModel.getWeather("52.52", "13.41").observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()){
                weatherAPIdata = ((Result.WeatherSuccess) result).getData().getWeather();
                //this.meteoList.clear();
                //this.meteoList.addAll(((Result.Success) result).getData().getMeteoList());
                showWeatherOnNewEvent(requireView());
            } else {
                ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.getErrorMessage(((Result.WeatherError) result).getMessage()), Snackbar.LENGTH_LONG).show();
            }
        });


        //iMeteoRepository.fetchMeteo("52.52", "13.41");
        //weatherAPIdata = getMeteoListWithGSon();
        //showWeatherOnNewEvent(requireView());

    }

    public void showWeatherOnNewEvent(View view){
        String[] dateArray = weatherAPIdata.getHour();
        double[] temperatureArray = weatherAPIdata.getTemperature();
        Log.d("API meteo", "errore show");
        date = view.findViewById(R.id.fragmentNewEvent_imageButton_datePicker);
        selectedDate = view.findViewById(R.id.fragmentNewEvent_textView_date);
        meteo = view.findViewById(R.id.meteo);
        temperatura = view.findViewById(R.id.temperatura);
        weatherIcon = view.findViewById(R.id.fragmentNewEvent_imageView_meteoIcon);

        FragmentManager fragmentManager = getParentFragmentManager();

        // on below line we are adding click listener for our pick date button
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

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
                                selectedDate.setText(dateWeather);
                                equals = false;
                                for( int i = 0;i < dateArray.length && !equals ; i+=96){
                                    boolean test=dateWeather.equals(dateArray[i].substring(0, 10));
                                    String prova= dateArray[i].substring(0, 10);
                                    if(dateWeather.equals(dateArray[i].substring(0, 10))) {
                                        indexDate = i;
                                        equals = true;
                                    }
                                }
                              /* if(indexDate>0) {
                                   indexDate += 96;
                               }*/
                                if(!equals){
                                    meteo.setText("meteo non disponibile, troppo lontano , accuratezza di 16 giorni");
                                    temperatura.setText("");
                                    weatherIcon.setBackgroundResource(0);
                                }
                                else {
                                    if (indexHour >= 0 && indexMinute >= 0) {
                                        String code = weatherAPIdata.getWeather_codeString(indexDate + indexHour + indexMinute);
                                        meteo.setText(code);
                                        temperatura.setText(weatherAPIdata.getTemperatureString(indexDate + indexHour + indexMinute));
                                        setWeatherIcon(weatherIcon, Integer.parseInt(code));
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


        time = view.findViewById(R.id.fragmentNewEvent_imageButton_pickTime);
        selectedTime = view.findViewById(R.id.fragmentNewEvent_textView_time);

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
                                selectedTime.setText(hourOfDay + ":" + minute);
                                hourWeather = hourOfDay + ":" + minute;
                                indexHour = hourOfDay*4;
                                indexMinute = minute/15;

                                String dateHourWeather = dateWeather + "T" + hourWeather;


                                assert weatherAPIdata != null;
                                //assert meteoList.get(0) != null;
                                assert weatherAPIdata.getHour()[indexHour] != null;
                                if(equals){
                                    double temp= temperatureArray[indexDate+indexHour+indexMinute];
                                    String code = weatherAPIdata.getWeather_codeString(indexDate+indexHour+indexMinute);
                                    meteo.setText(code);
                                    temperatura.setText(weatherAPIdata.getTemperatureString(indexDate+indexHour+indexMinute));
                                    setWeatherIcon(weatherIcon, Integer.parseInt(code));
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
    private Meteo getMeteoListWithGSon() {
        JSONParserUtil jsonParserUtil = new JSONParserUtil(requireActivity().getApplication());
        try {
            /**TODO
             * sistemare questa parte
             * */
            /*
            Context context = requireActivity().getApplication().getApplicationContext();
            InputStream inputStream = context.getAssets().open("meteo.json"); //apro file
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //estraggo json

            assert jsonParserUtil.parseJSONMeteoFileWithGSon(bufferedReader) != null;

            List<Meteo> meteoList = jsonParserUtil.parseJSONFileWithJSONObjectArray("meteo.json").getMeteoList();

            return meteoList;

             */
            MeteoApiResponse  response = jsonParserUtil.parseJSONFileAPIMeteo("meteoCompleto.json");

            return response.getWeather();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onSuccessFromRemote(MeteoApiResponse weatherApiResponse) {
        Log.d("API meteo", "Entra in OnSuccessEV");
    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }
}