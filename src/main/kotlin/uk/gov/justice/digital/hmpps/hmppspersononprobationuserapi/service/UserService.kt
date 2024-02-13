package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.ResourceNotFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.UserRepository

@Service
class UserService(private val userRepository: UserRepository) {

  @Transactional
  open fun getUserByCRN(crn: String): UserEntity? {
    val user = userRepository.findByCrn(crn)
      ?: throw ResourceNotFoundException("User with CRN $crn not found in database")
    return user
  }

  @Transactional
  open fun getAllUsers(): MutableList<UserEntity> {
    val user = userRepository.findAll()
      ?: throw ResourceNotFoundException("Unable to fetch the person on probation users List in database")
    return user
  }
}
