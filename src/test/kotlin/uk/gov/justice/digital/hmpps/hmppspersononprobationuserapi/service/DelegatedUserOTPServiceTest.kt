package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.DuplicateDataFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserOTP
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedUserOTPEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.DelegatedUserOTPRepository
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.UserRepository
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class DelegatedUserOTPServiceTest {
  private lateinit var delegatedUserOTPService: DelegatedUserOTPService

  @Mock
  private lateinit var userRepository: UserRepository

  @Mock
  private lateinit var delegatedUserOTPRepository: DelegatedUserOTPRepository

  private val fakeNow = LocalDateTime.parse("2024-02-12T14:33:26")

  @BeforeEach
  fun beforeEach() {
    delegatedUserOTPService = DelegatedUserOTPService(delegatedUserOTPRepository, userRepository)
  }

  @Test
  fun `test getDelegatedUserOTPByEmail - returns delegated user OTP details`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    val delegatedUserOTPEntity = DelegatedUserOTPEntity(1, 1, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2025-02-12T14:33:26"), "G123", "user1@gmail.com")
    val delegatedUserOTPEntityList = emptyList<DelegatedUserOTPEntity>().toMutableList()
    delegatedUserOTPEntityList.add(delegatedUserOTPEntity)
    Mockito.`when`(delegatedUserOTPRepository.findByUserIdAndEmailAndExpiryDateIsAfter(1, delegatedUserOTPEntity.email, LocalDateTime.now())).thenReturn(delegatedUserOTPEntityList)
    val result = delegatedUserOTPService.getUserOTPByUserIdAndEmail(1, "user1@gmail.com")
    Assertions.assertEquals(delegatedUserOTPEntityList, result)
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test getDelegatedUserOTPByUserId - returns delegated user OTP details`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    val delegatedUserOTPEntity = DelegatedUserOTPEntity(null, 1, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2025-02-12T14:33:26"), "G123", "user1@gmail.com")
    val delegatedUserOTPEntityList = emptyList<DelegatedUserOTPEntity>().toMutableList()
    delegatedUserOTPEntityList.add(delegatedUserOTPEntity)
    Mockito.`when`(delegatedUserOTPRepository.findByUserIdAndExpiryDateIsAfter(1, LocalDateTime.now()))
      .thenReturn(delegatedUserOTPEntityList)
    val result = delegatedUserOTPService.getUserOTPByUserId(1)
    Assertions.assertEquals(delegatedUserOTPEntityList, result)
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test delegated user otp - creates and returns delegated user otp`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    mockkStatic(::randomAlphaNumericString)
    every {
      randomAlphaNumericString()
    } returns "1X3456"
    val userEntity1 = UserEntity(id = 1, crn = "abc", cprId = "123", verified = true, creationDate = fakeNow, modifiedDate = fakeNow, nomsId = "G123", oneLoginUrn = "urn1")

    val delegatedUserOTPEntity = DelegatedUserOTPEntity(null, 1, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-03-13T23:59:59"), "1X3456", "user1@gmail.com")

    val delegatedUserOTPList = emptyList<DelegatedUserOTPEntity>().toMutableList()
    val userOTP = UserOTP(
      userid = 1,
      email = "user1@gmail.com",
    )

    Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.of(userEntity1))
    Mockito.lenient().`when`(delegatedUserOTPRepository.findByUserIdAndEmailAndExpiryDateIsAfter(any(), any(), any())).thenReturn(
      delegatedUserOTPList,
    )
    Mockito.`when`(delegatedUserOTPRepository.save(any())).thenReturn(delegatedUserOTPEntity)
    val result = delegatedUserOTPService.createDelegatedUserOTP(userOTP)

    Mockito.verify(delegatedUserOTPRepository).save(delegatedUserOTPEntity)
    Assertions.assertEquals(delegatedUserOTPEntity, result)
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test user - creates delegated user duplicate otp  happy path`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    val userEntity1 = UserEntity(id = 1, crn = "abc", cprId = "123", verified = true, creationDate = fakeNow, modifiedDate = fakeNow, nomsId = "G123", oneLoginUrn = "urn1")

    val delegatedUserOTPEntity = DelegatedUserOTPEntity(1, 1, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2025-02-12T14:33:26"), "G123", "user2@gmail.com")
    val delegatedUserOTPList = emptyList<DelegatedUserOTPEntity>().toMutableList()
    delegatedUserOTPList.add(delegatedUserOTPEntity)
    val userOTP = UserOTP(
      userid = 1,
      email = "user1@gmail.com",
    )
    Mockito.`when`(userRepository.findById(any())).thenReturn(Optional.of(userEntity1))
    Mockito.lenient().`when`(delegatedUserOTPRepository.findByUserIdAndEmailAndExpiryDateIsAfter(any(), any(), any())).thenReturn(delegatedUserOTPList)
    assertThrows<DuplicateDataFoundException> { delegatedUserOTPService.createDelegatedUserOTP(userOTP) }
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test delegated user otp - delete and returns delegated user otp`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    val delegatedUserOTPEntity = DelegatedUserOTPEntity(null, 1, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2025-02-12T14:33:26"), "G123", "user2@gmail.com")
    val delegatedUserOTPList = emptyList<DelegatedUserOTPEntity>().toMutableList()
    val userOTP = UserOTP(
      userid = 1,
      email = "user1@gmail.com",
    )
    Mockito.`when`(delegatedUserOTPRepository.save(any())).thenReturn(delegatedUserOTPEntity)
    Mockito.`when`(delegatedUserOTPRepository.findByIdAndExpiryDateIsAfter(any(), any())).thenReturn(delegatedUserOTPEntity)

    val result = delegatedUserOTPService.deleteDelegatedUserOTP(1)
    Mockito.verify(delegatedUserOTPRepository).save(delegatedUserOTPEntity)
    delegatedUserOTPEntity.expiryDate = LocalDateTime.now()
    Assertions.assertEquals(delegatedUserOTPEntity, result)
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test generate otp is 6 digits and AlphaNumeric`() {
    repeat(1000) {
      val otpValue = randomAlphaNumericString()
      Assertions.assertEquals(otpValue.length, 6)
      for (ch in otpValue) {
        Assertions.assertEquals(ch.toString().matches("[a-zA-Z0-9]".toRegex()), true)
      }
    }
  }
}
