package com.delivery.app.ui.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.delivery.app.DeliveryApplication;
import com.delivery.app.R;
import com.delivery.app.databinding.ActivityDashboardControllerBinding;
import com.delivery.app.ui.livraisondetail.LivraisonDetailActivity;
import com.delivery.app.ui.livreur.LivraisonAdapter;
import com.delivery.app.ui.message.MessageActivity;
import com.delivery.app.util.Resource;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

public class DashboardControllerActivity extends AppCompatActivity {
    private ActivityDashboardControllerBinding binding;
    private DashboardControllerViewModel viewModel;
    private ConsultationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardControllerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(DashboardControllerViewModel.class);

        setupToolbar();
        setupTabs();
        setupRecyclerView();
        setupSwipeRefresh();
        setupFab();
        loadStats();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tableau de bord");
        }
    }

    private void setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadDeliveriesForTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                loadStats();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new ConsultationAdapter(livraison -> {
            Intent intent = new Intent(DashboardControllerActivity.this, LivraisonDetailActivity.class);
            intent.putExtra("livraison_id", livraison.getId());
            startActivity(intent);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(R.color.accent, R.color.success);
        binding.swipeRefresh.setOnRefreshListener(this::loadStats);
    }

    private void setupFab() {
        binding.fabMessage.setOnClickListener(v -> {
            startActivity(new Intent(this, MessageActivity.class));
        });
    }

    private void loadStats() {
        viewModel.getDashboardStats().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.swipeRefresh.setRefreshing(false);
                    if (resource.data != null) {
                        binding.totalCount.setText(String.valueOf(resource.data.getTotal()));
                        binding.encoursCount.setText(String.valueOf(resource.data.getEncours()));
                        binding.livresCount.setText(String.valueOf(resource.data.getLivrees()));
                        binding.annulesCount.setText(String.valueOf(resource.data.getAnnulees()));
                    }
                    loadDeliveriesForTab(binding.tabLayout.getSelectedTabPosition());
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(View.GONE);
                    binding.swipeRefresh.setRefreshing(false);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void loadDeliveriesForTab(int position) {
        String etat = position == 0 ? null : position == 1 ? "EC" : position == 2 ? "LI" : "AL";
        viewModel.getFilteredDeliveries(etat).observe(this, resource -> {
            if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
                adapter.submitList(resource.data);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        new MaterialAlertDialogBuilder(this)
            .setTitle("Déconnexion")
            .setMessage("Voulez-vous vraiment vous déconnecter?")
            .setPositiveButton("Oui", (dialog, which) -> {
                DeliveryApplication.getInstance().clearAuthToken();
                finish();
            })
            .setNegativeButton("Non", null)
            .show();
    }
}