package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.helpers.TestBase

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest : TestBase() {
  @Autowired
  lateinit var userRepository: UserRepository

  @Test
  fun `test get saved id types`() {
    val userList = userRepository.findAll()

    assertThat(userList).isNotEmpty()
  }
}
