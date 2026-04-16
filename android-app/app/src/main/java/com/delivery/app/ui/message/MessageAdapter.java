package com.delivery.app.ui.message;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.app.R;
import com.delivery.app.data.remote.model.Message;
import com.delivery.app.databinding.ItemMessageBinding;

public class MessageAdapter extends ListAdapter<Message, MessageAdapter.MessageViewHolder> {
    private final String currentUserId;

    public MessageAdapter(String currentUserId) {
        super(DIFF_CALLBACK);
        this.currentUserId = currentUserId;
    }

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = ItemMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MessageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemMessageBinding binding;

        public MessageViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            binding.messageText.setText(message.getMessage());
            binding.timestamp.setText(message.getCreatedAt());

            boolean isFromMe = message.getFromUser().equals(currentUserId);
            
            if (isFromMe) {
                binding.messageCard.setCardBackgroundColor(
                        binding.getRoot().getContext().getResources().getColor(R.color.accent, binding.getRoot().getContext().getTheme()));
                binding.messageText.setTextColor(
                        binding.getRoot().getContext().getResources().getColor(R.color.white, binding.getRoot().getContext().getTheme()));
            } else {
                binding.messageCard.setCardBackgroundColor(
                        binding.getRoot().getContext().getResources().getColor(R.color.surface, binding.getRoot().getContext().getTheme()));
                binding.messageText.setTextColor(
                        binding.getRoot().getContext().getResources().getColor(R.color.text_primary, binding.getRoot().getContext().getTheme()));
            }

            if (message.isUrgent()) {
                binding.urgentIndicator.setVisibility(android.view.View.VISIBLE);
            } else {
                binding.urgentIndicator.setVisibility(android.view.View.GONE);
            }
        }
    }
}