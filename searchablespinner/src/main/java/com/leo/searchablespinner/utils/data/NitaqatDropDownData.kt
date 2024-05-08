package com.leo.searchablespinner.utils.data


class NitaqatDropDownData : ArrayList<NitaqatDropDownDataItem>()

data class NitaqatDropDownDataItem(
    val Economic_Activity: String,
    val Economic_Activity_Id: Int
)

data class LeagueItems(
    val ID: String? = null,
    val leagueName: String? = null
)

data class JobItems(
    val ID: String? = null,
    val jobDutyTitle: String? = null
)
data class AccountItems(
    val id: String? = null,
    val name: String? = null
)