package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedUserOTPEntity
import java.time.LocalDateTime

@Repository
interface DelegatedUserOTPRepository : JpaRepository<DelegatedUserOTPEntity, Long> {
  fun findByUserIdAndExpiryDateIsAfter(userId: Long, date: LocalDateTime): List<DelegatedUserOTPEntity>

  fun findByUserIdAndEmailAndExpiryDateIsAfter(userId: Long, email: String, expiryDate: LocalDateTime): List<DelegatedUserOTPEntity>

  fun findByIdAndExpiryDateIsAfter(id: Long, date: LocalDateTime): DelegatedUserOTPEntity?
}
