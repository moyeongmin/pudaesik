package com.example.bbudaesik.utils

object BuildingInfo {

    val AppWidgetResMap = mapOf(
        "금정회관 교직원" to "PG001",
        "금정회관 학생" to "PG002",
        "샛벌회관" to "PS001",
        "편의동 2층" to "Y001",
        "학생회관 학생" to "PH002",
        "학생회관(밀양) 교직원" to "M002",
        "학생회관(밀양) 학생" to "M001",
        "진리관" to "2",
        "웅비관" to "11",
        "자유관" to "13",
        "비마관" to "3",
        "행림관" to "12",
    )


    val restaurantMap = mapOf(
        0 to listOf("PG002", "PG001", "PS001", "PH002"),//"금정회관 학생 식당", "금정회관 교직원 식당", "샛벌회관 식당", "학생회관 학생 식당"
        1 to listOf("M001", "M002"),//"학생회관(밀양) 학생 식당", "학생회관(밀양) 교직원 식당"
        2 to listOf("Y001")//"편의동2층(양산)
    )

    val dormitoryResMap = mapOf(
        0 to listOf("2", "11", "13"),// 진리관 2 웅비관 11 자유관 13
        1 to listOf("3"), // 비마관 3
        2 to listOf("12") // 행림관 12
    )

    val resCodeToNm = mapOf(
        "PG001" to "금정회관 교직원식당",
        "PG002" to "금정회관 학생식당",
        "PS001" to "샛벌회관식당",
        "PH002" to "학생회관 학생식당",
        "M001" to "학생회관(밀양) 학생식당",
        "M002" to "학생회관(밀양) 교직원식당",
        "Y001" to "편의동2층(양산)식당"
    )
    /** 기숙사 코드 */
    val regionDormitories = mapOf(
        "진리관" to "2",
        "웅비관" to "11",
        "자유관" to "13",
        "비마관" to "3",
        "행림관" to "12"
    )

    val dorNoToNm = mapOf(
        "2" to "진리관",
        "11" to "웅비관",
        "13" to "자유관",
        "3" to "비마관",
        "12" to "행림관"
    )

    /** 메뉴 시간 코드 */
    val menuTimeCodes = mapOf(
        "01" to "조기",
        "02" to "조식",
        "B" to "조식",
        "03" to "중식",
        "L" to "중식",
        "04" to "석식",
        "D" to "석식"
    )
}
