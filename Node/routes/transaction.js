/**
 * Created by Nishok on 3/31/2016.
 */
var express = require('express');
var router = express.Router();

/* GET transaction listing. */
router.get('/', function(req, res, next) {
    res.send('respond with a resource');
});

/*
 POST method to add transaction and modify donation details and return the user with modified value
 */
router.post('/addTransaction', function (req, res) {
    var transactionCollection = req.dbo.collection('transactions');
    var donationCollection = req.dbo.collection('donations');
    var usersCollection = req.dbo.collection('users');
    transactionCollection.count({},function (err, count) {
            if (!err) {
                var newTranSize = count + 1;
                var trans = {
                    "_id": "transaction" + newTranSize,
                    "donor_id": req.body.username,
                    "receiver_id": req.body.receiver,
                    "status": "pending",
                    "payment_info": JSON.parse(req.body.payment_info)
                };
                transactionCollection.insertOne(trans, function (err, result) {
                    donationCollection.update({_id: req.body.donationId}, {$addToSet: {"transactions": "transaction" + newTranSize}, $set : { "status" : "process" } }, function (err, result) {
                        usersCollection.findAndModify({"username": req.body.username}, [
                                ['username', 1]
                            ], {
                                $addToSet: {"donationtransaction": "transaction" + newTranSize}
                            }, {new: true},
                            function (err, item) {
                                if (!err) {
                                    res.send(item);
                                } else {
                                    console.dir(err);
                                }
                            });
                    });
                });
            }
        }
    );
});

module.exports = router;
