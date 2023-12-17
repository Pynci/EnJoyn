package it.unimib.enjoyn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.User;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.NewViewHolder>{

    public interface OnItemClickListener{

        void onUserItemClick(User user);

    }

    private final UserRecyclerViewAdapter.OnItemClickListener onItemClickListener;

    private List<User> usersList;

    public UserRecyclerViewAdapter(List<User> users, OnItemClickListener onItemClickListener) {
        this.usersList = users;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public UserRecyclerViewAdapter.NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);

        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerViewAdapter.NewViewHolder holder, int position) {
        holder.bind(usersList.get(position));
    }


    @Override
    public int getItemCount() {
        if (usersList != null) {
            return usersList.size();
        }
        return 0;
    }

    public class NewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView textViewUsername;
        private final TextView textViewName;
        private final TextView textViewSurname;

        /**TODO  aggiungere immagine nell'adapter*/


        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.userListItem_textView_username);
            textViewName = itemView.findViewById(R.id.userListItem_textView_name);
            textViewSurname = itemView.findViewById(R.id.userListItem_textView_surname);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onUserItemClick(usersList.get(getAdapterPosition()));
        }

        public void bind(User user) {
            textViewUsername.setText(user.getUsername());
            textViewName.setText((user.getName()));
            textViewSurname.setText(user.getSurname());
        }
    }
}
