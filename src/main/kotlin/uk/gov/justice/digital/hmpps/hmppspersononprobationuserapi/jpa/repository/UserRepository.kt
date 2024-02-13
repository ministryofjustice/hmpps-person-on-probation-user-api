package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
  fun findByCrn(crnId: String): UserEntity?
}
