package com.cybersoft.jims_collector

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cybersoft.jims_collector.models.*
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.*
import java.net.URLEncoder


class ActivityMain : AppCompatActivity() {

    lateinit var apiInterface: APIInterface
    lateinit var smsInterface: SMSAPIInterface
    lateinit var glob: Globariables

    var printerConnected = false

    var balance = 0.0
    var totalPaying = 0.0

    lateinit var adapterDues: AdapterDues
    lateinit var sabeelNo: String
    lateinit var memberID: String
    lateinit var memberContact: String

    lateinit var events: List<Events.EventsData>
    var eventsList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        glob = applicationContext as Globariables
        apiInterface = APIClient.getClient().create(APIInterface::class.java)
        smsInterface = APIClient.getStringClient().create(SMSAPIInterface::class.java)

        if( ! glob.hasPermission(this, android.Manifest.permission.BLUETOOTH) )
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN),
                Globariables.PSN_READ_PH_ST)
        }

        balance = glob.GetSharedPrefs(Globariables.SP_BALANCE).toDouble()

        lbl_Collector.text = "${glob.GetSharedPrefs(Globariables.SP_FULLNAME)} " +
                "| Balance: ${"%.3f".format(balance)}"

//        Printooth.init(this.applicationContext)
//
//        startActivityForResult(Intent(this, ScanningActivity::class.java),
//            ScanningActivity.SCANNING_FOR_PRINTER)

        GetEvents()

        btn_AddPayment.setOnClickListener{

            val dlg = Dialog(this@ActivityMain)
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dlg.setContentView(R.layout.dlg_otherpayment)

            val txt_Event = dlg.findViewById<Spinner>(R.id.txt_Event)
            val txt_Amount = dlg.findViewById<EditText>(R.id.txt_Amount)
            val btn_AddEvent = dlg.findViewById<Button>(R.id.btn_AddEvent)

            txt_Event.adapter = ArrayAdapter<String>(this@ActivityMain, android.R.layout.simple_list_item_1,
                eventsList)

            txt_Amount.setText("")

            btn_AddEvent.setOnClickListener {

                if( !txt_Amount.text.toString().isNullOrEmpty() || txt_Amount.text.toString().toDouble() != 0.0 )
                {
                    val HeadID = events[txt_Event.selectedItemPosition].eventID
                    val HeadName = events[txt_Event.selectedItemPosition].eventName

                    var item = Dues().DuesHead()
                    item.headID = HeadID.toInt()
                    item.headName = HeadName
                    item.amountToBePaid = txt_Amount.text.toString().toDouble()

                    adapterDues.addItem(item)
                }
                else
                    Toast.makeText(this@ActivityMain, "Cannot add 0 amount", Toast.LENGTH_SHORT).show()

                dlg.dismiss()
            }

            dlg.show()

        }

        btn_Dues.setOnClickListener {

            GetDues(txt_Sabeel.text.toString())
            Globariables.hideKeyboardFrom(this@ActivityMain, txt_Sabeel)

        }

        adapterDues = AdapterDues(this) {

            totalPaying = it
            lbl_TotalAmount.setText("%.3f".format(it))

        }

        RV_Dues.layoutManager = LinearLayoutManager(this)
        RV_Dues.setHasFixedSize(true)
        RV_Dues.adapter = adapterDues

        btn_CreateReceipt.setOnClickListener {

            var payments: MutableList<ReceiptDetail> = mutableListOf()

            adapterDues.getDataSource().forEach {

                if( it.headID != -1 && it.amountToBePaid != 0.0 )
                {
                    var payment = ReceiptDetail()
                    payment.event_id = it.headID
                    payment.event_name = it.headName
                    payment.amt = it.amountToBePaid

                    payments.add(0, payment)
                }

            }

            if( payments.size > 0 )
                CreateReceipt(memberID, sabeelNo, payments)
            else
                Toast.makeText(this@ActivityMain,
                    "Cannot create receipt with 0 payment. Please enter paid amount.", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId)
        {
            R.id.menu_printReceipt -> {

                val dlg = Dialog(this@ActivityMain)
                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dlg.setContentView(R.layout.dlg_reprint)

                val txt_ReceiptNumber = dlg.findViewById<EditText>(R.id.txt_ReceiptNumber)
                val btn_Reprint = dlg.findViewById<Button>(R.id.btn_Reprint)

                txt_ReceiptNumber.setText("")

                btn_Reprint.setOnClickListener {

                    if( !txt_ReceiptNumber.text.toString().isNullOrEmpty() )
                    {
                        val receiptNumber = txt_ReceiptNumber.text.toString()
                        PrintReceipt(receiptNumber)
                    }
                    else
                        Toast.makeText(this@ActivityMain, "Please enter a receipt number", Toast.LENGTH_SHORT).show()

                    dlg.dismiss()
                }

                dlg.show()

                true

            }

            R.id.menu_reports -> {

                startActivity(Intent(this@ActivityMain, ActivityReport::class.java))
                true

            }

            else -> true
        }

    }

    fun GetEvents()
    {
        pbar.visibility = View.VISIBLE
        RV_Dues.visibility = View.GONE
        LL_Footer.visibility = View.GONE
        btn_AddPayment.visibility = View.GONE
        btn_CreateReceipt.visibility = View.GONE

        val call = apiInterface.Events()

        call.enqueue(object : Callback<Events> {
            override fun onResponse(call: Call<Events>, response: Response<Events>)
            {
                val body = response.body()
                val data = body!!.data

                if( body.status.toBoolean() )
                {
                    events = data

                    events.forEach {
                        eventsList.add(it.eventName)
                    }
                }
                else
                {
                    Toast.makeText(this@ActivityMain, body.message, Toast.LENGTH_SHORT).show()
                }

                pbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<Events>, t: Throwable) {
                Toast.makeText(this@ActivityMain, "Unable to connect to server, please try again later.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                call.cancel()
                pbar.visibility = View.GONE
                RV_Dues.visibility = View.GONE
                LL_Footer.visibility = View.GONE
                btn_AddPayment.visibility = View.GONE
                btn_CreateReceipt.visibility = View.GONE
            }
        })
    }

    fun GetDues(SabeelNo: String)
    {
        pbar.visibility = View.VISIBLE
        RV_Dues.visibility = View.GONE
        LL_Footer.visibility = View.GONE
        btn_AddPayment.visibility = View.GONE
        btn_CreateReceipt.visibility = View.GONE

        val call = apiInterface.Dues(SabeelNo)

        call.enqueue(object : Callback<Dues> {
            override fun onResponse(call: Call<Dues>, response: Response<Dues>)
            {
                val body = response.body()
                val data = body!!.data

                val dues = data.dues ?: mutableListOf()

                var heading = Dues().DuesHead()
                heading.headID = -1

                dues.add(0, heading)

                val member = data.member
                lbl_MemberName.text = "${member.memberName}"

                this@ActivityMain.sabeelNo = SabeelNo
                this@ActivityMain.memberID = member.memberID
                this@ActivityMain.memberContact = member.memberContact

                if( body.status.toBoolean() )
                {
//                    data.forEach {
//                        it.amountToBePaid = it.amount
//                    }

                    adapterDues.setDataSource(dues)

                }
                else
                {
                    adapterDues.setDataSource(dues)
                    Toast.makeText(this@ActivityMain, body.message, Toast.LENGTH_SHORT).show()
                }

                RV_Dues.visibility = View.VISIBLE
                LL_Footer.visibility = View.VISIBLE
                btn_AddPayment.visibility = View.VISIBLE
                btn_CreateReceipt.visibility = View.VISIBLE
                pbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<Dues>, t: Throwable) {
                Toast.makeText(this@ActivityMain, "Unable to connect to server, please try again later.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                call.cancel()
                pbar.visibility = View.GONE
                RV_Dues.visibility = View.GONE
                LL_Footer.visibility = View.GONE
                btn_AddPayment.visibility = View.GONE
                btn_CreateReceipt.visibility = View.GONE
            }
        })
    }

    fun CreateReceipt(MemberID: String, SabeelNo: String, payments: List<ReceiptDetail>)
    {
        Globariables.hideKeyboard(this@ActivityMain)

        pbar.visibility = View.VISIBLE
        RV_Dues.visibility = View.GONE
        LL_Footer.visibility = View.GONE
        btn_AddPayment.visibility = View.GONE
        btn_CreateReceipt.visibility = View.GONE

        val CollectorID = glob.GetSharedPrefs(Globariables.SP_C_ID);

        var receipt = ReceiptMaster()
        receipt.collectorID = CollectorID.toInt()
        receipt.sabeelNo = SabeelNo
        receipt.memberID = MemberID
        receipt.payments = payments

        val call = apiInterface.CreateReceipt(receipt)

        call.enqueue(object : Callback<General> {
            override fun onResponse(call: Call<General>, response: Response<General>)
            {
                val body = response.body()
                val ReceiptNo = body!!.data

                Toast.makeText(this@ActivityMain, body.message, Toast.LENGTH_SHORT).show()

                if( body.status.toBoolean() )
                {
                    updateBalance()
                    sendSMS(ReceiptNo)
                    //PrintReceipt(ReceiptNo)
                }

                pbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<General>, t: Throwable) {
                Toast.makeText(this@ActivityMain, "Unable to connect to server, please try again later.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                call.cancel()
                pbar.visibility = View.GONE
                RV_Dues.visibility = View.GONE
                LL_Footer.visibility = View.GONE
                btn_AddPayment.visibility = View.GONE
                btn_CreateReceipt.visibility = View.GONE
            }
        })
    }

    fun PrintReceipt(ReceiptNo: String)
    {
        pbar.visibility = View.VISIBLE

        val call = apiInterface.PrintReceipt(ReceiptNo)

        call.enqueue(object : Callback<Receipt> {
            override fun onResponse(call: Call<Receipt>, response: Response<Receipt>)
            {
                val body = response.body()
                val receipt = body!!.data

                if( body.status.toBoolean() )
                {
                    print(receipt)
                }
                else
                {
                    Toast.makeText(this@ActivityMain, body.message, Toast.LENGTH_SHORT).show()
                }

                pbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<Receipt>, t: Throwable) {
                Toast.makeText(this@ActivityMain, "Unable to connect to server, please try again later.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                call.cancel()
                pbar.visibility = View.GONE
            }
        })
    }

    fun sendSMS(receiptNo: String)
    {
        pbar.visibility = View.VISIBLE

        val curDate = Globariables.DateLongToString(System.currentTimeMillis(), "dd-MMM-yyyy HH:mm")

        val msg = "Sabeel : $sabeelNo, Receipt : $receiptNo, Date : $curDate, Amt : KD ${"%.3f".format(totalPaying)}, Shukran"

        val url = "http://62.215.226.164/fccsms.aspx?uid=Burhani&s=InfoText&g=965$memberContact" +
                "&iid=1022&l=3&t=1&m=${URLEncoder.encode(msg, "UTF-8")}"

        val call = smsInterface.getStringResponse(url)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>)
            {
                val body = response.body()

                print(body)

                pbar.visibility = View.GONE
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@ActivityMain, "Unable to connect to server, please try again later.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
                call.cancel()
                pbar.visibility = View.GONE
            }
        })
    }

    fun print(data: List<Receipt.ReceiptData>)
    {
        var printables = ArrayList<Printable>()

        printables.add(normalText("Najmi Mohallah", DefaultPrinter.ALIGNMENT_CENTER,
            DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_LARGE,
            DefaultPrinter.UNDERLINED_MODE_ON, 1))

        printables.add(normalText("Salmiyah, Kuwait", DefaultPrinter.ALIGNMENT_CENTER,
            DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_LARGE,
            DefaultPrinter.UNDERLINED_MODE_ON, 2))

        printables.add(normalText("Receipt No:   ", DefaultPrinter.ALIGNMENT_LEFT,
            DefaultPrinter.EMPHASIZED_MODE_NORMAL, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 0))

        printables.add(normalText("${data[0].receiptNo}", DefaultPrinter.ALIGNMENT_LEFT,
            DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_ON, 2))

        printables.add(normalText("Receipt Date: ", DefaultPrinter.ALIGNMENT_LEFT,
            DefaultPrinter.EMPHASIZED_MODE_NORMAL, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 0))

        printables.add(normalText("${data[0].receiptDate}", DefaultPrinter.ALIGNMENT_LEFT,
            DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_ON, 2))

        printables.add(normalText("Collector:    ",
            DefaultPrinter.ALIGNMENT_LEFT, DefaultPrinter.EMPHASIZED_MODE_NORMAL, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 0))

        printables.add(normalText("${data[0].collectorID} | ${data[0].collector}", DefaultPrinter.ALIGNMENT_LEFT,
            DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_ON, 2))

        printables.add(normalText("Sabeel No:    ",
            DefaultPrinter.ALIGNMENT_LEFT, DefaultPrinter.EMPHASIZED_MODE_NORMAL, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 0))

        printables.add(normalText("${data[0].sabeelNo}", DefaultPrinter.ALIGNMENT_LEFT,
            DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_ON, 1))

        printables.add(normalText("              ",
            DefaultPrinter.ALIGNMENT_LEFT, DefaultPrinter.EMPHASIZED_MODE_NORMAL, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 0))

        printables.add(normalText("${data[0].memberName}", DefaultPrinter.ALIGNMENT_LEFT,
            DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_ON, 2))

        printables.add(normalText("------------------------------------------------",
            DefaultPrinter.ALIGNMENT_CENTER, DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 1))

        var total = 0.0

        data.forEach {

            total += it.amount.toDouble()

            val amount_str = "%.3f".format(it.amount.toDouble())

            printables.add(normalText("${it.headName}:  KWD $amount_str",
                DefaultPrinter.ALIGNMENT_LEFT, DefaultPrinter.EMPHASIZED_MODE_NORMAL, DefaultPrinter.FONT_SIZE_NORMAL,
                DefaultPrinter.UNDERLINED_MODE_OFF, 1))

            val dues_str = "%.3f".format(it.dues.toDouble())

            if( it.dues.toDouble() > 0 )
                printables.add(normalText("Dues:  KWD $dues_str",
                    DefaultPrinter.ALIGNMENT_LEFT, DefaultPrinter.EMPHASIZED_MODE_NORMAL, DefaultPrinter.FONT_SIZE_NORMAL,
                    DefaultPrinter.UNDERLINED_MODE_OFF, 1))

            printables.add(normalText("------------------------------------------------",
                DefaultPrinter.ALIGNMENT_CENTER, DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
                DefaultPrinter.UNDERLINED_MODE_OFF, 1))
        }

        val total_str = "%.3f".format(total)

        printables.add(normalText("TOTAL:  KWD $total_str",
            DefaultPrinter.ALIGNMENT_CENTER, DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_LARGE,
            DefaultPrinter.UNDERLINED_MODE_OFF, 1))

        printables.add(normalText("------------------------------------------------",
            DefaultPrinter.ALIGNMENT_CENTER, DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 1))

        printables.add(normalText("SHUKRAN & JAZAKUM ALLAH KHAIRAN",
            DefaultPrinter.ALIGNMENT_CENTER, DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 2))

        printables.add(normalText("   ",
            DefaultPrinter.ALIGNMENT_LEFT, DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 1))

        printables.add(normalText("   ",
            DefaultPrinter.ALIGNMENT_LEFT, DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 1))

        printables.add(normalText("   ",
            DefaultPrinter.ALIGNMENT_LEFT, DefaultPrinter.EMPHASIZED_MODE_BOLD, DefaultPrinter.FONT_SIZE_NORMAL,
            DefaultPrinter.UNDERLINED_MODE_OFF, 1))


        Printooth.printer().print(printables)
    }

    fun normalText(text: String, alignment: Byte, weight: Byte, size: Byte, underline: Byte, linesAfter: Int): Printable {

        return TextPrintable.Builder()
            .setText(text) //The text you want to print
            .setAlignment(alignment)
            .setEmphasizedMode(weight) //Bold or normal
            .setFontSize(size)
            .setUnderlined(underline) // Underline on/off
            .setCharacterCode(DefaultPrinter.CHARCODE_PC437) // Character code to support languages
            .setLineSpacing(DefaultPrinter.LINE_SPACING_30)
            .setNewLinesAfter(linesAfter) // To provide n lines after sentence
            .build()

    }

    fun updateBalance()
    {
        balance += totalPaying
        glob.SetSharedPrefs(Globariables.SP_BALANCE, balance.toString())
        lbl_Collector.text = "${glob.GetSharedPrefs(Globariables.SP_FULLNAME)} " +
                "| Balance: ${"%.3f".format(balance)}"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK)
            printerConnected = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if( requestCode == Globariables.PSN_READ_PH_ST )
        {
            if( grantResults.size == 0 || grantResults.get(0) != PackageManager.PERMISSION_GRANTED )
            {
                Toast.makeText(this@ActivityMain,
                    "App Permissions Are Required To Run This App", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

}
