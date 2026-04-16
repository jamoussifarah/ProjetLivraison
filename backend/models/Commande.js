const mongoose = require('mongoose');

const commandeArticleSchema = new mongoose.Schema({
    articleId: { type: mongoose.Schema.Types.ObjectId, ref: 'Article' },
    quantite: { type: Number, required: true }
}, { _id: false });

const commandeSchema = new mongoose.Schema({
    clientId: { type: mongoose.Schema.Types.ObjectId, ref: 'Client', required: true },
    date: { type: Date, default: Date.now },
    etat: { type: String, enum: ['EN_ATTENTE', 'EN_COURS', 'LIVRE', 'ANNULE'], default: 'EN_ATTENTE' },
    montant: { type: Number, required: true },
    articles: [commandeArticleSchema]
});

module.exports = mongoose.model('Commande', commandeSchema);