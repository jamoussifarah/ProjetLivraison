const express = require('express');
const Livraison = require('../models/Livraison');
const { auth } = require('../middleware/auth');

const router = express.Router();

router.get('/stats', auth, async (req, res) => {
    try {
        const { date } = req.query;
        
        let startDate, endDate;
        
        if (date) {
            startDate = new Date(date);
            startDate.setHours(0, 0, 0, 0);
            endDate = new Date(startDate);
            endDate.setDate(endDate.getDate() + 1);
        } else {
            startDate = new Date();
            startDate.setHours(0, 0, 0, 0);
            endDate = new Date(startDate);
            endDate.setDate(endDate.getDate() + 1);
        }

        const query = {
            dateLivraison: { $gte: startDate, $lt: endDate }
        };

        const total = await Livraison.countDocuments(query);
        
        const parEtat = await Livraison.aggregate([
            { $match: query },
            { $group: { _id: '$etat', count: { $sum: 1 } } }
        ]);

        const parLivreur = await Livraison.aggregate([
            { $match: query },
            { $group: { _id: '$livreurId', count: { $sum: 1 } } }
        ]);

        const parClient = await Livraison.aggregate([
            { $match: query },
            { $lookup: { from: 'commandes', localField: 'commandeId', foreignField: '_id', as: 'commande' } },
            { $unwind: '$commande' },
            { $group: { _id: '$commande.clientId', count: { $sum: 1 } } }
        ]);

        const annulees = await Livraison.countDocuments({
            ...query,
            etat: 'AL'
        });

        const stats = {
            total,
            parEtat: parEtat.reduce((acc, item) => {
                acc[item._id] = item.count;
                return acc;
            }, {}),
            parLivreur: parLivreur.reduce((acc, item) => {
                acc[item._id?.toString()] = item.count;
                return acc;
            }, {}),
            annulees
        };

        res.json({ success: true, data: stats });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;