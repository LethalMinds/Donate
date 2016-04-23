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
    var deptCollection = req.dbo.collection('donations');
    deptCollection.find({"status" : "new"}).toArray(function (err, docs) {
        if (!err) {
            res.send(docs);
        } else {
            console.dir(err);
        }
    });
});

module.exports = router;
