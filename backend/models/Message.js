const mongoose = require('mongoose');

const messageSchema = new mongoose.Schema({
    fromUser: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    toUser: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    message: { type: String, required: true },
    type: { type: String, enum: ['INFO', 'URGENCE'], default: 'INFO' },
    commandeId: { type: mongoose.Schema.Types.ObjectId, ref: 'Commande' },
    clientContact: { type: String },
    createdAt: { type: Date, default: Date.now }
});

messageSchema.index({ fromUser: 1, toUser: 1, createdAt: -1 });

module.exports = mongoose.model('Message', messageSchema);