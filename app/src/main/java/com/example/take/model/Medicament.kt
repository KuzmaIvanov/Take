package com.example.take.model

data class Medicament(
    val id: Long,
    val name: String,
    val time: List<String>
)

//Расширенная версия класса Medicament, который используется для того, чтобы узнать более подробную инфу
//Можно добавить еще поля для более подробной информации (например, подгружать какую-то доп. инфу)
data class MedicamentDetails(
    val medicament: Medicament,
    val details: String
)