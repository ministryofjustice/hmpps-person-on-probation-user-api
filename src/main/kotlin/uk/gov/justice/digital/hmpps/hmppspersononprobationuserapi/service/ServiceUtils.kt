package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service

import org.slf4j.LoggerFactory
import java.security.SecureRandom
import kotlin.streams.asSequence

private val log = LoggerFactory.getLogger(object {}::class.java.`package`.name)

private val random = ThreadLocal.withInitial { SecureRandom() }
const val STRING_LENGTH = 6
private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun randomAlphaNumericString(): String = random.get()
  .ints(STRING_LENGTH.toLong(), 0, charPool.size)
  .asSequence()
  .map(charPool::get)
  .joinToString("")
