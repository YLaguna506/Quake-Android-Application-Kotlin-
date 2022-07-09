package yael.laguna.tsunami_draft

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private lateinit var quakerecyclerView: RecyclerView
    private lateinit var quakeArrayList : ArrayList<Quake>
    private lateinit var db : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseMessaging.getInstance().subscribeToTopic("Earthquake")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBar()

        // Assign the NavigationView.OnNavigationItemSelectedListener to navigation view.
        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener(this)

        createFragment()

        deploy()

    }

    fun deploy(){

        quakerecyclerView = findViewById(R.id.quake_list)
        quakerecyclerView.layoutManager = LinearLayoutManager(this)
        quakerecyclerView.setHasFixedSize(true)

        quakeArrayList = arrayListOf<Quake>()

       getQuakeData()

    }

        private fun getQuakeData(){

            db = FirebaseDatabase.getInstance().getReference("Earthquake")

            db.addValueEventListener(object: ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()){
                        var i = 0
                        for(quakeSnapshot in snapshot.children){

                            val quake = quakeSnapshot.getValue(Quake::class.java)!!
                            quakeArrayList.add(quake)
                            i++

                        }

                        //Invert List for Correct Order
                        quakeArrayList.reverse()

                        while (quakeArrayList.size > 5) {
                            quakeArrayList.removeAt(5)
                        }

                        val adapter = MyAdapter(quakeArrayList)
                        quakerecyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object : MyAdapter.onItemClickListener{

                            override fun onItemClick(position: Int, number: Int) {

                                //LOADS MARKERS WITHOUT CRASHING
                                for(item in quakeArrayList){
                                    item.Latitude?.let { item.Longitude?.let { it1 -> createMarker_2(it, it1) } }
                                }

                                if(number==1){
                                    //Toast.makeText(this@MainActivity, "1 - Clicked on item no. $position", Toast.LENGTH_SHORT).show()
                                    quakeArrayList[position].Latitude?.let {
                                        quakeArrayList[position].Longitude?.let { it1 ->
                                            LatLng(
                                                it, it1
                                            )
                                        }
                                    }?.let { CameraUpdateFactory.newLatLngZoom(it, 8f) }
                                        ?.let { map.animateCamera(it) }

                                } else if(number==2){

                                    //Toast.makeText(this@MainActivity, "2 - Clicked on item no. $position", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@MainActivity, QuakeDetails::class.java)

                                    intent.putExtra("Date", quakeArrayList[position].Date)
                                    intent.putExtra("Depth", quakeArrayList[position].Depth.toString())
                                    intent.putExtra("Earthquake_ID", quakeArrayList[position].Earthquake_ID.toString())
                                    intent.putExtra("Hyperlink", quakeArrayList[position].Hyperlink)
                                    intent.putExtra("Latitude", quakeArrayList[position].Latitude.toString())
                                    intent.putExtra("Longitude", quakeArrayList[position].Longitude.toString())
                                    intent.putExtra("Magnitude", quakeArrayList[position].Magnitude.toString())
                                    intent.putExtra("Region", quakeArrayList[position].Region)
                                    intent.putExtra("Tsunami_Message", quakeArrayList[position].Tsunami_Message)

                                    startActivity(intent)
                                }
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }

    private fun setupActionBar(){

        val toolBarMain: Toolbar = findViewById(R.id.toolbar_main_activity)

        setSupportActionBar(toolBarMain)
        toolBarMain.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolBarMain.setNavigationOnClickListener{
            toggleDrawer()
        }

    }

    private fun toggleDrawer(){

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }

    }

    override fun onBackPressed(){

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            finishAffinity()
            finish()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        when(item.itemId){

            R.id.nav_home -> {
                startActivity(Intent(this, MainActivity::class.java))
            }

            R.id.nav_quakelist -> {
                val intent = Intent(this, Earthquake_List::class.java)
                startActivity(intent)
            }

            R.id.nav_report -> {
                val url = "http://www.prsn.uprm.edu/Spanish/DFI/reportesismos.php"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

            R.id.nav_settings -> {
                val intent = Intent(this, settingsActivity::class.java)
                startActivity(intent)
                //Toast.makeText(this, "Settings opened", Toast.LENGTH_SHORT).show()

            }

        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
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

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Sets the map type to be "SATELLITE"
        //map.mapType = GoogleMap.MAP_TYPE_SATELLITE

        createMarker(18.30, -66.40)
    }

    private fun createMarker(latitude: Double, longitude: Double){
        val lat = latitude
        val lon = longitude

        map.apply {
            val location = LatLng(lat, lon)
            addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Puerto Rico")
            )

            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8f))

        }
    }

    private fun createMarker_2(latitude2: Double, longitude2: Double){
        val lat = latitude2
        val lon = longitude2

         map.apply {
            val location = LatLng(lat, lon)
              addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Puerto Rico $lat $lon")
            )
        }
    }

}
