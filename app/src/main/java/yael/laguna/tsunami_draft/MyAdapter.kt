package yael.laguna.tsunami_draft

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.os.Handler
import com.google.android.material.imageview.ShapeableImageView
import kotlin.collections.ArrayList

class MyAdapter(private val quakeList : ArrayList<Quake>) : RecyclerView.Adapter<MyAdapter.myViewHolder>() {

     private lateinit var mListener: onItemClickListener

     interface onItemClickListener {
         fun onItemClick(position: Int, number: Int)
     }

     fun setOnItemClickListener(listener: onItemClickListener){
         mListener = listener
     }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)

        return myViewHolder(itemView, mListener)

    }


    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        val currentQuake = quakeList[position]

        holder.Date.text = currentQuake.Date
        //holder.Depth.text = currentQuake.Depth.toString()
        //holder.Earthquake_ID.text = currentQuake.Earthquake_ID.toString()
        //holder.Hyperlink.text = currentQuake.Hyperlink
        holder.Latitude.text = currentQuake.Latitude.toString()
        holder.Longitude.text = currentQuake.Longitude.toString()
        holder.Magnitude.text = currentQuake.Magnitude.toString()
        //holder.Region.text = currentQuake.Region
        //holder.Tsunami_Message.text = currentQuake.Tsunami_Message


        if(currentQuake.Magnitude!! < 4.5){
            holder.item.setBackgroundColor(Color.parseColor("#21B846"))
        }else if(currentQuake.Magnitude!! >= 4.5 && currentQuake.Magnitude!! < 5.5){
            holder.item.setBackgroundColor(Color.parseColor("#FFEB3B"))
        }else if(currentQuake.Magnitude!! >= 5.5 && currentQuake.Magnitude!! < 6.5){
            holder.item.setBackgroundColor(Color.parseColor("#FF9800"))
        }else if(currentQuake.Magnitude!! >= 6.5){
            holder.item.setBackgroundColor(Color.parseColor("#ED2B11"))
        }

    }

    override fun getItemCount(): Int {

           return quakeList.size

    }


    public class myViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val Date : TextView = itemView.findViewById(R.id.x_date)
        //val Depth : TextView = itemView.findViewById(R.id.x_depth)
        //val Earthquake_ID : TextView = itemView.findViewById(R.id.x_id)
        //val Hyperlink : TextView = itemView.findViewById(R.id.x_hyperlink)
        val Latitude : TextView = itemView.findViewById(R.id.x_latitude)
        val Longitude : TextView = itemView.findViewById(R.id.x_longitude)
        val Magnitude : TextView = itemView.findViewById(R.id.x_magnitude)
        //val Region : TextView = itemView.findViewById(R.id.x_region)
        //val Tsunami_Message : TextView = itemView.findViewById(R.id.x_message)


        var item: ShapeableImageView = itemView.findViewById(R.id.circleImageView)

        private var i : Int = 0

        init {

            itemView.setOnClickListener {

                i++

                val handler = Handler()

                handler.postDelayed({
                    if(i==1){
                        listener.onItemClick(adapterPosition,i)

                    } else if(i==2){
                        listener.onItemClick(adapterPosition,i)

                    }

                    i = 0

                }, 500)

            }

        }

    }

}
