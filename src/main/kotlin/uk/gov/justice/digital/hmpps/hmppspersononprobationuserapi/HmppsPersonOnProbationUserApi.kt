package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

const val SYSTEM_USERNAME = "PERSON_ON_PROBATION_API"

@SpringBootApplication
class HmppsPersonOnProbationUserApi

fun main(args: Array<String>) {
  runApplication<HmppsPersonOnProbationUserApi>(*args)
}
