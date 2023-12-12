package com.example.listadapterpractice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listadapterpractice.R
import com.example.listadapterpractice.model.Search
import com.example.listadapterpractice.model.User
import com.example.listadapterpractice.model.ViewType
import com.orhanobut.logger.Logger
import java.lang.IllegalStateException

class UserAdapter(
    private val itemClickListener: ItemClickListener,
    private val searchBtnListener: SearchBtnListener,
) :
    ListAdapter<ViewType, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        private const val SEARCH_TYPE = 0
        private const val USER_TYPE = 1
        private val diffUtil = object : DiffUtil.ItemCallback<ViewType>() {

            override fun areItemsTheSame(oldItem: ViewType, newItem: ViewType): Boolean {

                val oldUser = (oldItem as? User)?: return true
                val newUser = (newItem as? User)?: return true

                // User properties may have changed if reloaded from the DB, but ID is fixed.
                //추가적으로 좀 검색을해서 찾아보니까 여기서 말하는 getId() 가 DB에서 식별이 가능한 PK 값 같은 것을 의미한다고 한다.
                // return oldItem.getId() == newItem.getId()
                //Logger.v("areItemTheSame : ${oldItem.name == newItem.name}")
                return oldUser.name == newUser.name
            }

            //todo : 애초에 oldItem 이랑 newItem 객체가 같은 시점부터 답이 없다. 뭔짓을 해도 true를 반환할거 아닌가.
            override fun areContentsTheSame(oldItem: ViewType, newItem: ViewType): Boolean {

                return oldItem == newItem
            }


        }
    }

    override fun getItemViewType(position: Int): Int {

        val data = getItem(position)
        return when(data){
            is Search -> SEARCH_TYPE
            is User -> USER_TYPE
            else -> throw IllegalStateException()
        }
    }

    //todo : 반드시 이 메소드를 오버라이드 해야하는지 확인해보기
//    override fun getItemCount(): Int {
//        return super.getItemCount()
//    }

    interface SearchBtnListener {
        fun itemSearch(userName: String)
    }

    interface ItemClickListener {
        fun itemClick(viewType: ViewType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        LayoutInflater.from(parent.context)
        return when(viewType){
            SEARCH_TYPE -> SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_user, parent, false))
            USER_TYPE -> SearchResultViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_itme_list, parent, false))
            else -> throw IllegalStateException()
        }

//        if (viewType == ViewTypeInteger.SEARCH) {
//            val itemView =
//                LayoutInflater.from(parent.context).inflate(R.layout.search_user, parent, false)
//            return SearchViewHolder(itemView)
//        } else {
//            val itemView =
//                LayoutInflater.from(parent.context).inflate(R.layout.user_itme_list, parent, false)
//            return SearchResultViewHolder(itemView)
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is SearchViewHolder -> holder.bind()
            is SearchResultViewHolder -> holder.bind(getItem(position))
            else -> throw IllegalStateException()
        }

//        //todo : when절로 바꾸기
//        if (holder is SearchViewHolder) {
//            holder.bind()
//
//        } else if (holder is SearchResultViewHolder) {
//            holder.bind(getItem(position))
//        }
    }


    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val searchKeyWord: EditText = itemView.findViewById(R.id.searchKeyWord)
        val searchBtn: Button = itemView.findViewById(R.id.searchButton)


        fun bind() {
            searchBtn.setOnClickListener {
                searchBtnListener.itemSearch(searchKeyWord.text.toString())
            }
        }

    }

    inner class SearchResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = itemView.findViewById(R.id.userName)

        //fun bind(user: ViewType) {
        fun bind(user: ViewType) {

            val user = user as User
            userName.text = user.name

        }

        init {
            itemView.setOnClickListener {
                itemClickListener.itemClick(getItem(adapterPosition))
            }
        }
    }

}