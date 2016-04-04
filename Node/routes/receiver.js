/**
 * Created by Nishok on 3/31/2016.
 */
var express = require('express');
var mongoClient = require('mongodb').MongoClient;
var router = express.Router();
var dbo;

//Mongodb connection using mongodb client : the mangodb service has to be started on the server
mongoClient.connect("mongodb://127.0.0.1:27017/udonate", function (err, db) {
    if (err) {
        return console.dir(err);
    }
    dbo = db;
});

/* GET receiver listing. */
router.get('/', function(req, res, next) {
    res.send('respond with a resource');
});

module.exports = router;
