package it.unimib.enjoyn.ui.auth;

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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.adapter.CategoriesSelectionAdapter;
import it.unimib.enjoyn.R;
import it.unimib.enjoyn.databinding.FragmentCategoriesSelectionBinding;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.viewmodels.CategoryViewModel;
import it.unimib.enjoyn.ui.viewmodels.InterestViewModelFactory;
import it.unimib.enjoyn.ui.viewmodels.InterestsViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModelFactory;
import it.unimib.enjoyn.util.ErrorMessagesUtil;
import it.unimib.enjoyn.util.ServiceLocator;

public class CategoriesSelectionFragment extends Fragment {

    private FragmentCategoriesSelectionBinding fragmentCategoriesSelectionBinding;
    private InterestsViewModel interestsViewModel;
    private CategoryViewModel categoryViewModel;
    private UserViewModel userViewModel;


    public CategoriesSelectionFragment() {
    }

    public static CategoriesSelectionFragment newInstance() {
        return new CategoriesSelectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interestsViewModel = new ViewModelProvider(
                requireActivity(),
                new InterestViewModelFactory(requireActivity().getApplication())).get(InterestsViewModel.class);
        categoryViewModel = new ViewModelProvider(
                requireActivity()).get(CategoryViewModel.class);
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCategoriesSelectionBinding = FragmentCategoriesSelectionBinding.inflate(inflater, container, false);
        return fragmentCategoriesSelectionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
        boolean isFromProfileFragment = getArguments().getBoolean("fromProfileFragment");
        Button buttonSkip = fragmentCategoriesSelectionBinding.fragmentCategoriesSelectionButtonSkip;
        Button buttonConfirm = fragmentCategoriesSelectionBinding.fragmentCategoriesSelectionButtonConfirm;

        ListView listView = fragmentCategoriesSelectionBinding.fragmentCategoriesSelectionListView;
        listView.setDivider(null);

        if(isFromProfileFragment) {
            buttonSkip.setText(R.string.annulla);
        }
        else{
            userViewModel.updateCategoriesSelectionStatus();
        }

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
                                if(imagesSorted.size() > 0){
                                    CategoriesSelectionAdapter customAdapter = new CategoriesSelectionAdapter(this.getContext(),
                                            categoryList, imagesSorted);
                                    listView.setAdapter(customAdapter);
                                }
                            }
                        });
            }
        };

        buttonSkip.setOnClickListener(v -> {
            if(!isFromProfileFragment)
                navigateTo(R.id.action_categoriesSelectionFragment_to_mainButtonMenuActivity, false);
            else
                navigateTo(R.id.action_categoriesSelectionFragment2_to_profileFragment, false);
        });

        buttonConfirm.setOnClickListener(v -> {

            if(!isFromProfileFragment){
                interestsViewModel.setUserInterests().observe(getViewLifecycleOwner(), result -> {
                    if (result.isSuccessful()) {
                        navigateTo(R.id.action_categoriesSelectionFragment_to_mainButtonMenuActivity, true);
                    }
                });
            }
            else{
                interestsViewModel.setUserInterests().observe(getViewLifecycleOwner(), result -> {
                    if (result.isSuccessful()) {
                        navigateTo(R.id.action_categoriesSelectionFragment2_to_authActivity2, false);
                    }
                });
            }
        });

        categoryViewModel.getAllCategories().observe(this.getViewLifecycleOwner(), categoriesObserver);
    }

    private void navigateTo(int destination, boolean finishActivity) {
        Navigation.findNavController(requireView()).navigate(destination);
        if (finishActivity) {
            requireActivity().finish();
        }
    }
}