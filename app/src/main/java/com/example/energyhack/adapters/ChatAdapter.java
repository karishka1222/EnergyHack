package com.example.energyhack.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.energyhack.R;
import com.example.energyhack.databinding.ActivityChatWithFriendsBinding;
import com.example.energyhack.databinding.ItemContainerReceivedMessageBinding;
import com.example.energyhack.databinding.ItemContainerSentMessageBinding;
import com.example.energyhack.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public final List<ChatMessage> chatMessages;
    private final Bitmap receiverProfileImage;
    private final String senderId;

    public static final int VIEW_TYPE_RECEIVED = 2;
    public static final int VIEW_TYPE_SENT = 1;

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ActivityChatWithFriendsBinding binding;

        ViewHolder(ActivityChatWithFriendsBinding activityChatWithFriendsBinding) {
            super(activityChatWithFriendsBinding.getRoot());
            binding = activityChatWithFriendsBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dataTime);
            System.out.println("data sent");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("position");
        if(viewType == VIEW_TYPE_SENT) {
            System.out.println("test");
//            System.out.println(position);
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );

        } else {
            System.out.println("test");
//            System.out.println(position);
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        if(getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
            System.out.println("test");
            System.out.println(position);
        } else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
            System.out.println("test");
            System.out.println(position);
        }
    }

    public void getItemText(int position, RecyclerView.ViewHolder holder) {
        System.out.println(holder.toString());

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        System.out.println("recycled");
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dataTime);
            System.out.println("data sent");
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dataTime);
            binding.imageProfile.setImageBitmap(receiverProfileImage);
            System.out.println("data sent2");
        }
    }
}
