package com.cybersoft.jims_collector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybersoft.jims_collector.models.Report
import kotlinx.android.synthetic.main.activity_report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityReport : AppCompatActivity() {

    lateinit var apiInterface: APIInterface
    lateinit var glob: Globariables

    lateinit var adapterReport: AdapterReport

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        glob = applicationContext as Globariables
        apiInterface = APIClient.getClient().create(APIInterface::class.java)

        adapterReport = AdapterReport(this)
        RV_Report.layoutManager = LinearLayoutManager(this)
        RV_Report.setHasFixedSize(true)
        RV_Report.adapter = adapterReport

        val collectorID = glob.GetSharedPrefs(Globariables.SP_C_ID);
        GetDues(collectorID)
    }

    fun GetDues(CollectorID: String)
    {
        pbar.visibility = View.VISIBLE
        RV_Report.visibility = View.GONE

        val call = apiInterface.Report(CollectorID)

        call.enqueue(object : Callback<Report> {
            override fun onResponse(call: Call<Report>, response: Response<Report>)
            {
                val body = response.body()
                val data = body!!.data

                if( body.status.toBoolean() )
                {
                    lbl_TotalAmount.text = "Total Cash In Hand: ${"%.3f".format(data[0].balance)}"

                    adapterReport.setDataSource(data)
                    RV_Report.visibility = View.VISIBLE
                }
                else
                    Toast.makeText(this@ActivityReport, body.message, Toast.LENGTH_SHORT).show()

                pbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<Report>, t: Throwable) {
                Toast.makeText(this@ActivityReport, "Unable to connect to server, please try again later.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                call.cancel()
                pbar.visibility = View.GONE
                RV_Report.visibility = View.GONE
            }
        })
    }

}
