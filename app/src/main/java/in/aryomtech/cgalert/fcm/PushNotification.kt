package `in`.aryomtech.cgalert.fcm

data class PushNotification (
    val data:NotificationData,
    val to:String
)