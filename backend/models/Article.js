const mongoose = require('mongoose');

const articleSchema = new mongoose.Schema({
    refart: { type: String, required: true, unique: true },
    designation: { type: String, required: true },
    prix: { type: Number, required: true },
    categorie: { type: String },
    qtestk: { type: Number, default: 0 }
});

module.exports = mongoose.model('Article', articleSchema);