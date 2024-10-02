package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "delegated_access_permission")
data class DelegatedAccessPermissionEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long?,

  @Column(name = "delegated_access_id")
  var delegatedAccessId: Long,

  @Column(name = "permission_id")
  var permissionId: Long,

  @Column(name = "when_granted")
  val granted: LocalDateTime,

  @Column(name = "when_revoked")
  var revoked: LocalDateTime?,

)
