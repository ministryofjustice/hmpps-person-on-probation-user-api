package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data

data class UserPost(
  var crn: String? = null,
  var cprId: String? = null,
  var verified: Boolean? = null,
  var nomsId: String? = null,
  var oneLoginUrn: String? = null,
)

data class UserPatch(
  var crn: String? = null,
  var cprId: String? = null,
  var verified: Boolean?,
  var nomsId: String? = null,
  var oneLoginUrn: String? = null,
)
