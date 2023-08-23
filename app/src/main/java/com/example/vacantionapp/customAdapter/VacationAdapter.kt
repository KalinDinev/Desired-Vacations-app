import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.vacantionapp.model.DesiredVacation
import com.example.vacantionapp.R
import com.example.vacantionapp.views.VacationsFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class VacationAdapter(private val vacationList: List<DesiredVacation>) :
    RecyclerView.Adapter<VacationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardImage = itemView.findViewById<ImageView>(R.id.cardImageView)
        val hotelName = itemView.findViewById<TextView>(R.id.hotelNameView)
        val locationName = itemView.findViewById<TextView>(R.id.hotelLocationView)
        val notificationBtn = itemView.findViewById<FloatingActionButton>(R.id.notificationBtn)
        val datePicker = itemView.findViewById<DatePicker>(R.id.datePicker)
        val timePicker = itemView.findViewById<TimePicker>(R.id.timePicker)
        val setNotificationBtn = itemView.findViewById<Button>(R.id.setNotificationBtn)
        var isTimePickerVisible = false
        var isDatePickerVisible = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.card_view, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentItem: DesiredVacation = vacationList[position]
        val bitmap = currentItem.photo
        val id = currentItem.id

        viewHolder.cardImage.setImageBitmap(bitmap)
        viewHolder.hotelName.text = currentItem.hotelName
        viewHolder.locationName.text = "Hotel in ${currentItem.location}"

        viewHolder.notificationBtn.setOnClickListener {
            if (viewHolder.isDatePickerVisible && viewHolder.isDatePickerVisible) {
                viewHolder.datePicker.visibility = View.GONE
                viewHolder.setNotificationBtn.visibility = View.GONE
                viewHolder.timePicker.visibility = View.GONE
                viewHolder.isDatePickerVisible = false
                viewHolder.isTimePickerVisible = false
            } else {
                viewHolder.datePicker.visibility = View.VISIBLE
                viewHolder.setNotificationBtn.visibility = View.VISIBLE
                viewHolder.timePicker.visibility = View.VISIBLE
                viewHolder.isDatePickerVisible = true
                viewHolder.isTimePickerVisible = true


                viewHolder.setNotificationBtn.setOnClickListener {
                    val year = viewHolder.datePicker.year
                    val month = viewHolder.datePicker.month
                    val dayOfMonth = viewHolder.datePicker.dayOfMonth

                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)
                    val notificationDate = calendar.timeInMillis

                    val hotelName = currentItem.hotelName


                    // Schedule the notification
                    VacationNotificationHandler.showNotification(
                        viewHolder.itemView.context, hotelName, notificationDate, id

                    )

                    Toast.makeText(
                        viewHolder.itemView.context,
                        "Notification scheduled for $hotelName",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewHolder.datePicker.visibility = View.GONE
                    viewHolder.setNotificationBtn.visibility = View.GONE
                    viewHolder.timePicker.visibility = View.GONE
                    viewHolder.isDatePickerVisible = false
                    viewHolder.isTimePickerVisible = false

                }
            }
        }

        viewHolder.itemView.setOnClickListener {
            val action = VacationsFragmentDirections.actionVacationsFragmentToDetailFragment(id)
            viewHolder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return vacationList.size
    }
}
