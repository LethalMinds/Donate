var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function (req, res, next) {
    res.send('respond with a resource');
});

/* POST authenticate user */
router.post('/authenticate', function (req, res) {
    var usersCollection = req.dbo.collection('users');
    usersCollection.findOne({"username": req.body.username, "password": req.body.password}, function (err, item) {
        if (!err) {
            res.send(item);
        } else {
            console.dir(err);
        }
    });
});

/*
    POST method to modify user basic profile details and return the user with modified value
 */
router.post('/findAndModify', function (req, res) {
    var usersCollection = req.dbo.collection('users');
    usersCollection.findAndModify({"username": req.body.username}, [
            ['username', 1]
        ], {
            $set: {
                "email": req.body.email,
                "address": req.body.address,
                "dob": req.body.dob,
                "password": req.body.password
            }
        }, {new: true},
        function (err, item) {
            if (!err) {
                res.send(item);
            } else {
                console.dir(err);
            }
        });
});

/*
 POST method to obsolete user Card profile details and return the user with modified value
 */
router.post('/findAndObsoleteCard', function (req, res) {
    var usersCollection = req.dbo.collection('users');
    usersCollection.findAndModify({"username": req.body.username, "cards._id" : req.body.cardid}, [
            ['username', 1]
        ], {
            $set: {
                "cards.$.status": "obsolete"
            }
        }, {new: true},
        function (err, item) {
            if (!err) {
                res.send(item);
            } else {
                console.dir(err);
            }
        });
});

/*
 POST method to add user card profile details and return the user with modified value
 */
router.post('/findUserAddCard', function (req, res) {
    var usersCollection = req.dbo.collection('users');
    req.dbo.collection('users').aggregate(
        [
            {
                $project: {
                    item: "$username",
                    cardsize: {$size: "$cards"}
                }
            },
            {$match: {item: req.body.username}}
        ]
    ).toArray(function (err, item) {
        if (!err) {
            usersCollection.findAndModify({"username": req.body.username}, [
                    ['username', 1]
                ], {
                    $addToSet: {
                        "cards": {
                            "_id": "card" + (item[0].cardsize + 1),
                            "type": "visa",
                            "number": req.body.cardnumber,
                            "expiry": req.body.expiry,
                            "cvv": req.body.cvv,
                            "name": req.body.cardname,
                            "status": "active"
                        }
                    }
                }, {new: true},
                function (err, item) {
                    if (!err) {
                        res.send(item);
                    } else {
                        console.dir(err);
                    }
                });
        } else {
            console.dir(err);
        }
    });
});


/* POST get all transactions for user listing. */
router.post('/getAllTransactions', function (req, res) {
    var transCollection = req.dbo.collection('transactions');
    transCollection.find({"donor_id": req.body.username}).toArray(function (err, docs) {
        if (!err) {
            res.send(docs);
        } else {
            console.dir(err);
        }
    });
});

module.exports = router;
