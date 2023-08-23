
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID

class VacationNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Get the data from the intent
        val hotelName = intent.getStringExtra(EXTRA_HOTEL_NAME)
        val currentDate =intent.getLongExtra(EXTRA_DATE,0)
        val currentId =intent.getIntExtra(EXTRA_ID,0)

        // Show the notification
        VacationNotificationHandler.showNotification(context, hotelName ?: "",currentDate,currentId)
    }

    companion object {
        const val EXTRA_HOTEL_NAME = "extra_hotel_name"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_ID ="test_id"
    }
}
