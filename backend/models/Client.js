const mongoose = require('mongoose');

const clientSchema = new mongoose.Schema({
    nom: { type: String, required: true },
    prenom: { type: String, required: true },
    adresse: { type: String, required: true },
    ville: { type: String, required: true },
    code_postal: { type: String },
    telephone: { type: String, required: true },
    email: { type: String }
});

module.exports = mongoose.model('Client', clientSchema);