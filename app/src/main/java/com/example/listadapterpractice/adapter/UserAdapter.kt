package com.example.listadapterpractice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.listadapterpractice.R
import com.example.listadapterpractice.model.User
import com.orhanobut.logger.Logger

class UserAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<User, UserAdapter.ViewHolder>(diffUtil) {

    //todo : 예제 프로젝트에서는 아래와 같은 코드가 있었습니다. 근데 제가 공부했기로는 어댑터에서 listData 를 안쓰려는 이유도 DiffUtil을 쓰는 이유중 하나인 것으로 이해했는데 이게 맞는지 모르겠네요.
    private val userData = mutableListOf<User>()

    interface ItemClickListener{
        fun itemClick(user : User, position : Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater.from(parent.context)
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.user_itme_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Logger.v("onBindViewHolder called")
        // ListAdaptr 가 없었으면 썼을 코드
        //holder.bind(userData[position])

        holder.bind(getItem(position))
    }

//    fun setData(petArrayList: List<User>) {
//        val petDiffUtilCallback = UserDiffCallback(userData, petArrayList)
//        val diffResult = DiffUtil.calculateDiff(petDiffUtilCallback)
//        dataSet.clear()
//        dataSet.addAll(petArrayList)
//        diffResult.dispatchUpdatesTo(this)
//    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        fun bind(user: User) {
            userName.text = user.name
        }

        init{
            itemView.setOnClickListener{
                itemClickListener.itemClick(getItem(adapterPosition), adapterPosition)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {

                // User properties may have changed if reloaded from the DB, but ID is fixed.
                //추가적으로 좀 검색을해서 찾아보니까 여기서 말하는 getId() 가 DB에서 식별이 가능한 PK 값 같은 것을 의미한다고 한다.
                // return oldItem.getId() == newItem.getId()
                Logger.v("areItemTheSame : ${oldItem.name == newItem.name}")
                return oldItem.name == newItem.name
            }

            //todo : 애초에 oldItem 이랑 newItem 객체가 같은 시점부터 답이 없다. 뭔짓을 해도 true를 반환할거 아닌가.
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {

                // 아래 로그가 서로 같은 시점부터 걍 망한거다.
                Logger.v(oldItem.hashCode().toString())
                Logger.v(newItem.hashCode().toString())

                // NOTE: if you use equals, your object must properly override Object#equals()
                // Incorrectly returning false here will result in too many animations.
                // 동등성 비교를 해야한다. 객체안의 서로 내용이 같은지를 비교해야함.
                return oldItem == newItem
            }

        }
    }

}