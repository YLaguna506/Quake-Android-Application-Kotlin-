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
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

class Earthquake_List : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var quakerecyclerView: RecyclerView
    private lateinit var quakeArrayList : ArrayList<Quake>
    private lateinit var db : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earthquake_list)

        setupActionBar()

        findViewById<NavigationView>(R.id.nav_view_2).setNavigationItemSelectedListener(this)

        quakerecyclerView = findViewById(R.id.quake_list_2)
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

                    for(quakeSnapshot in snapshot.children){

                        val quake = quakeSnapshot.getValue(Quake::class.java)!!
                        quakeArrayList.add(quake)

                    }

                    //Invert List for Correct Order
                    quakeArrayList.reverse()

                    val adapter = MyAdapter(quakeArrayList)
                    quakerecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : MyAdapter.onItemClickListener{

                        override fun onItemClick(position: Int, number: Int) {

                                //Toast.makeText(this@Earthquake_List, "1 - Clicked on item no. $position", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@Earthquake_List, QuakeDetails::class.java)

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
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_2)
        when (item.itemId) {

            R.id.nav_home -> {
                Toast.makeText(this, "Home Page", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }

            R.id.nav_quakelist -> {
                val intent = Intent(this, Earthquake_List::class.java)
                startActivity(intent)
            }

            R.id.nav_report -> {
                Toast.makeText(this, "Report Earthquake opened", Toast.LENGTH_SHORT).show()

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

    override fun onBackPressed(){

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_2)

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    private fun setupActionBar() {

        val toolBarMain: Toolbar = findViewById(R.id.toolbar_list_activity)
        setSupportActionBar(toolBarMain)
        toolBarMain.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolBarMain.setNavigationOnClickListener {
            toggleDrawer()
        }

    }

    private fun toggleDrawer() {

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_2)

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

}




