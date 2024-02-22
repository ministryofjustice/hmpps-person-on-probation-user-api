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
  var crn: String,

  @Column(name = "cpr_id")
  var cprId: String,

  @Column(name = "email")
  var email: String,

  @Column(name = "verified")
  var verified: Boolean?,

  @Column(name = "when_created")
  val creationDate: LocalDateTime,

  @Column(name = "when_modified")
  var modifiedDate: LocalDateTime,

  @Column(name = "noms_id")
  var nomsId: String,

  @Column(name = "one_login_urn")
  var oneLoginUrn: String,

)
