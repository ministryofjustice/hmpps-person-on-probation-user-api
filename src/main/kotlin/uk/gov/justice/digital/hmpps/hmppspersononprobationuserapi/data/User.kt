package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data

import java.time.LocalDate

data class UserPost(
  var crn: String? = null,
  var email: String? = null,
  var cprId: String? = null,
  var verified: Boolean? = null,
  var nomsId: String? = null,
  var oneLoginUrn: String? = null,
  var prisonId: String? = null,
  var releaseDate: LocalDate? = null,
)

data class UserPatch(
  var crn: String? = null,
  var cprId: String? = null,
  var email: String? = null,
  var verified: Boolean?,
  var nomsId: String? = null,
  var oneLoginUrn: String? = null,
  var prisonId: String? = null,
  var releaseDate: LocalDate? = null,
)
