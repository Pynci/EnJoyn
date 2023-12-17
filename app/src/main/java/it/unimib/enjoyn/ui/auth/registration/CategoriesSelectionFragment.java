package it.unimib.enjoyn.ui.auth.registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.adapter.CategoriesSelectionAdapter;
import it.unimib.enjoyn.R;

public class CategoriesSelectionFragment extends Fragment {

    public CategoriesSelectionFragment() {
        // Required empty public constructor
    }

    public static CategoriesSelectionFragment newInstance() {
        CategoriesSelectionFragment fragment = new CategoriesSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.fragmentCategoriesSelection_ListView); // Riferimento alla tua ListView
        List<String> yourData = new ArrayList<>();

        yourData.add("Prova");
        yourData.add("test");

        CategoriesSelectionAdapter customAdapter = new CategoriesSelectionAdapter(this.getContext(), yourData);
        listView.setAdapter(customAdapter);
    }
}