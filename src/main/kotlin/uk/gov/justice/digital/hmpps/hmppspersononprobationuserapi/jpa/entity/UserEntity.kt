package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "person_on_probation_user")
data class UserEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long?,

  @Column(name = "crn")
  val crn: String,

  @Column(name = "cpr_id")
  val cprId: String,

  @Column(name = "email")
  val email: String,

  @Column(name = "verified")
  val verified: Boolean,

  @Column(name = "when_created")
  val creationDate: LocalDateTime,

  @Column(name = "when_modified")
  val modifiedDate: LocalDateTime,

)