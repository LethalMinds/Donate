/**
 * Created by Nishok on 3/31/2016.
 */
var express = require('express');
var router = express.Router();

/* GET news listing. */
router.get('/', function(req, res, next) {
    res.send('respond with a resource');
});

/* POST getNews listing. */
router.post('/getNews', function (req, res) {
    var donationsCollection = req.dbo.collection('donations');
    donationsCollection.find({"status" : "process"}).toArray(function (err, docs) {
        if (!err) {
            res.send(docs);
        } else {
            console.dir(err);
        }
    });
});

router.post('/getDonations', function(req, res, next) {
    var donationCollection = req.dbo.collection('donations');
    donationCollection.find({"category" : req.body.category, "status" : req.body.status}).toArray(function (err, docs) {
        if (!err) {
            res.send(docs);
        } else {
            console.dir(err);
        }
    });
});


module.exports = router;
