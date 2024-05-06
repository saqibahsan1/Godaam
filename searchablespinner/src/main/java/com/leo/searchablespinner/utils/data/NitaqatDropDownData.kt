package com.leo.searchablespinner.utils.data


class NitaqatDropDownData : ArrayList<NitaqatDropDownDataItem>()

data class NitaqatDropDownDataItem(
    val Economic_Activity: String,
    val Economic_Activity_Id: Int
)

class LeagueName : ArrayList<LeagueItems>()

data class LeagueItems(
    val ID: String? = null,
    val leagueName: String? = null
)