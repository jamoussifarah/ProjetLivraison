package com.delivery.app.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.delivery.app.data.local.AppDatabase;
import com.delivery.app.data.local.dao.LivraisonDao;
import com.delivery.app.data.local.entity.LivraisonEntity;
import com.delivery.app.data.remote.api.ApiClient;
import com.delivery.app.data.remote.api.ApiService;
import com.delivery.app.data.remote.model.ApiResponse;
import com.delivery.app.data.remote.model.DashboardStats;
import com.delivery.app.data.remote.model.Livraison;
import com.delivery.app.data.remote.model.LoginRequest;
import com.delivery.app.data.remote.model.Message;
import com.delivery.app.data.remote.model.User;
import com.delivery.app.util.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivraisonRepository {
    private final ApiService apiService;
    private final LivraisonDao livraisonDao;

    public LivraisonRepository(Context context) {
        this.apiService = ApiClient.getInstance().getApiService();
        this.livraisonDao = AppDatabase.getInstance(context).livraisonDao();
    }

    public LiveData<Resource<User>> login(String login, String password) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.login(new LoginRequest(login, password)).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    User user = response.body().getData();
                    String token = response.body().getToken();
                    result.setValue(Resource.success(user, token));
                } else {
                    result.setValue(Resource.error("Login failed", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Livraison>>> getTodayDeliveries() {
        MutableLiveData<Resource<List<Livraison>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getTodayDeliveries().enqueue(new Callback<ApiResponse<List<Livraison>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Livraison>>> call, Response<ApiResponse<List<Livraison>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Livraison> livraisons = response.body().getData();
                    cacheLivraisons(livraisons);
                    result.setValue(Resource.success(livraisons));
                } else {
                    result.setValue(Resource.error("Failed to load deliveries", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Livraison>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Livraison>>> getDeliveries(String date, String livreur, String etat, String client) {
        MutableLiveData<Resource<List<Livraison>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getDeliveries(date, livreur, etat, client).enqueue(new Callback<ApiResponse<List<Livraison>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Livraison>>> call, Response<ApiResponse<List<Livraison>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error("Failed to load deliveries", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Livraison>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<Livraison>> updateLivraison(String id, Livraison livraison) {
        MutableLiveData<Resource<Livraison>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.updateLivraison(id, livraison).enqueue(new Callback<ApiResponse<Livraison>>() {
            @Override
            public void onResponse(Call<ApiResponse<Livraison>> call, Response<ApiResponse<Livraison>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error("Failed to update delivery", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Livraison>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Livraison>>> syncLivraisons(List<Livraison> livraisons) {
        MutableLiveData<Resource<List<Livraison>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.syncLivraisons(livraisons).enqueue(new Callback<ApiResponse<List<Livraison>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Livraison>>> call, Response<ApiResponse<List<Livraison>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error("Sync failed", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Livraison>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<DashboardStats>> getDashboardStats(String date) {
        MutableLiveData<Resource<DashboardStats>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getDashboardStats(date).enqueue(new Callback<ApiResponse<DashboardStats>>() {
            @Override
            public void onResponse(Call<ApiResponse<DashboardStats>> call, Response<ApiResponse<DashboardStats>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error("Failed to load stats", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DashboardStats>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<Message>> sendMessage(Message message) {
        MutableLiveData<Resource<Message>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.sendMessage(message).enqueue(new Callback<ApiResponse<Message>>() {
            @Override
            public void onResponse(Call<ApiResponse<Message>> call, Response<ApiResponse<Message>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error("Failed to send message", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Message>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Message>>> getMessages(String toUser) {
        MutableLiveData<Resource<List<Message>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getMessages(toUser).enqueue(new Callback<ApiResponse<List<Message>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Message>>> call, Response<ApiResponse<List<Message>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.setValue(Resource.success(response.body().getData()));
                } else {
                    result.setValue(Resource.error("Failed to load messages", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Message>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    public void cacheLivraisons(List<Livraison> livraisons) {
        List<LivraisonEntity> entities = new ArrayList<>();
        for (Livraison livraison : livraisons) {
            entities.add(toEntity(livraison));
        }
        livraisonDao.insertAll(entities);
    }

    public LiveData<List<LivraisonEntity>> getCachedLivraisons(String livreurId) {
        return livraisonDao.getLivraisonsByLivreur(livreurId);
    }

    public List<LivraisonEntity> getPendingSyncLivraisons() {
        return livraisonDao.getPendingSyncLivraisons();
    }

    private LivraisonEntity toEntity(Livraison livraison) {
        LivraisonEntity entity = new LivraisonEntity();
        entity.setId(livraison.getId());
        entity.setCommandeId(livraison.getCommandeId());
        entity.setLivreurId(livraison.getLivreurId());
        entity.setDateLivraison(livraison.getDateLivraison());
        entity.setModePaiement(livraison.getModePaiement());
        entity.setEtat(livraison.getEtat());
        entity.setRemarque(livraison.getRemarque());
        
        if (livraison.getLocalisation() != null) {
            entity.setVille(livraison.getLocalisation().getVille());
            entity.setLatitude(livraison.getLocalisation().getLatitude());
            entity.setLongitude(livraison.getLocalisation().getLongitude());
        }

        if (livraison.getCommande() != null && livraison.getCommande().getClient() != null) {
            entity.setClientId(livraison.getCommande().getClient().getId());
            entity.setClientNom(livraison.getCommande().getClient().getNom());
            entity.setClientPrenom(livraison.getCommande().getClient().getPrenom());
            entity.setClientTelephone(livraison.getCommande().getClient().getTelephone());
            entity.setClientAdresse(livraison.getCommande().getClient().getAdresse());
            entity.setClientVille(livraison.getCommande().getClient().getVille());
            entity.setMontant(livraison.getCommande().getMontant());
        }

        entity.setSyncStatus(0);
        entity.setLastModified(System.currentTimeMillis());
        return entity;
    }
}