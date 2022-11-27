package com.portfolio.fcfsreward.repository.domain.reword.entity

import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.RewordSuppliedHistory
import com.portfolio.fcfsreward.repository.domain.user.entity.UserEntity
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
internal class RewordEntity(
    @Id
    val id: UUID,
    val name: String,
    val description: String,
    val limitCount: Long,
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val history: List<RewordHistoryEntity>
) {
    fun toDomain(): Reword = Reword(
        id = id,
        name = name,
        description = description,
        limitCount = limitCount,
        history = history.map { it.toDomain() }
    )
}

@Embeddable
internal class RewordHistoryId(
    @Column
    val rewordId: UUID,
    @Column
    val date: LocalDate,
) : Serializable

@Entity
internal class RewordHistoryEntity(
    @EmbeddedId
    val id: RewordHistoryId,
    @OneToMany(fetch = FetchType.LAZY,  cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val suppliedHistories: List<RewordSuppliedHistoryEntity>
) {
    fun toDomain(): RewordHistory = RewordHistory(
        rewordId = id.rewordId,
        date = id.date,
        suppliedHistories = suppliedHistories.map { it.toDomain() }.toMutableList()
    )
}

@Entity
internal class RewordSuppliedHistoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val seq: Long?,
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "reword_history_id")
    val rewordHistory: RewordHistoryEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val reset: Boolean,
    val suppliedPoint: Long,
    val createDate: LocalDateTime
) {
    fun toDomain(): RewordSuppliedHistory = RewordSuppliedHistory(
        seq = seq,
        rewordId = rewordHistory.id.rewordId,
        userId = user.id,
        reset = reset,
        createDate = createDate,
        suppliedPoint = suppliedPoint
    )
}