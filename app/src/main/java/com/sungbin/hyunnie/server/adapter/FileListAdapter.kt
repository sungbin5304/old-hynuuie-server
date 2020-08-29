package com.sungbin.hyunnie.server.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.hyunnie.server.R
import com.sungbin.hyunnie.server.dto.FileListItem
import com.sungbin.sungbintool.DataUtils
import java.util.*
import kotlin.collections.ArrayList

class FileListAdapter(private var list: ArrayList<FileListItem>?, private val ctx: Context) :
    RecyclerView.Adapter<FileListAdapter.FileListViewHolder>() {

    private var clickListener: OnItemClickListener? = null

    init {
        val isSortGanada = DataUtils.readData(ctx, "sort ganada", "true").toBoolean()
        val isSortFolder = DataUtils.readData(ctx, "sort folder", "true").toBoolean()
        sortFolder(isSortFolder)
        sortGanada(isSortGanada)
        sortBack()
    }

    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int, isFile: Boolean)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

    inner class FileListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var icon: ImageView = view.findViewById(R.id.iv_icon)
        var name: TextView = view.findViewById(R.id.tv_file_name)
        var count: TextView = view.findViewById(R.id.tv_file_count)
        var nameFull: TextView = view.findViewById(R.id.tv_file_name_full)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FileListViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.layout_list, viewGroup, false)
        return FileListViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull viewholder: FileListViewHolder, position: Int) {
        val primary = Color.parseColor(DataUtils.readData(ctx, "primary", "#2195f2"))
        val primaryDark = Color.parseColor(DataUtils.readData(ctx, "primaryDark", "#0068bf"))
        val accent = Color.parseColor(DataUtils.readData(ctx, "accent", "#6ec5ff"))
        val isThemeChange = DataUtils.readData(ctx, "theme change", "false").toBoolean()

        val isFile = list!![position].isFile!!
        val name = list!![position].name
        val count = list!![position].count

        if(isFile){
            viewholder.icon.setImageDrawable(
                ctx.getDrawable(R.drawable.ic_insert_drive_file_primary_24dp))
        }
        else {
            viewholder.icon.setImageDrawable(
                ctx.getDrawable(R.drawable.ic_folder_primary_24dp))
        }

        viewholder.nameFull.text = name
        viewholder.name.text = name
        viewholder.count.text = count

        viewholder.count.setOnClickListener {
            clickListener!!.onItemClick(viewholder.nameFull, position, isFile)
        }
        viewholder.icon.setOnClickListener {
            clickListener!!.onItemClick(viewholder.nameFull, position, isFile)
        }
        viewholder.name.setOnClickListener {
            clickListener!!.onItemClick(viewholder.nameFull, position, isFile)
        }
        viewholder.nameFull.setOnClickListener {
            clickListener!!.onItemClick(viewholder.nameFull, position, isFile)
        }

        if(name == "뒤로가기") {
            viewholder.icon.setImageDrawable(
                ctx.getDrawable(R.drawable.ic_keyboard_backspace_primary_24dp))

            viewholder.nameFull.text = name
            viewholder.nameFull.visibility = View.VISIBLE
            viewholder.name.visibility = View.GONE
            viewholder.count.visibility = View.GONE
        }

        if(isThemeChange){
            viewholder.icon.setColorFilter(primary, PorterDuff.Mode.SRC_IN)
            viewholder.name.setTextColor(primary)
            viewholder.nameFull.setTextColor(primary)
            viewholder.count.setTextColor(accent)
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getItem(position: Int): FileListItem {
        return list!![position]
    }

    fun sortGanada(isGanada: Boolean) {
        Collections.sort(list!!, kotlin.Comparator { t, t2 ->
            return@Comparator t.name!!.compareTo(t2.name!!)
        })
        if(!isGanada) list!!.reverse()
        sortBack()
    }

    fun sortFolder(isFile: Boolean){
        Collections.sort(list!!, kotlin.Comparator { t, t2 ->
            return@Comparator if(isFile) t.isFile!!.compareTo(t2.isFile!!)
                else t2.isFile!!.compareTo(t.isFile!!)
        })
        sortBack()
    }

    fun sortBack(){
        var item: FileListItem? = null
        for(element in list!!){
            if(element.name == "뒤로가기"){
                item = element
            }
            else continue
        }
        if(item != null){
            list!!.remove(item)
            list!!.add(0, item)
        }
        notifyDataSetChanged()
    }

    fun sortSearch(search: String){
        var item: FileListItem? = null
        for(element in list!!){
            if(element.name!!.toLowerCase(Locale.getDefault())
                    .contains(search.toLowerCase(Locale.getDefault()))){
                item = element
            }
            else continue
        }
        if(item != null){
            list!!.remove(item)
            list!!.add(0, item)
        }
        sortBack()
    }

}
