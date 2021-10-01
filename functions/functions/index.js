const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp(functions.config().firebase);

const ScoreManagement = require('./ScoreManagement');
exports.ScoreManagement = ScoreManagement.ScoreManagement;
