package com.idfm.hackathon.data.models

enum class TransportationType(val prefix: String) {
    METRO("metro_ligne_"),
    RER("rer_"),
    TRAM("tramway_t")
}
enum class TransportationLine(val type: TransportationType, val line: String) {
    METRO_1(TransportationType.METRO, "1"),
    METRO_2(TransportationType.METRO, "2"),
    METRO_3(TransportationType.METRO, "3"),
    METRO_3b(TransportationType.METRO, "3bis"),
    METRO_4(TransportationType.METRO, "4"),
    METRO_5(TransportationType.METRO, "5"),
    METRO_6(TransportationType.METRO, "6"),
    METRO_7(TransportationType.METRO, "7"),
    METRO_7b(TransportationType.METRO, "7bis"),
    METRO_8(TransportationType.METRO, "8"),
    METRO_9(TransportationType.METRO, "9"),
    METRO_10(TransportationType.METRO, "10"),
    METRO_11(TransportationType.METRO, "11"),
    METRO_12(TransportationType.METRO, "12"),
    METRO_13(TransportationType.METRO, "13"),
    METRO_14(TransportationType.METRO, "14"),
    METRO_15(TransportationType.METRO, "15"),
    METRO_16(TransportationType.METRO, "16"),
    METRO_17(TransportationType.METRO, "17"),

    RER_A(TransportationType.RER, "a"),
    RER_B(TransportationType.RER, "b"),
    RER_C(TransportationType.RER, "c"),
    RER_D(TransportationType.RER, "d"),
    RER_E(TransportationType.RER, "e"),

    TRAM_1(TransportationType.TRAM, "1"),
    TRAM_2(TransportationType.TRAM, "2"),
    TRAM_3a(TransportationType.TRAM, "3a"),
    TRAM_3b(TransportationType.TRAM, "3b"),
    TRAM_4(TransportationType.TRAM, "4"),
    TRAM_5(TransportationType.TRAM, "5"),
    TRAM_6(TransportationType.TRAM, "6"),
    TRAM_7(TransportationType.TRAM, "7"),
    TRAM_8(TransportationType.TRAM, "8"),
    TRAM_9(TransportationType.TRAM, "9"),
    TRAM_10(TransportationType.TRAM, "10"),
    TRAM_11(TransportationType.TRAM, "11"),
    TRAM_12(TransportationType.TRAM, "12"),
    TRAM_13(TransportationType.TRAM, "13"),


    ;

    fun toImageResName() = "${type.prefix}$line"
}