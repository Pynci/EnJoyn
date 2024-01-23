package it.unimib.enjoyn.ui.auth.registration;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.adapter.CategoriesSelectionAdapter;
import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.CategoryViewModel;
import it.unimib.enjoyn.ui.CategoryViewModelFactory;

public class CategoriesSelectionFragment extends Fragment {

    private CategoryViewModel categoryViewModel;

    public CategoriesSelectionFragment() {
    }

    public static CategoriesSelectionFragment newInstance() {
        CategoriesSelectionFragment fragment = new CategoriesSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(
                requireActivity(),
                new CategoryViewModelFactory(requireActivity().getApplication())).get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonSkip = view.findViewById(R.id.fragmentCategoriesSelection_button_skip);
        Button buttonConfirm = view.findViewById(R.id.fragmentCategoriesSelection_button_confirm);

        ListView listView = view.findViewById(R.id.fragmentCategoriesSelection_ListView);
        listView.setDivider(null);

        Observer<Result> categoriesObserver = result -> {
            if (result.isSuccessful()) {
                List<Category> categoryList = ((Result.CategorySuccess) result).getCategoryList();

                categoryViewModel
                        .getAllImages(categoryList)
                        .observe(this.getViewLifecycleOwner(), result1 -> {


                            if (result1 instanceof Result.ImagesReadFromRemote) {

                                List<Uri> imagesNotSorted = ((Result.ImagesReadFromRemote) result1).getImagesUri();
                                List<Uri> imagesSorted = new ArrayList<>();

                                for (int i = 0; i < categoryList.size(); i++) {
                                    for (int j = 0; j < imagesNotSorted.size(); j++) {

                                        String tempUri = imagesNotSorted.get(j).toString();
                                        String tempCategory = categoryList.get(i).getNome().toLowerCase();

                                        if (tempUri.contains(tempCategory)) {
                                            imagesSorted.add(imagesNotSorted.get(j));
                                        }
                                    }
                                }

                                CategoriesSelectionAdapter customAdapter = new CategoriesSelectionAdapter(this.getContext(),
                                        categoryList, imagesSorted);
                                listView.setAdapter(customAdapter);
                            }
                        });
            }
        };

        buttonSkip.setOnClickListener(v -> {
            Navigation
                    .findNavController(view)
                    .navigate(R.id.action_categoriesSelectionFragment_to_mainButtonMenuActivity);
        });

        buttonConfirm.setOnClickListener(v -> {

            categoryViewModel.setUserInterests().observe(getViewLifecycleOwner(), result -> {
                if (result.isSuccessful()) {
                    Navigation
                            .findNavController(view)
                            .navigate(R.id.action_categoriesSelectionFragment_to_mainButtonMenuActivity);
                }
            });
        });

        categoryViewModel.getAllCategories().observe(this.getViewLifecycleOwner(), categoriesObserver);
    }
}