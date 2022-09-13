package com.cybersoft.jims_collector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cybersoft.jims_collector.models.Login
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityLogin : AppCompatActivity() {

    lateinit var apiInterface: APIInterface
    lateinit var Glob: Globariables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Glob = applicationContext as Globariables

        apiInterface = APIClient.getClient().create(APIInterface::class.java)

        btn_Login.setOnClickListener {
            if( Glob.NetConnected() )
                Login(txt_Username.text.toString(), txt_Password.text.toString())
            else
                Toast.makeText(this@ActivityLogin, "Unable to connect to internet. Please check your internet connection.",
                    Toast.LENGTH_SHORT).show()
        }

    }

    fun Login(Username: String, Password: String)
    {
        btn_Login.startAnimation()

        val call = apiInterface.Login(Username, Password)

        call.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>)
            {
                val body = response.body()
                val data = body!!.data

                Toast.makeText(this@ActivityLogin, body.message, Toast.LENGTH_SHORT).show()

                if( body.status.toBoolean() )
                {
                    Glob.SetSharedPrefs(Globariables.SP_C_ID, data.c_id)
                    Glob.SetSharedPrefs(Globariables.SP_FULLNAME, data.name)
                    Glob.SetSharedPrefs(Globariables.SP_BALANCE, data.balance.toString())

                    startActivity(Intent(this@ActivityLogin, ActivityMain::class.java))
                    finish()
                }

                btn_Login.revertAnimation()
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                Toast.makeText(this@ActivityLogin, "Unable to connect to server, please try again later.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                call.cancel()
                btn_Login.revertAnimation()
            }
        })
    }

}
