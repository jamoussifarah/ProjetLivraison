package com.delivery.app.ui.message;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.delivery.app.DeliveryApplication;
import com.delivery.app.R;
import com.delivery.app.data.remote.model.Message;
import com.delivery.app.databinding.ActivityMessageBinding;
import com.delivery.app.util.Constants;
import com.delivery.app.util.Resource;

public class MessageActivity extends AppCompatActivity {
    private ActivityMessageBinding binding;
    private MessageViewModel viewModel;
    private MessageAdapter adapter;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        currentUserId = DeliveryApplication.getInstance().getUserId();

        setupToolbar();
        setupRecyclerView();
        setupSendButton();
        loadMessages();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Messages");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        adapter = new MessageAdapter(currentUserId);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupSendButton() {
        binding.sendButton.setOnClickListener(v -> {
            String content = binding.messageInput.getText().toString().trim();
            if (!content.isEmpty()) {
                String toUser = DeliveryApplication.getInstance().getUserRole().equals(Constants.ROLE_LIVREUR) 
                    ? "CONTROLLER" : currentUserId;
                sendMessage(content, toUser);
            }
        });
    }

    private void loadMessages() {
        String toUser = DeliveryApplication.getInstance().getUserRole().equals(Constants.ROLE_LIVREUR) 
            ? "CONTROLLER" : currentUserId;
        
        viewModel.getMessages(toUser).observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    if (resource.data != null) {
                        adapter.submitList(resource.data);
                    }
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void sendMessage(String content, String toUser) {
        Message message = new Message(currentUserId, toUser, content, Message.TYPE_INFO);
        viewModel.sendMessage(message).observe(this, resource -> {
            if (resource.status == Resource.Status.SUCCESS) {
                binding.messageInput.setText("");
                Toast.makeText(this, "Message envoyé", Toast.LENGTH_SHORT).show();
                loadMessages();
            } else if (resource.status == Resource.Status.ERROR) {
                Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}