package com.delivery.app.ui.controller;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.app.R;
import com.delivery.app.data.remote.model.Livraison;
import com.delivery.app.databinding.ItemConsultationBinding;
import com.delivery.app.util.Constants;

import java.text.NumberFormat;
import java.util.Locale;

public class ConsultationAdapter extends ListAdapter<Livraison, ConsultationAdapter.ConsultationViewHolder> {
    private final OnLivraisonClickListener listener;

    public interface OnLivraisonClickListener {
        void onClick(Livraison livraison);
    }

    public ConsultationAdapter(OnLivraisonClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Livraison> DIFF_CALLBACK = new DiffUtil.ItemCallback<Livraison>() {
        @Override
        public boolean areItemsTheSame(@NonNull Livraison oldItem, @NonNull Livraison newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Livraison oldItem, @NonNull Livraison newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };

    @NonNull
    @Override
    public ConsultationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConsultationBinding binding = ItemConsultationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ConsultationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ConsultationViewHolder extends RecyclerView.ViewHolder {
        private final ItemConsultationBinding binding;

        public ConsultationViewHolder(ItemConsultationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Livraison livraison) {
            binding.orderNumber.setText("#" + livraison.getOrderNumber());
            binding.clientName.setText(livraison.getClientName());
            binding.clientCity.setText(livraison.getClientCity());
            binding.clientPhone.setText(livraison.getClientPhone());
            
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
            binding.orderAmount.setText(format.format(livraison.getMontant()) + " TND");

            String etat = livraison.getEtat();
            String statusText;
            int statusColor;

            switch (etat) {
                case Constants.ETAT_ENCOURS:
                    statusText = "En cours";
                    statusColor = R.color.status_encours;
                    break;
                case Constants.ETAT_LIVRE:
                    statusText = "Livré";
                    statusColor = R.color.status_livre;
                    break;
                case Constants.ETAT_ANNULE:
                    statusText = "Annulé";
                    statusColor = R.color.status_annule;
                    break;
                default:
                    statusText = "En attente";
                    statusColor = R.color.text_secondary;
                    break;
            }

            binding.statusChip.setText(statusText);
            binding.statusChip.setChipBackgroundColorResource(statusColor);

            if (livraison.getLivreur() != null) {
                binding.livreurName.setText(livraison.getLivreur().getFullName());
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(livraison);
                }
            });
        }
    }
}