package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "delegated_user_otp")
data class DelegatedUserOTPEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long?,

  @Column(name = "user_id")
  val userId: Long,

  @Column(name = "when_created")
  val creationDate: LocalDateTime,

  @Column(name = "expiry_date")
  var expiryDate: LocalDateTime,

  @Column(name = "otp")
  val otp: String,

  @Column(name = "email")
  var email: String,

)
