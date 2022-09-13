package com.cybersoft.jims_collector

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cybersoft.jims_collector.models.Receipt
import kotlinx.android.synthetic.main.item_report.view.*

class AdapterReport(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private lateinit var data: MutableList<Receipt.ReceiptData>

    fun setDataSource(data: MutableList<Receipt.ReceiptData>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    fun getDataSource(): List<Receipt.ReceiptData> {
        return this.data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_report, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val head = data[position]

        val hold = holder as ItemViewHolder

        hold.col_ReceiptNo?.text = head.receiptNo
        hold.col_ReceiptDate?.text = head.receiptDate
        hold.col_MemberName?.text = head.memberName
        hold.col_MemberDetails?.text = "Sabeel No: ${head.sabeelNo} - Contact No: ${head.memberContact}"
        hold.col_HeadName?.text = head.headName
        hold.col_Amount?.text = "KWD %.3f".format(head.amount.toDouble())

    }

    override fun getItemCount(): Int {
        return if( data == null ) 0 else data.size
    }

    class ItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val col_ReceiptNo: TextView = view.col_ReceiptNo
        val col_ReceiptDate: TextView = view.col_ReceiptDate
        val col_MemberName: TextView = view.col_MemberName
        val col_MemberDetails: TextView = view.col_MemberDetails
        val col_HeadName: TextView = view.col_HeadName
        val col_Amount: TextView = view.col_Amount
    }

}