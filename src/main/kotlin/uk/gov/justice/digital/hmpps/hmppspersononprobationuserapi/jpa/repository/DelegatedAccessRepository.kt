package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessEntity
import java.util.*

@Repository
interface DelegatedAccessRepository : JpaRepository<DelegatedAccessEntity, Long> {

  fun findByInitiatedUserIdAndDelegatedUserIdAndDeletedDateIsNull(initiatedUserId: Long, delegatedUserId: Long): DelegatedAccessEntity?

  fun findByIdAndDeletedDateIsNull(id: Long): DelegatedAccessEntity?

  fun findByInitiatedUserId(initiatedUserId: Long): List<DelegatedAccessEntity>

  fun findByInitiatedUserIdAndDeletedDateIsNull(initiatedUserId: Long): List<DelegatedAccessEntity>

  fun findByDelegatedUserId(delegatedUserId: Long): List<DelegatedAccessEntity>

  fun findByDelegatedUserIdAndDeletedDateIsNull(delegatedUserId: Long): List<DelegatedAccessEntity>
}
