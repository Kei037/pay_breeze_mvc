package com.kei037.pay_breeze_mvc.ui.calender.calenderAdapter

import android.util.Log
import com.kei037.pay_breeze_mvc.data.db.entity.TransactionEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// 날짜를 나타내는 데이터 클래스
data class DateItem(val date: String) : ListItem {
    // ListItem 인터페이스를 구현하여 ViewType 반환
    override fun getType(): ViewType = ViewType.DATE_HEADER
}

// 이벤트를 나타내는 데이터 클래스
data class EventItem(val transaction: TransactionEntity) : ListItem {
    // ListItem 인터페이스를 구현하여 ViewType 반환
    override fun getType(): ViewType = ViewType.EVENT_ITEM
}

// ListItem 인터페이스 정의
interface ListItem {
    // 각 항목의 ViewType을 반환하는 함수
    fun getType(): ViewType
}

// 트랜잭션을 날짜별로 그룹화하여 ListItem 리스트로 반환하는 함수
fun groupEventsByDate(transactions: List<TransactionEntity>): List<ListItem> {
    val grouped = mutableListOf<ListItem>()
    // 트랜잭션을 날짜별로 그룹화
    val groupedByDate = transactions.groupBy { it.transactionDate }

    // 그룹화된 각 날짜별로 DateItem과 EventItem 추가  ex) date = 2023-01-01
    for ((date, events) in groupedByDate) {
        val formattedDate = formatDateString(date)

        Log.i("날짜 테스트 ==== ", formattedDate)
        grouped.add(DateItem(formattedDate))
        events.forEach { grouped.add(EventItem(it)) }
    }

    // 최종 그룹화된 리스트 반환
    return grouped
}

fun formatDateString(dateString: String): String {
    // 날짜 문자열을 LocalDate로 파싱
    val date = LocalDate.parse(dateString)

    // 원하는 출력 형식 정의
    val formatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)

    // LocalDate를 원하는 형식의 문자열로 변환
    return date.format(formatter)
}
