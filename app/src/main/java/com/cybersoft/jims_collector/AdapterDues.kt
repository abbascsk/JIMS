package com.cybersoft.jims_collector

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cybersoft.jims_collector.models.Dues
import kotlinx.android.synthetic.main.item_dues.view.*

class AdapterDues(val context: Context, val amountChanged: (Double) -> Unit ) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private lateinit var data: MutableList<Dues.DuesHead>

    private val TYPE_ITEM = 1
    private val TYPE_HEADING = 2

    fun setDataSource(data: MutableList<Dues.DuesHead>) {
        this.data = data
        amountChanged(this@AdapterDues.getTotalAmount())
        this.notifyDataSetChanged()
    }

    fun getDataSource(): List<Dues.DuesHead> {
        return this.data
    }

    fun addItem(item: Dues.DuesHead)
    {
        this.data.add(item)
        amountChanged(this@AdapterDues.getTotalAmount())
        this.notifyDataSetChanged()
    }

    fun getTotalAmount(): Double {

        var total = 0.0

        if( this.data.size > 0 )
        {
            this.data.forEach {
                total += it.amountToBePaid
            }

            return total
        }
        else
            return total
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if( viewType == TYPE_HEADING )
            return HeadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dues_heading, parent, false))
        else
            return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dues, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val head = data[position]

        if( getItemViewType(position) == TYPE_ITEM )
        {
            val hold = holder as ItemViewHolder

            hold.colDuesHead?.text = head.headName
            hold.colDuesAmount?.text = "%.3f".format(head.amount)
            hold.txtAmount?.setText( "%.3f".format(head.amountToBePaid))

            hold.txtAmount?.addTextChangedListener( object : TextWatcher {

                override fun afterTextChanged(value: Editable?) {
                    if( value != null && value.isNotEmpty() )
                        head.amountToBePaid = value.toString().toDouble()
                    else
                        head.amountToBePaid = 0.0

                    amountChanged(this@AdapterDues.getTotalAmount())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            } )

        }

    }

    override fun getItemCount(): Int {
        return if( data == null ) 0 else data.size
    }

    override fun getItemViewType(position: Int): Int {
        if( data[position].headID == -1 )
            return TYPE_HEADING
        else
            return TYPE_ITEM
    }

    class HeadingViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    }

    class ItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val colDuesHead: TextView = view.col_DuesHead
        val colDuesAmount: TextView = view.col_DuesAmount
        val txtAmount: EditText = view.txt_Amount
    }

}