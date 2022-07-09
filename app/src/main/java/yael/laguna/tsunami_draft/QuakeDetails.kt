package yael.laguna.tsunami_draft

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class QuakeDetails : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quake_details)

        val date : TextView = findViewById(R.id.date_value_table_text)
        val depth : TextView = findViewById(R.id.depth_value_table_text)
        val id : TextView = findViewById(R.id.id_value_table_text)
        //val hlink : TextView = findViewById(R.id.link_value_text)
        val lat: TextView = findViewById(R.id.lat_table_value_text)
        val lon: TextView = findViewById(R.id.lon_table_value_text)
        val magnitude: TextView = findViewById(R.id.magnitude_value_table_text)
        val region: TextView = findViewById(R.id.region_value_table_text)
        val message : TextView = findViewById(R.id.message_value_text)

        //hlink.movementMethod = LinkMovementMethod.getInstance()
        //hlink.setLinkTextColor(Color.BLUE)

        date.text = intent.getStringExtra("Date")
        depth.text = intent.getStringExtra("Depth")
        id.text = intent.getStringExtra("Earthquake_ID")
        //hlink.text = Html.fromHtml("<a href=\""+ intent.getStringExtra("Hyperlink") + "\">" + "Link: " + intent.getStringExtra("Hyperlink") + "</a>")
        lat.text = intent.getStringExtra("Latitude")
        lon.text = intent.getStringExtra("Longitude")
        magnitude.text = intent.getStringExtra("Magnitude")
        region.text = intent.getStringExtra("Region")
        message.text = intent.getStringExtra("Tsunami_Message")

        createFragment()

    }

    // GOOGLE MAPS FUNCTIONS
    private fun createFragment() {

        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.maps, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)

    }


    private fun createMarker(latitude: Double, longitude: Double){
        val lat = latitude
        val lon = longitude

        map.apply {
            val location = LatLng(lat, lon)
            addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Puerto Rico ($lat , $lon)")
            )

            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f))

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        var lat: Double = 0.0
        var lon: Double = 0.0
        var lat_text: String = intent.getStringExtra("Latitude")!!
        var lon_text: String = intent.getStringExtra("Longitude")!!

        lat = lat_text.toDouble()
        lon = lon_text.toDouble()

       createMarker(lat,lon)

    }

}


