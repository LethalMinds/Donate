var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

/* POST authenticate user */
router.post('/authenticate', function (req, res) {
  var usersCollection = req.dbo.collection('users');
  usersCollection.findOne({"username" : req.body.username, "password":req.body.password},function (err, item) {
    if (!err) {
      res.send(item);
    } else {
      console.dir(err);
    }
  });
});

router.post('/findAndModify', function (req,res) {
  var usersCollection = req.dbo.collection('users');
  usersCollection.findAndModify({ "username": req.body.username }, [
        ['username', 1]
      ], {
        $set: {
          "email": req.body.email,
          "address": req.body.address,
          "dob": req.body.dob,
          "password": req.body.password
        }
      }, { new: true },
      function(err, item) {
        if (!err) {
          res.send(item);
        } else {
          console.dir(err);
        }
      });
});

/* POST get all transactions for user listing. */
router.post('/getAllTransactions', function (req, res) {
  var transCollection = req.dbo.collection('transactions');
  transCollection.find({"donor_id" : req.body.username}).toArray(function (err, docs) {
    if (!err) {
      res.send(docs);
    } else {
      console.dir(err);
    }
  });
});

module.exports = router;
