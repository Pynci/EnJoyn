package it.unimib.enjoyn.ui.auth.registration;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.unimib.enjoyn.adapter.CategoriesSelectionAdapter;
import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.CategoryViewModel;
import it.unimib.enjoyn.ui.UserViewModel;
import it.unimib.enjoyn.ui.auth.LoginFragment;

public class CategoriesSelectionFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    private Observer<Result> categoriesObserver;

    public CategoriesSelectionFragment() {
    }

    public static CategoriesSelectionFragment newInstance() {
        CategoriesSelectionFragment fragment = new CategoriesSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = view.findViewById(R.id.fragmentCategoriesSelection_ListView);
        listView.setDivider(null);

        categoriesObserver = result -> {
            if (result.isSuccessful()) {

                categoryViewModel
                        .getAllImages(((Result.CategoryResponseSuccess) result).getCategoryList())
                        .observe(this.getViewLifecycleOwner(), result1 -> {

                            List<Result> risultati = ((Result.ResultList) result1).getResults();
                            Iterator<Result> iterator = risultati.iterator();
                            List<Uri> images = new ArrayList<>();

                            while (iterator.hasNext()) {

                                Result.ImageReadFromRemote imageResult = (Result.ImageReadFromRemote) iterator.next();
                                images.add(imageResult.getImageUri());
                            }

                            CategoriesSelectionAdapter customAdapter = new CategoriesSelectionAdapter(this.getContext(),
                                    ((Result.CategoryResponseSuccess) result).getCategoryList(), images);
                            listView.setAdapter(customAdapter);

                        });
            }
        };

        categoryViewModel.getAllNews().observe(this.getViewLifecycleOwner(), categoriesObserver);
    }
}