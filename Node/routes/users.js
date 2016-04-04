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

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

/* POST authenticate user */
router.post('/authenticate', function (req, res) {
  var deptCollection = dbo.collection('users');
  deptCollection.findOne({"username" : req.body.username, "password":req.body.password},function (err, item) {
    if (!err) {
      res.send(item);
    } else {
      console.dir(err);
    }
  });
});

module.exports = router;
