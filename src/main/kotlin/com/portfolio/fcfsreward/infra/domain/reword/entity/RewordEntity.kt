package com.portfolio.fcfsreward.infra.domain.reword.entity

import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.RewordSuppliedHistory
import com.portfolio.fcfsreward.infra.domain.user.entity.UserEntity
import com.portfolio.fcfsreward.infra.util.converter.UuidConverter
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.OneToMany

@Entity
internal class RewordEntity(
    @Id
    @Convert(converter = UuidConverter::class)
    @Column(name = "reword_id", columnDefinition = "BINARY(16)")
    val rewordId: UUID,
    @Column(name = "name")
    val name: String,
    @Column(name = "description")
    val description: String,
    @Column(name = "limitCount")
    val limitCount: Long,
) {
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE], mappedBy = "reword")
    lateinit var history: List<RewordHistoryEntity>
    fun toDomain(): Reword = Reword(
        id = rewordId,
        name = name,
        description = description,
        limitCount = limitCount,
        history = history.map { it.toDomain() }
    )

}

@Embeddable
internal data class RewordHistoryId(
    @Convert(converter = UuidConverter::class)
    @Column(name = "reword_id", columnDefinition = "BINARY(16)")
    val rewordId: UUID,
    @Column(name = "history_date")
    val date: LocalDate,
) : Serializable

@Entity
internal class RewordHistoryEntity(
    @MapsId("rewordId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reword_id")
    val reword: RewordEntity,
    @EmbeddedId
    val id: RewordHistoryId,
) {

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE], mappedBy = "rewordHistory")
    lateinit var suppliedHistories: List<RewordSuppliedHistoryEntity>
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
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(name = "bridge_reword_hisotry_supplied")
    val rewordHistory: RewordHistoryEntity,
    @ManyToOne(fetch = FetchType.LAZY) // 바운더리 밖이라 Lazy로딩
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    @Column(name = "continuous_reset")
    val reset: Boolean,
    @Column(name = "supplied_point")
    val suppliedPoint: Long,
    @Column(name = "generate_date")
    val generateDate: LocalDateTime
) {
    fun toDomain(): RewordSuppliedHistory = RewordSuppliedHistory(
        seq = seq,
        rewordId = rewordHistory.id.rewordId,
        userId = user.id,
        reset = reset,
        generateDate = generateDate,
        suppliedPoint = suppliedPoint
    )

}