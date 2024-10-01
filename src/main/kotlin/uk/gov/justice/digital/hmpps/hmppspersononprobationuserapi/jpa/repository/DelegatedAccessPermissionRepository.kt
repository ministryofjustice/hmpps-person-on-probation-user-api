package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessPermissionEntity
import java.util.*

@Repository
interface DelegatedAccessPermissionRepository : JpaRepository<DelegatedAccessPermissionEntity, Long> {

  fun findByIdAndRevokedIsNull(id: Long): DelegatedAccessPermissionEntity?

  fun findByIdAndRevokedIsNotNull(id: Long): DelegatedAccessPermissionEntity?

  fun findByDelegatedAccessId(accessId: Long): List<DelegatedAccessPermissionEntity>

  fun findByDelegatedAccessIdAndPermissionIdAndGrantedIsNotNullAndRevokedIsNull(accessId: Long, permissionId: Long): DelegatedAccessPermissionEntity?

  fun findByDelegatedAccessIdAndGrantedIsNotNullAndRevokedIsNull(accessId: Long): List<DelegatedAccessPermissionEntity>
}
