const functions = require("firebase-functions");
const admin = require("firebase-admin");

exports.ScoreManagement = functions.firestore
    .document("Users/{docId}/Score/{scoreId}")
    .onCreate((snap, context) => {
        const newValue = snap.data();
        const parentPath = context.params.docId;
        var newScore = null;

        Object.keys(newValue).forEach((date) => {
            newScore = newValue[date];
        })

        return admin.firestore().collection("Users").doc(parentPath).get().then((value) => {
            const maxScore = value.data().maxScore;
            const ref = admin.firestore().collection("Users").doc(parentPath)

            if (maxScore == null){
                ref.update({maxScore : newScore});
            }

            else{
                if (maxScore > newScore){
                    ref.update({maxScore : newScore});
                }
            }

        })

    })