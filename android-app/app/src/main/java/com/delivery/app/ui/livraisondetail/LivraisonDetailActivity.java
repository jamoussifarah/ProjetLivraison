package com.delivery.app.ui.livraisondetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.delivery.app.DeliveryApplication;
import com.delivery.app.R;
import com.delivery.app.databinding.ActivityLivraisonDetailBinding;
import com.delivery.app.data.remote.model.Message;
import com.delivery.app.util.Constants;
import com.delivery.app.util.Resource;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LivraisonDetailActivity extends AppCompatActivity {
    private ActivityLivraisonDetailBinding binding;
    private LivraisonDetailViewModel viewModel;
    private String livraisonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLivraisonDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LivraisonDetailViewModel.class);

        livraisonId = getIntent().getStringExtra("livraison_id");

        setupToolbar();
        setupStatusSpinner();
        setupButtons();
        loadLivraison();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Détails Livraison");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupStatusSpinner() {
        String[] statuses = {"En attente", "En cours", "Livré", "Annulé"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_dropdown_item, statuses);
        binding.statusSpinner.setAdapter(adapter);
    }

    private void setupButtons() {
        binding.callButton.setOnClickListener(v -> {
            String phone = binding.clientPhone.getText().toString();
            if (!phone.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });

        binding.mapsButton.setOnClickListener(v -> {
            String address = binding.clientAddress.getText().toString();
            if (!address.isEmpty()) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, 
                            Uri.parse("https://maps.google.com/?q=" + Uri.encode(address)));
                    startActivity(browserIntent);
                }
            }
        });

        binding.saveButton.setOnClickListener(v -> updateLivraison());

        binding.urgentButton.setOnClickListener(v -> sendUrgentMessage());
    }

    private void loadLivraison() {
        viewModel.getLivraison(livraisonId).observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    if (resource.data != null) {
                        com.delivery.app.data.remote.model.Livraison livraison = resource.data;
                        
                        binding.orderNumber.setText("#" + livraison.getOrderNumber());
                        binding.clientName.setText(livraison.getClientName());
                        binding.clientPhone.setText(livraison.getClientPhone());
                        binding.clientCity.setText(livraison.getClientCity());
                        binding.clientAddress.setText(livraison.getClientAddress());
                        
                        String etat = livraison.getEtat();
                        int statusIndex = "EC".equals(etat) ? 1 : "LI".equals(etat) ? 2 : "AL".equals(etat) ? 3 : 0;
                        binding.statusSpinner.setSelection(statusIndex);
                        
                        String modePaiement = livraison.getModePaiement();
                        if (modePaiement != null) {
                            binding.paymentMode.setText(modePaiement);
                        }
                        
                        binding.remarkInput.setText(livraison.getRemarque() != null ? livraison.getRemarque() : "");
                    }
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void updateLivraison() {
        String remarque = binding.remarkInput.getText().toString();
        int statusIndex = binding.statusSpinner.getSelectedItemPosition();
        String etat = statusIndex == 1 ? Constants.ETAT_ENCOURS : 
                  statusIndex == 2 ? Constants.ETAT_LIVRE : 
                  statusIndex == 3 ? Constants.ETAT_ANNULE : "AL";

        viewModel.updateLivraison(livraisonId, etat, remarque).observe(this, resource -> {
            if (resource.status == Resource.Status.SUCCESS) {
                Toast.makeText(this, "Livraison mise à jour", Toast.LENGTH_SHORT).show();
            } else if (resource.status == Resource.Status.ERROR) {
                Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendUrgentMessage() {
        new MaterialAlertDialogBuilder(this)
            .setTitle("Message Urgent")
            .setMessage("Envoyer un message urgent au contrôleur?")
            .setPositiveButton("Envoyer", (dialog, which) -> {
                String userId = DeliveryApplication.getInstance().getUserId();
                Message message = new Message(userId, "CONTROLLER", "Urgence! Problème avec la livraison " + 
                        binding.orderNumber.getText(), Message.TYPE_URGENCE);
                viewModel.sendMessage(message).observe(this, resource -> {
                    if (resource.status == Resource.Status.SUCCESS) {
                        Toast.makeText(this, "Message urgent envoyé", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Erreur: " + resource.message, Toast.LENGTH_SHORT).show();
                    }
                });
            })
            .setNegativeButton("Annuler", null)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}