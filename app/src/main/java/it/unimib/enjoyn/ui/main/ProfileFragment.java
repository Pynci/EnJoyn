package it.unimib.enjoyn.ui.main;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.CategoriesSelectionAdapter;
import it.unimib.enjoyn.databinding.FragmentProfileBinding;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.viewmodels.CategoryViewModel;
import it.unimib.enjoyn.ui.viewmodels.InterestViewModelFactory;
import it.unimib.enjoyn.ui.viewmodels.InterestsViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModelFactory;
import it.unimib.enjoyn.util.ServiceLocator;
import it.unimib.enjoyn.util.SnackbarBuilder;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding fragmentProfileBinding;
    private UserViewModel userViewModel;
    private CategoryViewModel categoryViewModel;
    private InterestsViewModel interestsViewModel;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        categoryViewModel = new ViewModelProvider(
                requireActivity()).get(CategoryViewModel.class);

        interestsViewModel = new ViewModelProvider(requireActivity(),
                new InterestViewModelFactory(requireActivity().getApplication())).get(InterestsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getViewLifecycleOwner();
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return fragmentProfileBinding.getRoot();
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

        ShapeableImageView propic = fragmentProfileBinding.fragmentProfileImageViewPropic;
        TextView username = fragmentProfileBinding.fragmentProfileTextViewUsername;
        TextView nameSurname = fragmentProfileBinding.fragmentProfileTextViewNameSurname;
        TextView description = fragmentProfileBinding.fragmentProfileTextViewDescriptionText;
        ImageButton logout = fragmentProfileBinding.fragmentProfileImageButtonLogOut;
        Button editProfile = fragmentProfileBinding.fragmentProfileTextButtonEditProfile;
        Button editInterests = fragmentProfileBinding.fragmentProfileTextButtonEditInterests;
        ListView listView = fragmentProfileBinding.fragmentProfileListView;
        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        listView.setDivider(null);

        Observer<Result> interestsObserver = result -> {
            if (result.isSuccessful() && result instanceof Result.CategorySuccess) {
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

        userViewModel.getUserPropic().observe(this.getViewLifecycleOwner(), result -> {

            if(result.isSuccessful() && result instanceof Result.SingleImageReadFromRemote)
                Glide
                        .with(this.getContext())
                        .load(((Result.SingleImageReadFromRemote) result).getUri())
                        .into(propic);
        });

        userViewModel.getCurrentUser().observe(this.getViewLifecycleOwner(), result -> {

            if(result.isSuccessful() && result instanceof Result.UserSuccess) {

                User user = ((Result.UserSuccess) result).getData();
                if(user != null){

                    if(user.getUsername() != null)
                        username.setText(user.getUsername());
                    else
                        username.setText("");

                    if(user.getName() != null && user.getSurname() != null)
                        nameSurname.setText(user.getName() + " " + user.getSurname());
                    else
                        nameSurname.setText("");

                    if(user.getDescription() != null)
                        description.setText(user.getDescription());
                    else
                        description.setText("");
                }
            }
        });

        logout.setOnClickListener(v -> userViewModel.signOut().observe(this.getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()) {

                navigateTo(R.id.action_profileFragment_to_authActivity2, true,true);
            }
            else{
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(R.string.failed_operation, view, getContext(), currentTheme);
                snackbar.show();
            }
        }));

        editProfile.setOnClickListener(v -> navigateTo(R.id.action_profileFragment_to_profileConfigurationFragment2, false, true));

        editInterests.setOnClickListener(v -> navigateTo(R.id.action_profileFragment_to_categoriesSelectionFragment2, false, true));

        interestsViewModel.getInterests().observe(this.getViewLifecycleOwner(), interestsObserver);
    }

    private void navigateTo(int destination, boolean finishActivity, boolean fromProfileFragment) {

        Bundle bundle = new Bundle();
        bundle.putBoolean("fromProfileFragment", fromProfileFragment);
        Navigation.findNavController(requireView()).navigate(destination, bundle);
        if (finishActivity) {
            requireActivity().finish();
        }
    }
}