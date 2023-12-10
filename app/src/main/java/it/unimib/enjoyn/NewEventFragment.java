package it.unimib.enjoyn;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import it.unimib.enjoyn.model.MeteoDatabaseResponse;
import it.unimib.enjoyn.util.DatePickerFragment;
import it.unimib.enjoyn.util.JSONParserUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventFragment extends Fragment {

    Button date;
    TextView selectedDate;

    Button time;
    TextView selectedTime;


   TextView meteo;
    TextView temperatura;
    String hourWeather;
    int indexHour=-1;
    int indexMinute=-1;

    int indexDate=-1;
    String dateWeather;



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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        List<Meteo> meteoList = getMeteoListWithGSon();
        String[] dateArray = meteoList.get(0).getHour();
        double[] temperatureArray = meteoList.get(0).getTemperature();
        date = view.findViewById(R.id.newEventFragment_button_datePicker);
        selectedDate = view.findViewById(R.id.fragmentNewEvent_textView_date);
        meteo = view.findViewById(R.id.meteo);
        temperatura = view.findViewById(R.id.temperatura);

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
                                selectedDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                if(dayOfMonth>9)
                                dateWeather = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                else
                                    dateWeather = year + "-" + (monthOfYear + 1) + "-" + "0"+dayOfMonth;

                               for( int i = 0;!(dateWeather.equals(dateArray[i].substring(0, 10)))&& i< dateArray.length ; i+=96){
                                   boolean test=dateWeather.equals(dateArray[i].substring(0, 10));
                                  String prova= dateArray[i].substring(0, 10);
                                   indexDate=i;
                               }
                               if(indexDate>0) {
                                   indexDate += 96;
                               }
                               if(indexHour>0 && indexMinute>0){
                                meteo.setText(meteoList.get(0).getWeather_codeString(indexDate+indexHour+indexMinute));
                                temperatura.setText( meteoList.get(0).getTemperatureString(indexDate+indexHour+indexMinute));
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


        time = view.findViewById(R.id.fragmentNewEvent_button_pickTime);
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





                                assert meteoList != null;
                                assert meteoList.get(0) != null;
                                assert meteoList.get(0).getHour()[indexHour] != null;
                                if(indexDate>=0){
                               double temp= temperatureArray[indexDate+indexHour+indexMinute];
                                meteo.setText(meteoList.get(0).getWeather_codeString(indexDate+indexHour+indexMinute));
                                temperatura.setText( meteoList.get(0).getTemperatureString(indexDate+indexHour+indexMinute));
                                }
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }

        });



    }

    private List<Meteo> getMeteoListWithGSon() {
        JSONParserUtil jsonParserUtil = new JSONParserUtil(requireActivity().getApplication());
        try {
            /**TODO
             * sistemare questa parte
             * */

            Context context = requireActivity().getApplication().getApplicationContext();
            InputStream inputStream = context.getAssets().open("meteo.json"); //apro file
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //estraggo json

            assert jsonParserUtil.parseJSONMeteoFileWithGSon(bufferedReader) != null;

            List<Meteo> meteoList = jsonParserUtil.parseJSONFileWithJSONObjectArray("meteo.json").getMeteoList();

            return meteoList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}