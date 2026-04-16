package com.delivery.app.ui.livreur;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.delivery.app.R;
import com.delivery.app.data.remote.model.Livraison;
import com.delivery.app.databinding.ItemLivraisonBinding;
import com.delivery.app.util.Constants;

import java.text.NumberFormat;
import java.util.Locale;

public class LivraisonAdapter extends ListAdapter<Livraison, LivraisonAdapter.LivraisonViewHolder> {
    private final OnLivraisonClickListener listener;
    private int lastPosition = -1;

    public interface OnLivraisonClickListener {
        void onClick(Livraison livraison);
    }

    public LivraisonAdapter(OnLivraisonClickListener listener) {
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
            return oldItem.getId().equals(newItem.getId()) &&
                   oldItem.getEtat().equals(newItem.getEtat());
        }
    };

    @NonNull
    @Override
    public LivraisonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLivraisonBinding binding = ItemLivraisonBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new LivraisonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LivraisonViewHolder holder, int position) {
        holder.bind(getItem(position));
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_animation);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    class LivraisonViewHolder extends RecyclerView.ViewHolder {
        private final ItemLivraisonBinding binding;

        public LivraisonViewHolder(ItemLivraisonBinding binding) {
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

            String status = livraison.getEtat();
            String statusText;
            int statusColor;

            switch (status) {
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

            String modePaiement = livraison.getModePaiement();
            if (modePaiement != null) {
                binding.paymentMode.setText(modePaiement);
                binding.paymentMode.setVisibility(View.VISIBLE);
            } else {
                binding.paymentMode.setVisibility(View.GONE);
            }

            String ville = livraison.getLocalisation() != null ? 
                livraison.getLocalisation().getVille() : "";
            binding.villeGroup.setText(ville);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(livraison);
                }
            });
        }
    }
}