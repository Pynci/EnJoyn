package it.unimib.enjoyn.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.UserRecyclerViewAdapter;
import it.unimib.enjoyn.databinding.FragmentFriendsBinding;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.util.JSONParserUtil;


public class FriendsFragment extends Fragment {
    private FragmentFriendsBinding fragmentFriendsBinding;
    public FriendsFragment() {
        // Required empty public constructor
    }
    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getViewLifecycleOwner();

        fragmentFriendsBinding = FragmentFriendsBinding.inflate(inflater, container, false);
        return fragmentFriendsBinding.getRoot();
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

        RecyclerView recyclerViewFriendsList = fragmentFriendsBinding.fragmentFriendsRecyclerView;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        List<User> userList = getUserListWithGSon();


        UserRecyclerViewAdapter userRecyclerViewAdapter = new UserRecyclerViewAdapter(userList,
                new UserRecyclerViewAdapter.OnItemClickListener(){
                    @Override
                    public void onUserItemClick(User user) {
                    }

                    @Override
                    public void onAddUserClick(User user) {

                    }
                },this.getContext() );
        recyclerViewFriendsList.setLayoutManager(layoutManager);
        recyclerViewFriendsList.setAdapter(userRecyclerViewAdapter);
    }


    private List<User> getUserListWithGSon() {
        JSONParserUtil jsonParserUtil = new JSONParserUtil(requireActivity().getApplication());
        try {


            Context context = requireActivity().getApplication().getApplicationContext();
            InputStream inputStream = context.getAssets().open("provaUser.json"); //apro file
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //estraggo json

            return jsonParserUtil.parseJSONUserFileWithGSon(bufferedReader).getUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}