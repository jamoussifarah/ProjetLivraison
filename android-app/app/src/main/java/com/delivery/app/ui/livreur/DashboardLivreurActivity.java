package com.delivery.app.ui.livreur;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.delivery.app.DeliveryApplication;
import com.delivery.app.R;
import com.delivery.app.databinding.ActivityDashboardLivreurBinding;
import com.delivery.app.ui.livraisondetail.LivraisonDetailActivity;
import com.delivery.app.ui.message.MessageActivity;
import com.delivery.app.util.Resource;
import com.google.android.material.tabs.TabLayout;

public class DashboardLivreurActivity extends AppCompatActivity {
    private ActivityDashboardLivreurBinding binding;
    private DashboardLivreurViewModel viewModel;
    private LivraisonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardLivreurBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(DashboardLivreurViewModel.class);

        setupToolbar();
        setupTabs();
        setupRecyclerView();
        setupSwipeRefresh();
        setupFab();
        loadTodayDeliveries();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mes Livraisons");
        }
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white, getTheme()));
    }

    private void setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterByStatus(tab.getPosition() == 0 ? null : 
                    tab.getPosition() == 1 ? "EC" : tab.getPosition() == 2 ? "LI" : "AL");
                animateList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                binding.swipeRefresh.setRefreshing(true);
                loadTodayDeliveries();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new LivraisonAdapter(livraison -> {
            Intent intent = new Intent(DashboardLivreurActivity.this, LivraisonDetailActivity.class);
            intent.putExtra("livraison_id", livraison.getId());
            startActivity(intent);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        binding.recyclerView.startAnimation(animation);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(R.color.accent, R.color.success);
        binding.swipeRefresh.setOnRefreshListener(() -> loadTodayDeliveries());
    }

    private void setupFab() {
        binding.fabSync.setOnClickListener(v -> {
            viewModel.syncPendingDeliveries();
            Toast.makeText(this, "Synchronisation en cours...", Toast.LENGTH_SHORT).show();
        });

        binding.fabMessage.setOnClickListener(v -> {
            startActivity(new Intent(this, MessageActivity.class));
        });
    }

    private void loadTodayDeliveries() {
        viewModel.getTodayDeliveries().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    if (!binding.swipeRefresh.isRefreshing()) {
                        binding.progressBar.setVisibility(android.view.View.VISIBLE);
                    }
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(android.view.View.GONE);
                    binding.swipeRefresh.setRefreshing(false);
                    if (resource.data != null) {
                        adapter.submitList(resource.data);
                        updateStats(resource.data.size());
                    }
                    break;
                case ERROR:
                    binding.progressBar.setVisibility(android.view.View.GONE);
                    binding.swipeRefresh.setRefreshing(false);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void filterByStatus(String etat) {
        viewModel.filterByStatus(etat).observe(this, resource -> {
            if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
                adapter.submitList(resource.data);
            }
        });
    }

    private void updateStats(int total) {
        binding.totalCount.setText(String.valueOf(total));
    }

    private void animateList() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        binding.recyclerView.startAnimation(animation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_livreur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        DeliveryApplication.getInstance().clearAuthToken();
        finish();
    }
}