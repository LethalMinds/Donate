/**
 * Created by Nishok on 3/31/2016.
 */
var express = require('express');
var router = express.Router();

/* GET receiver listing. */
router.get('/', function(req, res, next) {
    res.send('respond with a resource');
});

module.exports = router;
