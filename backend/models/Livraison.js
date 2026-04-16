const mongoose = require('mongoose');

const localisationSchema = new mongoose.Schema({
    ville: { type: String },
    latitude: { type: Number },
    longitude: { type: Number }
}, { _id: false });

const livraisonSchema = new mongoose.Schema({
    commandeId: { type: mongoose.Schema.Types.ObjectId, ref: 'Commande', required: true },
    livreurId: { type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    dateLivraison: { type: Date, default: Date.now },
    modePaiement: { type: String, enum: ['ESPECES', 'CARTE', 'ONLINE'] },
    etat: { type: String, enum: ['EC', 'LI', 'AL'], default: 'EC' },
    remarque: { type: String },
    localisation: { type: localisationSchema }
});

livraisonSchema.index({ livreurId: 1, dateLivraison: -1 });
livraisonSchema.index({ dateLivraison: -1 });

module.exports = mongoose.model('Livraison', livraisonSchema);