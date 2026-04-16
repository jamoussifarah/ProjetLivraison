package com.delivery.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.delivery.app.data.local.entity.LivraisonEntity;

import java.util.List;

@Dao
public interface LivraisonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LivraisonEntity livraison);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LivraisonEntity> livraisons);

    @Update
    void update(LivraisonEntity livraison);

    @Delete
    void delete(LivraisonEntity livraison);

    @Query("SELECT * FROM livraisons ORDER BY lastModified DESC")
    LiveData<List<LivraisonEntity>> getAllLivraisons();

    @Query("SELECT * FROM livraisons WHERE livreurId = :livreurId ORDER BY lastModified DESC")
    LiveData<List<LivraisonEntity>> getLivraisonsByLivreur(String livreurId);

    @Query("SELECT * FROM livraisons WHERE etat = :etat ORDER BY lastModified DESC")
    LiveData<List<LivraisonEntity>> getLivraisonsByEtat(String etat);

    @Query("SELECT * FROM vaccinations WHERE ville = :ville ORDER BY lastModified DESC")
    LiveData<List<LivraisonEntity>> getLivraisonsByVille(String ville);

    @Query("SELECT * FROM livraisons WHERE syncStatus != 0 ORDER BY lastModified DESC")
    List<LivraisonEntity> getPendingSyncLivraisons();

    @Query("SELECT * FROM livraisons WHERE id = :id")
    LiveData<LivraisonEntity> getLivraisonById(String id);

    @Query("SELECT * FROM livraisons WHERE id = :id")
    LivraisonEntity getLivraisonByIdSync(String id);

    @Query("DELETE FROM livraisons")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM livraisons WHERE etat = :etat")
    int getCountByEtat(String etat);

    @Query("SELECT COUNT(*) FROM livraisons")
    int getTotalCount();

    @Query("SELECT COUNT(*) FROM livraisons WHERE livreurId = :livreurId")
    int getCountByLivreur(String livreurId);
}