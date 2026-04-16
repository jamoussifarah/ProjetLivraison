package com.delivery.app.ui.controller;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.delivery.app.data.remote.model.DashboardStats;
import com.delivery.app.data.remote.model.Livraison;
import com.delivery.app.repository.LivraisonRepository;
import com.delivery.app.util.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardControllerViewModel extends AndroidViewModel {
    private final LivraisonRepository repository;
    private final String today;
    private final MutableLiveData<String> filterEtat = new MutableLiveData<>();
    private LiveData<Resource<List<Livraison>>> filteredLivraisons;
    private LiveData<Resource<DashboardStats>> dashboardStats;

    public DashboardControllerViewModel(@NonNull Application application) {
        super(application);
        repository = new LivraisonRepository(application);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        today = sdf.format(new Date());
    }

    public LiveData<Resource<DashboardStats>> getDashboardStats() {
        if (dashboardStats == null) {
            dashboardStats = repository.getDashboardStats(today);
        }
        return dashboardStats;
    }

    public LiveData<Resource<List<Livraison>>> getFilteredDeliveries(String etat) {
        filterEtat.setValue(etat);
        if (filteredLivraisons == null) {
            filteredLivraisons = Transformations.switchMap(filterEtat, status -> {
                return repository.getDeliveries(today, null, status, null);
            });
        }
        return filteredLivraisons;
    }
}