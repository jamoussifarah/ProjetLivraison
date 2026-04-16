const express = require('express');
const Livraison = require('../models/Livraison');
const Commande = require('../models/Commande');
const { auth } = require('../middleware/auth');

const router = express.Router();

router.get('/today', auth, async (req, res) => {
    try {
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        const tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);

        let query = {
            dateLivraison: { $gte: today, $lt: tomorrow }
        };

        if (req.user.role === 'LIVREUR') {
            query.livreurId = req.user._id;
        }

        const livraisons = await Livraison.find(query)
            .populate('commandeId')
            .populate('livreurId')
            .sort({ dateLivraison: -1 });

        const enrichedLivraisons = await Promise.all(
            livraisons.map(async (liv) => {
                const livraison = liv.toObject();
                if (liv.commandeId) {
                    const commande = await Commande.findById(liv.commandeId._id)
                        .populate('clientId');
                    livraison.commande = commande;
                }
                return livraison;
            })
        );

        res.json({ success: true, data: enrichedLivraisons });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

router.get('/', auth, async (req, res) => {
    try {
        const { date, livreur, etat, client } = req.query;
        
        let query = {};

        if (date) {
            const startDate = new Date(date);
            startDate.setHours(0, 0, 0, 0);
            const endDate = new Date(startDate);
            endDate.setDate(endDate.getDate() + 1);
            query.dateLivraison = { $gte: startDate, $lt: endDate };
        }

        if (livreur) query.livreurId = livreur;
        if (etat) query.etat = etat;

        const livraisons = await Livraison.find(query)
            .populate('commandeId')
            .populate('livreurId')
            .sort({ dateLivraison: -1 });

        res.json({ success: true, data: commandes = livraisons });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

router.put('/:id', auth, async (req, res) => {
    try {
        const { etat, remarque, modePaiement } = req.body;
        const { id } = req.params;

        const livraison = await Livraison.findById(id);
        
        if (!livraison) {
            return res.status(404).json({ success: false, message: 'Livraison non trouvée' });
        }

        if (etat) livraison.etat = etat;
        if (remarque !== undefined) livraison.remarque = remarque;
        if (modePaiement) livraison.modePaiement = modePaiement;

        await livraison.save();

        res.json({ success: true, data: livraison });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

router.post('/sync', auth, async (req, res) => {
    try {
        const { livraison } = req.body;

        const results = [];
        
        for (const livData of livraison) {
            const livraison = await Livraison.findByIdAndUpdate(
                livData._id,
                {
                    etat: livData.etat,
                    remarque: livData.remarque,
                    modePaiement: livData.modePaiement,
                    livreurId: livData.livreurId || req.user._id
                },
                { new: true, upsert: true }
            );
            results.push(livraison);
        }

        res.json({ success: true, data: results });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;