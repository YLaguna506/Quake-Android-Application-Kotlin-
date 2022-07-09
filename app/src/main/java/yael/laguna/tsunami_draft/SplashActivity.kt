package yael.laguna.tsunami_draft

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.TextView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val tvAppName: TextView = findViewById(R.id.tv_app_name)

        val typeFace: Typeface = Typeface.createFromAsset(assets, "carbon bl.ttf")
        tvAppName.typeface = typeFace

        Handler().postDelayed({
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        }, 2500)

    }
}