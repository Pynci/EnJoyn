package it.unimib.enjoyn.ui.main;

import android.content.res.Configuration;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.viewmodels.CategoryViewModel;
import it.unimib.enjoyn.ui.viewmodels.CategoryViewModelFactory;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModelFactory;
import it.unimib.enjoyn.util.ServiceLocator;
import it.unimib.enjoyn.util.SnackbarBuilder;

public class ProfileFragment extends Fragment {

    UserViewModel userViewModel;
    CategoryViewModel categoryViewModel;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        categoryViewModel = new ViewModelProvider(
                requireActivity(),
                new CategoryViewModelFactory(requireActivity().getApplication())).get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getViewLifecycleOwner();
        return inflater.inflate(R.layout.fragment_profile, container, false);
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

        ShapeableImageView propic = view.findViewById(R.id.fragmentProfile_imageView_propic);
        TextView propicUsernmame = view.findViewById(R.id.fragmentProfile_textView_username);
        TextView propicNameAndSurname = view.findViewById(R.id.fragmentProfile_textView_nameSurname);
        TextView description = view.findViewById(R.id.fragmentProfile_textView_descriptionText);
        ImageButton logout = view.findViewById(R.id.fragmentProfile_imageButton_logOut);

        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

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

                propicUsernmame.setText(user.getUsername());
                propicNameAndSurname.setText(user.getName() + " " + user.getSurname());
                description.setText(user.getDescription());
            }
        });

        logout.setOnClickListener(v -> {

            userViewModel.signOut().observe(this.getViewLifecycleOwner(), result -> {
                if(result.isSuccessful()) {
                    Navigation
                            .findNavController(view)
                            .navigate(R.id.action_profileFragment_to_authActivity2);
                }
                else{
                    String text = "Impossibile completare l'operazione richiesta";
                    Snackbar snackbar;
                    snackbar = SnackbarBuilder.buildErrorSnackbar(text, view, getContext(), currentTheme);
                    snackbar.show();
                }
            });
        });
    }
}