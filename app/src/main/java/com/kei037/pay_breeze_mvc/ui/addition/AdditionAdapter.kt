package com.kei037.pay_breeze_mvc.ui.addition

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import com.kei037.pay_breeze_mvc.databinding.ItemAdditionBinding

/**
 * AdditionAdapter : RecyclerView 를 위한 어댑터, 트랜잭션 항목 표시
 *
 * @property onDeleteClick : 항목 삭제 클릭 리스너
 */
class AdditionAdapter(private val onDeleteClick: (TransactionEntity) -> Unit) :
    ListAdapter<TransactionEntity, AdditionAdapter.AdditionViewHolder>(AdditionDiffCallback()) {

    /**
     * view holder 생성
     *
     * @param parent : 부모 뷰 그룹
     * @param viewType : 뷰 타입
     * @return 생성된 view holder
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdditionViewHolder {
        val binding =
            ItemAdditionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdditionViewHolder(binding)
    }

    /**
     * view holder 에 데이터 binding
     *
     * @param holder : 뷰 홀더
     * @param position : 항목 위치
     */
    override fun onBindViewHolder(holder: AdditionViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick)
    }

    /**
     * AdditionViewHolder : 트랜잭션 항목의 뷰 홀더
     *
     * @property binding : 항목 뷰 바인딩 객체
     */
    class AdditionViewHolder(private val binding: ItemAdditionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * 뷰 홀더에 트랜잭션 데이터 바인딩
         *
         * @param transaction : 트랜잭션 엔티티
         * @param onDeleteClick : 항목 삭제 클릭 리스너
         */
        fun bind(transaction: TransactionEntity, onDeleteClick: (TransactionEntity) -> Unit) {
            // 트랜잭션 제목 설정
            binding.title.text = transaction.title
            // 트랜잭션 금액 설정
            binding.category.text = transaction.categoryName

            // 트랜잭션 금액이 음수인지 여부에 따라 색상 및 텍스트 설정
            if (transaction.amount < 0) {
                binding.amount.setTextColor(Color.BLACK)
                binding.amount.text = "-${-transaction.amount}"
            } else {
                binding.amount.setTextColor(Color.BLUE)
                binding.amount.text = transaction.amount.toString()
            }

            // 삭제 아이콘 클릭 리스너 설정
            binding.ivDelete.setOnClickListener {
                onDeleteClick(transaction)
            }
        }
    }

    /**
     * AdditionDiffCallback : 트랜잭션 항목 간 차이 계산
     */
    class AdditionDiffCallback : DiffUtil.ItemCallback<TransactionEntity>() {

        /**
         * 두 트랜잭션 항목이 동일한 항목인지 비교
         *
         * @param oldItem : 기존 항목
         * @param newItem : 새로운 항목
         * @return 두 항목이 동일한 경우 true, 그렇지 않은 경우 false
         */
        override fun areItemsTheSame(
            oldItem: TransactionEntity,
            newItem: TransactionEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        /**
         * 두 트랜잭션 항목의 콘텐츠가 동일한지 비교
         *
         * @param oldItem : 기존 항목
         * @param newItem : 새로운 항목
         * @return 두 항목의 콘텐츠가 동일한 경우 true, 그렇지 않은 경우 false
         */
        override fun areContentsTheSame(
            oldItem: TransactionEntity,
            newItem: TransactionEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}
