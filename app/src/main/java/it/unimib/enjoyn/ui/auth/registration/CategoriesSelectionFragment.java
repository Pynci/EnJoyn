package it.unimib.enjoyn.ui.auth.registration;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import it.unimib.enjoyn.adapter.CategoriesSelectionAdapter;
import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.CategoryViewModel;

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

                            if(result1 instanceof Result.ImagesReadFromRemote){

                                List<Uri> images = ((Result.ImagesReadFromRemote) result1).getImagesUri();
                                CategoriesSelectionAdapter customAdapter = new CategoriesSelectionAdapter(this.getContext(),
                                        ((Result.CategoryResponseSuccess) result).getCategoryList(), images);
                                listView.setAdapter(customAdapter);
                            }
                        });
            }
        };

        categoryViewModel.getAllCategories().observe(this.getViewLifecycleOwner(), categoriesObserver);
    }
}