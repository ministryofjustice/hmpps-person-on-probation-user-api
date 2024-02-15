package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data

data class UserPost(
  var crn: String? = null,
  var email: String? = null,
  var cprId: String? = null,
  var verified: Boolean? = null,
)

data class UserPatch(
  var crn: String? = null,
  var cprId: String? = null,
  var email: String? = null,
  var verified: Boolean?,
)
