package com.example.energyhack.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.energyhack.databinding.ItemsContainerUserBinding;
import com.example.energyhack.listeners.UserListener;
import com.example.energyhack.models.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;
    private final UserListener userListener;

    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("position");
        ItemsContainerUserBinding itemsContainerUserBinding = ItemsContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(itemsContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        System.out.println("position10");
        holder.setUserData(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }



    class UserViewHolder extends RecyclerView.ViewHolder {

        ItemsContainerUserBinding binding;

        UserViewHolder(ItemsContainerUserBinding itemsContainerUserBinding) {
            super(itemsContainerUserBinding.getRoot());
            binding = itemsContainerUserBinding;
        }

        void setUserData(User user) {
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }
    }

    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
