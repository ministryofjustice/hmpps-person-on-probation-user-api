package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "delegated_access")
data class DelegatedAccessEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long?,

  @Column(name = "initiated_user_id")
  var initiatedUserId: Long,

  @Column(name = "delegated_user_id")
  var delegatedUserId: Long,

  @Column(name = "when_created")
  val createdDate: LocalDateTime,

  @Column(name = "when_deleted")
  var deletedDate: LocalDateTime?,

)
