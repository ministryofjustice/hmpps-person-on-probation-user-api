package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service

import jakarta.transaction.Transactional
import jakarta.validation.ValidationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.DuplicateDataFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.ResourceNotFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserOTP
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedUserOTPEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.DelegatedUserOTPRepository
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.UserRepository
import java.time.LocalDateTime
import java.util.*

@Service
class DelegatedUserOTPService(private val delegatedUserOTPRepository: DelegatedUserOTPRepository, private val userRepository: UserRepository) {

  @Transactional
  fun getUserOTPByUserId(userId: Long): List<DelegatedUserOTPEntity> {
    val userOTPEntityList = delegatedUserOTPRepository.findByUserIdAndExpiryDateIsAfter(userId, LocalDateTime.now())
    if (userOTPEntityList.isEmpty()) {
      throw ResourceNotFoundException("User OTP for  user  $userId not found in database or OTP expired!")
    }
    return userOTPEntityList
  }

  @Transactional
  fun getUserOTPByUserIdAndEmail(userId: Long, email: String): List<DelegatedUserOTPEntity> {
    val userOTPEntity =
      delegatedUserOTPRepository.findByUserIdAndEmailAndExpiryDateIsAfter(userId, email, LocalDateTime.now())
    if (userOTPEntity.isEmpty()) {
      throw ResourceNotFoundException("User OTP for  user  $userId  and email $email not found in database or OTP expired!")
    }
    return userOTPEntity
  }

  @Transactional
  fun createDelegatedUserOTP(userOTP: UserOTP): DelegatedUserOTPEntity {
    val delegatedUser = userRepository.findByIdOrNull(userOTP.userid)
      ?: throw ResourceNotFoundException("Given Id ${userOTP.userid} not found in the database")

    if (delegatedUser.verified == false) {
      throw ValidationException("Unable to generate OTP for an unverified user")
    }

    if (userOTP.email.isBlank()) {
      throw ValidationException("Missing required parameter 'email' ")
    }

    // For now OTP generated is in 6 digits, for 8 digits the below value should be 99999999
    val otpValue = randomAlphaNumericString()
    val alreadyExistsEntity = delegatedUserOTPRepository.findByUserIdAndEmailAndExpiryDateIsAfter(
      userOTP.userid,
      userOTP.email,
      LocalDateTime.now(),
    )
    val now = LocalDateTime.now()
    if (alreadyExistsEntity.isNotEmpty()) {
      throw DuplicateDataFoundException("Valid OTP already exists for user id ${userOTP.userid}")
    } else {
      val userOTPEntity = DelegatedUserOTPEntity(
        id = null,
        userId = userOTP.userid,
        creationDate = now,
        expiryDate = now.plusDays(30).withHour(23).withMinute(59).withSecond(59),
        otp = otpValue,
        email = userOTP.email,
      )
      return delegatedUserOTPRepository.save(userOTPEntity)
    }
  }

  @Transactional
  fun deleteDelegatedUserOTP(id: Long): DelegatedUserOTPEntity {
    val delegatedUserOTPEntity = delegatedUserOTPRepository.findByIdAndExpiryDateIsAfter(id, LocalDateTime.now()) ?: throw ResourceNotFoundException("Given id $id not found in the database or otp already expired")
    delegatedUserOTPEntity.expiryDate = LocalDateTime.now()
    return delegatedUserOTPRepository.save(delegatedUserOTPEntity)
  }
}
