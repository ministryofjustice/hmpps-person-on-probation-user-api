package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service

import jakarta.transaction.Transactional
import jakarta.validation.ValidationException
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.DuplicateDataFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.ResourceNotFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserPatch
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserPost
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.UserRepository
import java.time.LocalDateTime

@Service
class UserService(private val userRepository: UserRepository) {

  @Transactional
  fun getUserByCRN(crn: String): MutableList<UserEntity> {
    val user = userRepository.findByCrn(crn)
    if (user.size == 0) {
      throw ResourceNotFoundException("User with CRN $crn not found in database")
    }
    return user
  }

  @Transactional
  fun getAllUsers(): MutableList<UserEntity> {
    val user = userRepository.findAll()
      ?: throw ResourceNotFoundException("Unable to fetch the person on probation users List in database")
    return user
  }

  @Transactional
  fun createUser(userPost: UserPost): UserEntity {
    val now = LocalDateTime.now()

    if ((
        userPost.verified == true &&
          userPost.crn != null &&
          userPost.cprId != null &&
          userPost.verified != null &&
          userPost.nomsId != null &&
          userPost.oneLoginUrn != null
        ) ||
      (userPost.verified == false && userPost.oneLoginUrn != null)
    ) {
      val userExistsWithURN = userRepository.findByOneLoginUrn(userPost.oneLoginUrn!!)
      if (userExistsWithURN != null) {
        throw DuplicateDataFoundException("User with One Login URN  ${userPost.oneLoginUrn} already exists in the  database")
      }
      val userEntity: UserEntity
      if (userPost.verified == true) {
        userEntity = UserEntity(
          id = null,
          crn = userPost.crn!!,
          cprId = userPost.cprId!!,
          verified = userPost.verified,
          creationDate = now,
          modifiedDate = now,
          nomsId = userPost.nomsId!!,
          oneLoginUrn = userPost.oneLoginUrn!!,
        )
        return userRepository.save(userEntity)
      } else {
        userEntity = UserEntity(
          id = null,
          crn = null.toString(),
          cprId = null.toString(),
          verified = userPost.verified,
          creationDate = now,
          modifiedDate = now,
          nomsId = null.toString(),
          oneLoginUrn = userPost.oneLoginUrn!!,
        )
        return userRepository.save(userEntity)
      }
    }
    throw ValidationException(
      "Request invalid. " +
        "crn= ${userPost.crn} " +
        "cprId= ${userPost.cprId} " +
        "nomsId= ${userPost.nomsId} " +
        "verified=${userPost.verified} " +
        "oneLoginUrn=${userPost.oneLoginUrn} ",

    )
  }

  @Transactional
  fun updateUser(existingUser: UserEntity, existingUserPatchDTO: UserPatch): UserEntity {
    val now = LocalDateTime.now()
    existingUser.crn = existingUserPatchDTO.crn ?: existingUser.crn
    existingUser.verified = existingUserPatchDTO.verified ?: existingUser.verified
    existingUser.cprId = existingUserPatchDTO.cprId ?: existingUser.cprId
    existingUser.nomsId = existingUserPatchDTO.nomsId ?: existingUser.nomsId
    existingUser.oneLoginUrn = existingUserPatchDTO.oneLoginUrn ?: existingUser.oneLoginUrn

    existingUser.modifiedDate = now
    return userRepository.save(existingUser)
  }

  @Transactional
  fun getUserByCrnAndUserId(crn: String, userId: Long): UserEntity? {
    val user = userRepository.findByIdAndCrn(userId, crn)
      ?: throw ResourceNotFoundException("User with Id $userId and CRN $crn not found in database")
    return user
  }

  @Transactional
  fun getUserByUrn(urn: String): UserEntity? {
    val user = userRepository.findByOneLoginUrn(urn)
      ?: throw ResourceNotFoundException("User with One Login URN $urn not found in database")
    return user
  }
}
