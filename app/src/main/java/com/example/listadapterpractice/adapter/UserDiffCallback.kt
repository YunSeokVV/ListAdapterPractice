package com.example.listadapterpractice.adapter
// 출처 : https://medium.com/android-news/smart-way-to-update-recyclerview-using-diffutil-345941a160e0
// DiffUtil.Callback 을 상속받는 클래스다. ListAdapter를 쓰는 예제를 참고해서 봤어야 했는데 잘못봤다.
import androidx.recyclerview.widget.DiffUtil
import com.example.listadapterpractice.model.User
import com.orhanobut.logger.Logger

// 업데이트 기능이 동작하게 되면 submitList의 list 값이 얕은복사가 되서 실제로 수정된 list말고 수정되기 이전의 list가 적용된다. 깊은 복사를 사용해야지 제대로 수정된 값을 볼 수 있다.
//이를 위해서 DiffUtil.Callback() 을 사용한다.
class UserDiffCallback(private val oldUserList : List<User>, private val newUserList : List<User>) : DiffUtil.Callback() {

//    private val oldUserList = listOf<User>()
//    private val newUserList = listOf<User>()

    override fun getOldListSize(): Int {
        return oldUserList.size
    }

    override fun getNewListSize(): Int {
        return newUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        Logger.v((oldUserList.get(oldItemPosition).name == newUserList.get(newItemPosition).name).toString())
        return oldUserList.get(oldItemPosition).name == newUserList.get(newItemPosition).name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser : User = oldUserList.get(oldItemPosition)
        val newUser : User = newUserList.get(newItemPosition)
        Logger.v(oldUser.equals(newUser).toString())
        return oldUser.equals(newUser)
    }

    //areItemTheSame() 메소드가 true를 반환하고 areContentsTheSame() 메소드가 false를 반환하면 DiffUtil 유틸리티는 변경된 값을 받기위해서 이 메소드를 호출한다.
    //이 메소드가 리턴한 객체는 notifyItemRangeChanged()를 사용해서 DiffResult에서 적용되고, 이 메소드는 어댑터의 onBindViewHolder 메소드를 호출한다.
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}