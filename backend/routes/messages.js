const express = require('express');
const Message = require('../models/Message');
const { auth } = require('../middleware/auth');

const router = express.Router();

router.post('/', auth, async (req, res) => {
    try {
        const { toUser, message, type, commandeId, clientContact } = req.body;

        const newMessage = new Message({
            fromUser: req.user._id,
            toUser,
            message,
            type: type || 'INFO',
            commandeId,
            clientContact
        });

        await newMessage.save();

        const populatedMessage = await Message.findById(newMessage._id)
            .populate('fromUser', 'nom prenom')
            .populate('toUser', 'nom prenom');

        res.json({ success: true, data: populatedMessage });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

router.get('/', auth, async (req, res) => {
    try {
        const { toUser } = req.query;
        
        const query = {
            $or: [
                { fromUser: req.user._id },
                { toUser: req.user._id }
            ]
        };

        if (toUser) {
            query.$or = [
                { fromUser: req.user._id, toUser },
                { fromUser: toUser, toUser: req.user._id }
            ];
        }

        const messages = await Message.find(query)
            .populate('fromUser', 'nom prenom')
            .populate('toUser', 'nom prenom')
            .sort({ createdAt: -1 })
            .limit(100);

        res.json({ success: true, data: messages.reverse() });
    } catch (error) {
        res.status(500).json({ success: false, message: error.message });
    }
});

module.exports = router;