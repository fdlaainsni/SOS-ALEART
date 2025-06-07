const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.sendFireAlertNotification = functions.database
  .ref('/alarmStatus')
  .onWrite(async (change, context) => {
    const alarmData = change.after.val();
    if (alarmData && alarmData.active === true) {
      const payload = {
        notification: {
          title: "Telah Terjadi Kebakaran! 🔥",
          body: "Di Harap Mengevakuasi Diri!",
          sound: "default"
        }
      };

      try {
        await admin.messaging().sendToTopic("fire_alert", payload);
        console.log("Notification sent to fire_alert topic");
      } catch (error) {
        console.error("Error sending notification:", error);
      }
    }
  });
