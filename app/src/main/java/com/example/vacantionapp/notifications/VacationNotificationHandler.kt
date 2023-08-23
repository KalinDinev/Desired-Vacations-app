import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.vacantionapp.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object VacationNotificationHandler {

    fun showNotification(context: Context, hotelName: String, currentDate: Long, currentId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val NOTIFICATION_ID = currentId
        createNotificationChannel(context, hotelName)

        val notification = buildNotification(context, hotelName, currentDate)

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun buildNotification(
        context: Context, hotelName: String, vacationDate: Long
    ): Notification {
        val contentText = "Vacation at $hotelName is scheduled!"
        val formattedDate = formatDate(vacationDate)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Vacation in $hotelName").setContentText(contentText)
            .setContentText(formattedDate).setSmallIcon(R.drawable.baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true).build()
    }

    private fun formatDate(timeInMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun createNotificationChannel(context: Context, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = CHANNEL_ID
            val channelName = "Vacation Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private const val CHANNEL_ID = "vacation_channel"

}
