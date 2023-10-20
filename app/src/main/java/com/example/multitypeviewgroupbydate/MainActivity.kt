package com.example.multitypeviewgroupbydate

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.multitypeviewgroupbydate.MainActivity.ListItem.Companion.TYPE_DATE
import com.example.multitypeviewgroupbydate.MainActivity.ListItem.Companion.TYPE_GENERAL


class MainActivity : AppCompatActivity() {
    private val myOptions: MutableList<PojoOfJsonArray> = ArrayList()
    var consolidatedList: MutableList<ListItem> = ArrayList()

    private var mRecyclerView: RecyclerView? = null
    private var adapter: ViewTypeAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRecyclerView = findViewById<View>(R.id.rcv_view) as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)
        myOptions.add(PojoOfJsonArray("name 1", "2016-06-21"))
        myOptions.add(PojoOfJsonArray("name 2", "2016-06-05"))
        myOptions.add(PojoOfJsonArray("name 2", "2016-06-05"))
        myOptions.add(PojoOfJsonArray("name 3", "2016-05-17"))
        myOptions.add(PojoOfJsonArray("name 3", "2016-05-17"))
        myOptions.add(PojoOfJsonArray("name 3", "2016-05-17"))
        myOptions.add(PojoOfJsonArray("name 3", "2016-05-17"))
        myOptions.add(PojoOfJsonArray("name 2", "2016-06-05"))
        myOptions.add(PojoOfJsonArray("name 3", "2016-05-17"))
        val groupedHashMap = groupDataIntoHashMap(myOptions)
        for (date in groupedHashMap.keys) {
            val dateItem = DateItem()
            dateItem.date = (date)
            consolidatedList.add(dateItem)
            for (pojoOfJsonArray in groupedHashMap[date]!!) {
                val generalItem = GeneralItem()
                generalItem.pojoOfJsonArray =(pojoOfJsonArray) //setBookingDataTabs(bookingDataTabs);
                consolidatedList.add(generalItem)
            }
        }
        adapter = ViewTypeAdapter(this, consolidatedList)
//        val layoutManager = LinearLayoutManager(this)
//        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val gridLayoutManager = GridLayoutManager(this,3)
        gridLayoutManager.setSpanSizeLookup(object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter!!.getItemViewType(position)) {
                    TYPE_GENERAL -> 1
                    TYPE_DATE -> 3
                    else -> 1
                }
            }
        })
        mRecyclerView!!.layoutManager = gridLayoutManager

            mRecyclerView!!.adapter = adapter
    }

    private fun groupDataIntoHashMap(listOfPojosOfJsonArray: List<PojoOfJsonArray>): HashMap<String, MutableList<PojoOfJsonArray>> {
        val groupedHashMap = HashMap<String, MutableList<PojoOfJsonArray>>()
        for (pojoOfJsonArray in listOfPojosOfJsonArray) {
            val hashMapKey = pojoOfJsonArray.date
            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap[hashMapKey]!!.add(pojoOfJsonArray)
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                val list: MutableList<PojoOfJsonArray> = ArrayList()
                list.add(pojoOfJsonArray)
                groupedHashMap[hashMapKey] = list
            }
        }
        return groupedHashMap
    }
    abstract class ListItem {
        abstract fun type(): Int

        companion object {
            const val TYPE_DATE = 0
            const val TYPE_GENERAL = 1
        }
    }

    class GeneralItem : ListItem() {
        var pojoOfJsonArray: PojoOfJsonArray? = null
        override fun type(): Int {
            return TYPE_GENERAL
        }

    }

    class DateItem : ListItem() {
        var date: String? = null
        override fun type(): Int {
            return TYPE_DATE
        }

    }


    class ViewTypeAdapter(context: Context, consolidatedList: MutableList<ListItem>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
        private val mContext: Context
        private var consolidatedList: List<ListItem> = ArrayList()

        init {
            this.consolidatedList = consolidatedList
            mContext = context
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var viewHolder: RecyclerView.ViewHolder? = null
            val inflater = LayoutInflater.from(parent.context)
            when (viewType) {
                TYPE_GENERAL -> {
                    val v1: View = inflater.inflate(
                        R.layout.items, parent,
                        false
                    )
                    viewHolder = GeneralViewHolder(v1)
                }

                TYPE_DATE -> {
                    val v2: View = inflater.inflate(R.layout.itemsh, parent, false)
                    viewHolder = DateViewHolder(v2)
                }
            }
            return viewHolder!!
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder.itemViewType) {
                TYPE_GENERAL -> {
                    val generalItem = consolidatedList!![position] as GeneralItem
                    val generalViewHolder = holder as GeneralViewHolder?
                    generalViewHolder!!.txtTitle.text = generalItem.pojoOfJsonArray!!.name
                }

                TYPE_DATE -> {
                    val dateItem = consolidatedList!![position] as DateItem
                    val dateViewHolder = holder as DateViewHolder?
                    dateViewHolder!!.txtTitle.text = dateItem.date
                }
            }
        }


        // ViewHolder for date row item
        internal inner class DateViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var txtTitle: TextView

            init {
                txtTitle = v.findViewById<View>(R.id.txt) as TextView
            }
        }

        // View holder for general row item
        internal inner class GeneralViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var txtTitle: TextView

            init {
                txtTitle = v.findViewById<View>(R.id.txt) as TextView
            }
        }

        override fun getItemViewType(position: Int): Int {
            return consolidatedList!![position].type()
        }

        override fun getItemCount(): Int {
            return if (consolidatedList != null) consolidatedList!!.size else 0
        }
    }
}